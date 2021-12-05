package secure.notes;

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
   JTextField titleTextField;
   JEditorPane bodyPane;
   JPasswordField indvPasswordTextField;
   JButton saveButton;
   JMenuItem newNoteItem;
   JMenuItem deleteNoteItem;
   JMenuItem keybindingsItem;
   JMenuItem aboutItem;
   JMenuItem userSettingsItem;
   JMenuItem deleteUserItem;

   DefaultListModel<String> titleListModel;
   List<Note> notesList;
   int shownNote = -1;

   DBConnection dbConnection;

   SecureNotes() {
      super();
      initComponents();
      setupNotes();
   }

   void initComponents() {
      jFrame = new JFrame("Secure notes");
      jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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

      newNoteItem = new JMenuItem("New note");
      newNoteItem.setFont(Constants.FONT);
      newNoteItem.setMnemonic(VK_N);
      newNoteItem.addActionListener(this);
      file.add(newNoteItem);

      deleteNoteItem = new JMenuItem("Delete note currently displayed");
      deleteNoteItem.setFont(Constants.FONT);
      deleteNoteItem.setMnemonic(VK_D);
      deleteNoteItem.addActionListener(this);
      file.add(deleteNoteItem);

      JMenu help = new JMenu("Help");
      help.setFont(Constants.FONT);
      help.setMnemonic(VK_H);
      menuBar.add(help);

      keybindingsItem = new JMenuItem("Keybindings");
      keybindingsItem.setFont(Constants.FONT);
      keybindingsItem.setMnemonic(VK_K);
      keybindingsItem.addActionListener(this);
      help.add(keybindingsItem);

      aboutItem = new JMenuItem("About");
      aboutItem.setFont(Constants.FONT);
      aboutItem.setMnemonic(VK_A);
      aboutItem.addActionListener(this);
      help.add(aboutItem);

      JMenu settings = new JMenu("Settings");
      settings.setFont(Constants.FONT);
      settings.setMnemonic(VK_S);
      menuBar.add(settings);

      userSettingsItem = new JMenuItem("Change user settings");
      userSettingsItem.setFont(Constants.FONT);
      userSettingsItem.setMnemonic(VK_C);
      userSettingsItem.addActionListener(this);
      settings.add(userSettingsItem);

      deleteUserItem = new JMenuItem("Delete user");
      deleteUserItem.setFont(Constants.FONT);
      deleteUserItem.setMnemonic(VK_D);
      deleteUserItem.addActionListener(this);
      settings.add(deleteUserItem);

      titleListModel = new DefaultListModel<>();
      titleListModel.addElement("* New Note *");
      titleList = new JList<>(titleListModel);
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

      saveButton = new JButton("Save note");
      saveButton.setFont(Constants.FONT);
      saveButton.setBackground(Constants.BUTTON_COLOR);
      saveButton.addActionListener(this);
      noteEditorPanel.add(saveButton, BorderLayout.SOUTH);

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

      titleTextField = new JTextField();
      titleTextField.setFont(Constants.FONT);
      randomPanel1.add(titleTextField);

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

      indvPasswordTextField = new JPasswordField();
      indvPasswordTextField.setFont(Constants.FONT);
      randomPanel2.add(indvPasswordTextField);

      jFrame.setMinimumSize(new Dimension(900, 500));
      jFrame.setPreferredSize(new Dimension(1000, 600));
      jFrame.setLocationRelativeTo(null);
      jFrame.setVisible(true);
   }

   void setupNotes() {
      notesList = new ArrayList<>();
      dbConnection = new DBConnection();

      try {
         ResultSet resultSet = dbConnection.selectNotes(UserData.getUsername());
         while (resultSet.next()) {
            String decTitle = Crypt.decrypt(resultSet.getString(2), String.valueOf(UserData.getPassword()));
            String decBody = Crypt.decrypt(resultSet.getString(3), String.valueOf(UserData.getPassword()));
            String decPassword = Crypt.decrypt(resultSet.getString(4), String.valueOf(UserData.getPassword()));
            notesList.add(new Note(resultSet.getInt(1), decTitle, decBody, decPassword));
            titleListModel.addElement(decTitle);
         }
      } catch (SQLException e) {
         dbConnection.close();
      }

      for (int i = 0; i < notesList.size(); i++) {
         if (notesList.get(i).password == null) {
            showNote(i + 1);
            break;
         }
      }

      if (shownNote == -1) {
         titleTextField.setEnabled(false);
         bodyPane.setEnabled(false);
         indvPasswordTextField.setEnabled(false);
         saveButton.setEnabled(false);
      }
   }

   void showNote(int index) {
      if (UserData.isBlocked()) {
         return;
      }

      titleTextField.setEnabled(true);
      bodyPane.setEnabled(true);
      indvPasswordTextField.setEnabled(true);
      saveButton.setEnabled(true);

      if (shownNote > 0) {
         notesList.set(shownNote - 1, new Note(notesList.get(shownNote - 1).id, titleTextField.getText(), bodyPane.getText(), new String(indvPasswordTextField.getPassword()).length() == 0 ? null : new String(indvPasswordTextField.getPassword())));
      }

      String title = titleListModel.getElementAt(index--);
      String body = notesList.get(index).body;
      String password = notesList.get(index).password;

      if (password == null) {
         titleTextField.setText(title);
         bodyPane.setText(body);
         indvPasswordTextField.setText("");

         shownNote = index + 1;
         titleList.setSelectedIndex(shownNote);
         return;
      }

      int finalIndex = index;
      new EnterPasswordFrame(password.toCharArray(), () -> {
         titleTextField.setText(title);
         bodyPane.setText(body);
         indvPasswordTextField.setText(password);

         shownNote = finalIndex + 1;
         titleList.setSelectedIndex(shownNote);
      });
   }

   void createNote() {
      notesList.add(new Note("[Untitled note]", "", null));
      titleListModel.addElement("[Untitled note]");
      showNote(notesList.size());
   }

   void saveNote(int shownNote) {
      if (shownNote < 1) return;

      titleList.setSelectedIndex(shownNote);
      int index = shownNote - 1;
      Note note = notesList.get(index);
      Note newNote = new Note(note.id, titleTextField.getText(), bodyPane.getText(), new String(indvPasswordTextField.getPassword()).length() == 0 ? null : new String(indvPasswordTextField.getPassword()));
      String encTitle = Crypt.encrypt(newNote.title, UserData.getPassword());
      String encBody = Crypt.encrypt(newNote.body, UserData.getPassword());
      String encPassword = Crypt.encrypt(newNote.password, UserData.getPassword());

      if (newNote.title.length() == 0 || newNote.title.length() > 255) {
         new TextDialog("Title length error", "Title has to be 1 to 255 characters long");
         return;
      }

      try {
         if (note.id == 0) {
            if (insertNote(index, newNote, encTitle, encBody, encPassword)) {
               return;
            }
         } else {
            dbConnection.updateNote(UserData.getUsername(), encTitle, encBody, encPassword, newNote.id);
         }
      } catch (SQLException e) {
         e.printStackTrace();
         new TextDialog("Note not saved", "Note failed to save correctly: Check connection and try again");
         return;
      }

      notesList.set(index, newNote);
      titleListModel.setElementAt(newNote.title, ++index);
   }

   boolean insertNote(int index, Note newNote, String encTitle, String encBody, String encPassword) {
      boolean titleTaken = false;
      for (int i = 0; i < notesList.size(); i++) {
         if (notesList.get(i).title.equals(newNote.title) && i != index) {
            titleTaken = true;
            break;
         }
      }

      if (titleTaken) {
         new TextDialog("Title taken", "The title is already is use");
         return true;
      } else {
         try {
            dbConnection.insertNote(UserData.getUsername(), encTitle, encBody, encPassword);
            ResultSet resultSet;
            resultSet = dbConnection.selectId(UserData.getUsername());
            resultSet.next();
            newNote.id = resultSet.getInt(1);
         } catch (SQLException e) {
            e.printStackTrace();
            return true;
         }
      }

      return false;
   }

   void deleteNote() {
      if (shownNote == 0) {
         return;
      }

      CallbackEvent delete = () -> {
         if (notesList.get(shownNote - 1).id != 0) {
            try {
               dbConnection.deleteNote(notesList.get(shownNote - 1).id);
            } catch (SQLException e) {
               e.printStackTrace();
               new TextDialog("Not deleted", "Note failed to delete, check connection");
            }
         }

         notesList.remove(shownNote - 1);
         titleListModel.removeElementAt(shownNote);
      };

      if (notesList.get(shownNote - 1).password == null) {
         delete.doEvent();
      } else {
         new EnterPasswordFrame(UserData.getPassword(), delete);
      }
   }

   void close() {
      if (jFrame != null) {
         jFrame.dispose();
         dbConnection.close();
      }
   }

   void showKeybindings() {
      StringBuilder keybindingsFile = new StringBuilder("Error loading help file");
      try {
         keybindingsFile = new StringBuilder();
         Scanner scanner = new Scanner(new File(Constants.RESOURCE_PATH + "keybindings.txt"));
         while (scanner.hasNextLine()) {
            keybindingsFile.append(scanner.nextLine()).append("\n");
         }
         scanner.close();
      } catch (FileNotFoundException ex) {
         ex.printStackTrace();
      }
      new TextDialog("Keybindings", keybindingsFile.toString());
   }

   void showAbout() {
      StringBuilder aboutFile = new StringBuilder("Error loading help file");
      try {
         aboutFile = new StringBuilder();
         Scanner scanner = new Scanner(new File(Constants.RESOURCE_PATH + "about.txt"));
         while (scanner.hasNextLine()) {
            aboutFile.append(scanner.nextLine()).append("\n");
         }
         scanner.close();
      } catch (FileNotFoundException ex) {
         ex.printStackTrace();
      }
      new TextDialog("About", String.format(aboutFile.toString(), Constants.MAX_ATTEMPT, Constants.TIMEOUT_DURATION));
   }

   void changeUserSettings() {
      new EnterPasswordFrame(UserData.getPassword(), () -> new ChangeUserSettingsFrame(() -> {
         for (int i = 0; i < notesList.size(); i++) {
            if (notesList.get(i).id != 0) {
               saveNote(i + 1);
            }
         }
      }));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JComponent component = (JComponent) e.getSource();

      if (component == newNoteItem) {
         createNote();
      } else if (component == deleteNoteItem) {
         deleteNote();
      } else if (component == keybindingsItem) {
         showKeybindings();
      } else if (component == aboutItem) {
         showAbout();
      } else if (component == userSettingsItem) {
         changeUserSettings();
      } else if (component == saveButton) {
         saveNote(shownNote);
      } else if (component == deleteUserItem) {
         new EnterPasswordFrame(UserData.getPassword(), () -> new DeleteUserFrame(this::close));
      }
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 1) {
         int index = titleList.locationToIndex(e.getPoint());
         if (index == titleListModel.size()) return;
         if (index == 0) {
            createNote();
         } else {
            showNote(index);
         }
      }
   }

   @Override
   public void mousePressed(MouseEvent e) {
      // Unused listener method
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      // Unused listener method
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      // Unused listener method
   }

   @Override
   public void mouseExited(MouseEvent e) {
      // Unused listener method
   }

   @Override
   public void keyTyped(KeyEvent e) {
      // Unused listener method
   }

   @Override
   public void keyPressed(KeyEvent e) {
      // Unused listener method
   }

   @Override
   public void keyReleased(KeyEvent e) {
      // Unused listener method
   }
}
