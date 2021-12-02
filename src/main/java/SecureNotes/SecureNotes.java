package SecureNotes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.awt.event.KeyEvent.*;

class SecureNotes extends JPanel implements ActionListener, KeyListener, MouseListener {
   JFrame jFrame;
   JList<String> titleList;
   JScrollPane titleScroll;
   JTextField titleBar;
   JEditorPane bodyPane;
   JPasswordField indvPasswordBar;
   JButton save;
   JMenuItem newNote;
   JMenuItem deleteNote;
   JMenuItem keybindings;
   JMenuItem about;
   JMenuItem userSettings;
   JMenuItem deleteUser;

   DefaultListModel<String> titles;
   List<Note> notes;
   ArrayList<Integer> strikes;
   int shownNote = -1;

   DBConnection dbConnection;

   SecureNotes() {
      super();
      initComponents();
   }

   void initComponents() {
      notes = new ArrayList<>();
      strikes = new ArrayList<>();

      jFrame = new JFrame("Secure notes");
      jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      jFrame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            close();
            dbConnection.close();
         }
      });
      jFrame.setLayout(new BorderLayout(3, 3));
      jFrame.setIconImage(Constants.ICON);

      setLayout(new BorderLayout(3, 3));
      setBorder(new EmptyBorder(10, 10, 10, 10));
      jFrame.add(this, BorderLayout.CENTER);

      JMenuBar menuBar = new JMenuBar();
      menuBar.setBackground(new Color(246, 246, 246));
      jFrame.setJMenuBar(menuBar);

      JMenu file = new JMenu("File");
      file.setFont(Constants.FONT);
      file.setMnemonic(VK_F);
      menuBar.add(file);

      newNote = new JMenuItem("New note");
      newNote.setFont(Constants.FONT);
      newNote.setMnemonic(VK_N);
      newNote.addActionListener(this);
      file.add(newNote);

      deleteNote = new JMenuItem("Delete note currently displayed");
      deleteNote.setFont(Constants.FONT);
      deleteNote.setMnemonic(VK_D);
      deleteNote.addActionListener(this);
      file.add(deleteNote);

      JMenu help = new JMenu("Help");
      help.setFont(Constants.FONT);
      help.setMnemonic(VK_H);
      menuBar.add(help);

      keybindings = new JMenuItem("Keybindings");
      keybindings.setFont(Constants.FONT);
      keybindings.setMnemonic(VK_K);
      keybindings.addActionListener(this);
      help.add(keybindings);

      about = new JMenuItem("About");
      about.setFont(Constants.FONT);
      about.setMnemonic(VK_A);
      about.addActionListener(this);
      help.add(about);

      JMenu settings = new JMenu("Settings");
      settings.setFont(Constants.FONT);
      settings.setMnemonic(VK_S);
      menuBar.add(settings);

      userSettings = new JMenuItem("Change user settings");
      userSettings.setFont(Constants.FONT);
      userSettings.setMnemonic(VK_C);
      userSettings.addActionListener(this);
      settings.add(userSettings);

      deleteUser = new JMenuItem("Delete user");
      deleteUser.setFont(Constants.FONT);
      deleteUser.setMnemonic(VK_D);
      deleteUser.addActionListener(this);
      settings.add(deleteUser);

      titles = new DefaultListModel<>();
      titles.addElement("* New Note *");
      titleList = new JList<>(titles);
      titleList.setFont(Constants.FONT);
      titleList.setFixedCellHeight(20);
      titleList.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == VK_DELETE && titleList.getSelectedIndex() > 0) {
               deleteNote();
            }
         }
      });
      titleList.addMouseListener(this);
      DefaultListCellRenderer renderer = (DefaultListCellRenderer) titleList.getCellRenderer();
      renderer.setHorizontalAlignment(SwingConstants.CENTER);
      titleList.setPreferredSize(new Dimension(200, 600));
      titleScroll = new JScrollPane(titleList);
      add(titleScroll, BorderLayout.WEST);

      JPanel noteEditorPanel = new JPanel();
      noteEditorPanel.setLayout(new BorderLayout(5, 5));
      noteEditorPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
      add(noteEditorPanel, BorderLayout.CENTER);

      save = new JButton("Save note");
      save.setFont(Constants.FONT);
      save.setBackground(Constants.BUTTON_COLOR);
      save.addActionListener(this);
      noteEditorPanel.add(save, BorderLayout.SOUTH);

      JPanel contentEditorPanel = new JPanel();
      contentEditorPanel.setLayout(new BorderLayout(5, 5));
      noteEditorPanel.add(contentEditorPanel, BorderLayout.CENTER);

      JPanel randomPanel1 = new JPanel();
      randomPanel1.setLayout(new BoxLayout(randomPanel1, BoxLayout.LINE_AXIS));
      contentEditorPanel.add(randomPanel1, BorderLayout.NORTH);

      JLabel l1 = new JLabel("Title:");
      l1.setFont(Constants.FONT);
      randomPanel1.add(l1);

      randomPanel1.add(Box.createRigidArea(new Dimension(5, 0)));

      titleBar = new JTextField();
      titleBar.setFont(Constants.FONT);
      randomPanel1.add(titleBar);

      bodyPane = new JEditorPane();
      bodyPane.setFont(Constants.FONT);
      JScrollPane bodyScroll = new JScrollPane(bodyPane);
      contentEditorPanel.add(bodyScroll, BorderLayout.CENTER);

      JPanel randomPanel2 = new JPanel();
      randomPanel2.setLayout(new BoxLayout(randomPanel2, BoxLayout.LINE_AXIS));
      contentEditorPanel.add(randomPanel2, BorderLayout.SOUTH);

      JLabel l2 = new JLabel("[May be blank] Password:");
      l2.setFont(Constants.FONT);
      randomPanel2.add(l2);

      randomPanel2.add(Box.createRigidArea(new Dimension(5, 0)));

      indvPasswordBar = new JPasswordField();
      indvPasswordBar.setFont(Constants.FONT);
      randomPanel2.add(indvPasswordBar);

      jFrame.setMinimumSize(new Dimension(900, 500));
      jFrame.setPreferredSize(new Dimension(1000, 600));
      jFrame.setLocationRelativeTo(null);
      jFrame.setVisible(true);

      dbConnection = new DBConnection();
      try {
         ResultSet resultSet = dbConnection.selectNotes(UserData.username);
         while (resultSet.next()) {
            String decTitle = Crypt.decrypt(resultSet.getString(2), new String(UserData.password));
            String decBody = Crypt.decrypt(resultSet.getString(3), new String(UserData.password));
            String decPassword = Crypt.decrypt(resultSet.getString(4), new String(UserData.password));
            notes.add(new Note(resultSet.getInt(1), decTitle, decBody, decPassword));
            titles.addElement(decTitle);
         }
      } catch (SQLException e) {
         dbConnection.close();
      }

      for (int i = 0; i < notes.size(); i++) {
         if (notes.get(i).password == null) {
            showNote(++i);
            break;
         }
      }

      if (shownNote == -1) {
         titleBar.setEnabled(false);
         bodyPane.setEnabled(false);
         indvPasswordBar.setEnabled(false);
         save.setEnabled(false);
      }
   }

   void showNote(int index) {
      titleBar.setEnabled(true);
      bodyPane.setEnabled(true);
      indvPasswordBar.setEnabled(true);
      save.setEnabled(true);

      if (shownNote > 0) {
         notes.set(shownNote - 1, new Note(notes.get(shownNote - 1).id, titleBar.getText(), bodyPane.getText(), new String(indvPasswordBar.getPassword()).length() == 0 ? null : new String(indvPasswordBar.getPassword())));
      }


      String title = titles.getElementAt(index--);
      String body = notes.get(index).body;
      String password = notes.get(index).password;

      if (password == null) {
         titleBar.setText(title);
         bodyPane.setText(body);
         indvPasswordBar.setText("");

         shownNote = index + 1;
         titleList.setSelectedIndex(shownNote);
         return;
      }

      int finalIndex = index;
      new EnterPasswordFrame(password.toCharArray(), () -> {
         titleBar.setText(title);
         bodyPane.setText(body);
         indvPasswordBar.setText(password);


         shownNote = finalIndex + 1;
         titleList.setSelectedIndex(shownNote);
      });
   }

   void createNote() {
      notes.add(new Note("[Untitled note]", "", null));
      titles.addElement("[Untitled note]");
      showNote(notes.size());
   }

   void saveNote(int shownNote) {
      if (shownNote == 0) return;

      titleList.setSelectedIndex(shownNote);
      int index = shownNote - 1;
      Note note = notes.get(index);
      Note newNote = new Note(note.id, titleBar.getText(), bodyPane.getText(), new String(indvPasswordBar.getPassword()).length() == 0 ? null : new String(indvPasswordBar.getPassword()));

      if (newNote.title.length() == 0 || newNote.title.length() >= 256) {
         new TextDialog("Title length error", "Title has to be 1 to 255 characters long");
         return;
      }

      if (note.id == 0) {
         boolean titleTaken = false;
         for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).title.equals(newNote.title) && i != index) {
               titleTaken = true;
               break;
            }
         }
         if (titleTaken) {
            new TextDialog("Title taken", "The title is already is use");
            return;
         } else {
            try {
               String encTitle = Crypt.encrypt(newNote.title, UserData.password);
               String encBody = Crypt.encrypt(newNote.body, UserData.password);
               String encPassword = Crypt.encrypt(newNote.password, UserData.password);
               dbConnection.insertNote(UserData.username, encTitle, encBody, encPassword);
               ResultSet resultSet = dbConnection.selectId(UserData.username);
               resultSet.next();
               newNote.id = resultSet.getInt(1);
            } catch (SQLException e) {
               e.printStackTrace();
               new TextDialog("Note not saved", "Note failed to save correctly: Check connection and try again");
               return;
            }
         }
      } else {
         try {
            String encTitle = Crypt.encrypt(newNote.title, UserData.password);
            String encBody = Crypt.encrypt(newNote.body, UserData.password);
            String encPassword = Crypt.encrypt(newNote.password, UserData.password);
            dbConnection.updateNote(UserData.username, encTitle, encBody, encPassword, newNote.id);
         } catch (SQLException e) {
            e.printStackTrace();
            new TextDialog("Note not saved", "Note failed to save correctly: Check connection and try again");
            return;
         }
      }

      notes.set(index, newNote);
      titles.setElementAt(newNote.title, ++index);
   }

   void deleteNote() {
      if (shownNote == 0) {
         return;
      }

      CallbackEvent delete = () -> {
         if (notes.get(shownNote - 1).id != 0) {
            try {
               dbConnection.deleteNote(notes.get(shownNote - 1).id);
            } catch (SQLException e) {
               e.printStackTrace();
               new TextDialog("Not deleted", "Note failed to delete, check connection");
            }
         }

         notes.remove(shownNote - 1);
         titles.removeElementAt(shownNote);
      };

      if (notes.get(shownNote - 1).password == null) {
         delete.doEvent();
      } else {
         new EnterPasswordFrame(UserData.password, delete);
      }
   }

   void close() {
      if (jFrame != null) {
         jFrame.dispose();
         dbConnection.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object component = e.getSource();

      if (component == newNote) {
         createNote();
      } else if (component == deleteNote) {
         deleteNote();
      } else if (component == keybindings) {
         StringBuilder keybindingsFile = new StringBuilder("Error loading help file");
         try {
            keybindingsFile = new StringBuilder();
            Scanner scanner = new Scanner(new File(Constants.RESOURCE_PATH + "keybindings.txt"));
            while (scanner.hasNextLine()) {
               keybindingsFile.append(scanner.nextLine()).append("\n");
            }
         } catch (FileNotFoundException ex) {
            ex.printStackTrace();
         }
         new TextDialog("Keybindings", keybindingsFile.toString());
      } else if (component == about) {
         StringBuilder aboutFile = new StringBuilder("Error loading help file");
         try {
            aboutFile = new StringBuilder();
            Scanner scanner = new Scanner(new File(Constants.RESOURCE_PATH + "about.txt"));
            while (scanner.hasNextLine()) {
               aboutFile.append(scanner.nextLine()).append("\n");
            }
         } catch (FileNotFoundException ex) {
            ex.printStackTrace();
         }
         new TextDialog("About", String.format(aboutFile.toString(), Constants.MAX_ATTEMPT, Constants.TIMEOUT_DURATION));
      } else if (component == userSettings) {
         new EnterPasswordFrame(UserData.password, () -> new ChangeUserSettingsFrame(() -> {
            for (int i = 0; i < notes.size(); i++) {
               if (notes.get(i).id != 0) {
                  saveNote(i + 1);
               }
            }
         }));
      } else if (component == save) {
         saveNote(shownNote);
      } else if (component == deleteUser) {
         new EnterPasswordFrame(UserData.password, () -> new DeleteUserFrame(this::close));
      }
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 1) {
         int index = titleList.locationToIndex(e.getPoint());
         if (index == titles.size()) return;
         if (index == 0) {
            createNote();
         } else {
            showNote(index);
         }
      }
   }

   @Override
   public void mousePressed(MouseEvent e) {

   }

   @Override
   public void mouseReleased(MouseEvent e) {

   }

   @Override
   public void mouseEntered(MouseEvent e) {

   }

   @Override
   public void mouseExited(MouseEvent e) {

   }

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {

   }

   @Override
   public void keyReleased(KeyEvent e) {

   }
}
