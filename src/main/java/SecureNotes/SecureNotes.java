package SecureNotes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.awt.event.KeyEvent.*;

public class SecureNotes extends JPanel implements ActionListener, KeyListener, MouseListener {
   JFrame jFrame;
   JList<String> titleList;
   JScrollPane titleScroll;
   JTextField titleBar;
   JEditorPane bodyPane;
   JPasswordField indvPasswordBar;
   JButton save;
   JMenuItem newNote;
   JMenuItem deleteNote;
   JMenuItem saveAs;
   JMenuItem keybindings;
   JMenuItem about;
   JMenuItem userSettings;
   JMenuItem deleteUser;

   DefaultListModel<String> titles;
   volatile List<Note> notes;
   ArrayList<Integer> strikes;

   DBConnection dbConnection;

   public SecureNotes() {
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

      deleteNote = new JMenuItem("Delete selected note(s)");
      deleteNote.setFont(Constants.FONT);
      deleteNote.setMnemonic(VK_D);
      deleteNote.addActionListener(this);
      file.add(deleteNote);

      saveAs = new JMenuItem("Save as");
      saveAs.setFont(Constants.FONT);
      saveAs.setMnemonic(VK_V);
      saveAs.addActionListener(this);
      file.add(saveAs);

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
      deleteUser.setMnemonic(VK_E);
      deleteUser.addActionListener(this);
      settings.add(deleteUser);

      titles = new DefaultListModel<>();
      titles.addElement("* New Note *");
      // test titles
//      addNote("Todo #1", "* Cry", "");
//      addNote("Shopping", "* Eggs\n* Bread\n* 2 Children", "");
//      addNote("Product idea", "A portable monitor (A4 size) 1080p resolution.\nHas a plastic cover plate which has mousepad felt on the inside", "password");
      //------------
      titleList = new JList<>(titles);
      titleList.setFont(Constants.FONT);
      titleList.setFixedCellHeight(20);
      titleList.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            System.out.println(titleList.getSelectedIndex());
            if (e.getKeyCode() == VK_DELETE && titleList.getSelectedIndex() > 0) {
//               deleteNote();
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

      dbConnection = new DBConnection();

      jFrame.setMinimumSize(new Dimension(900, 500));
      jFrame.setPreferredSize(new Dimension(1000, 600));
      jFrame.setLocationRelativeTo(null);
      jFrame.setVisible(true);

//      UserData.lockedOutEvent = () -> {
//         about.setEnabled(false);
//         bodyPane.setEnabled(false);
//         deleteNote.setEnabled(false);
//         deleteUser.setEnabled(false);
//         indvPasswordBar.setEnabled(false);
//         keybindings.setEnabled(false);
//         newNote.setEnabled(false);
//         save.setEnabled(false);
//         saveAs.setEnabled(false);
//         titleBar.setEnabled(false);
//         userSettings.setEnabled(false);
//      };

//      if (titles.size() <= 1)
//         createNewNote();

//      showNote(1);

      new SwingWorker<>() {
         @Override
         protected Object doInBackground() {
            List<Note> toAdd = NoteIO.getAllFiles(UserData.FILEPATH);
            for (Note note : toAdd) {
               titles.addElement(note.title);
               notes.add(note);
            }
            return null;
         }
      }.execute();

//      NoteIO.list(InfoHolder.FILEPATH).forEach(System.out::println);
//      notes = NoteIO.getAllFiles(InfoHolder.FILEPATH);
   }

//   void showNote(int titleListIndex) {
//      if (InfoHolder.blockRequest) return;
//
//      if (strikes.get(titleListIndex - 1) == 3) {
//         InfoHolder.blockRequest = true;
//         return;
//      }
//
//      char[] password = notes.get(titleListIndex - 1).password;
//
//      if (password.length == 0) {
//         titleBar.setText(titles.getElementAt(titleListIndex));
//         bodyPane.setText(notes.get(--titleListIndex).body);
//         indvPasswordBar.setText(String.valueOf(notes.get(titleListIndex).password));
//         return;
//      }
//
//      int finalTitleListIndex = titleListIndex;
//      new SwingWorker<>() {
//         @Override
//         protected Object doInBackground() {
//            var npf = new NotePasswordFrame();
//            char[] notePassword = npf.run();
//            System.out.println(notePassword);
//            if (Arrays.equals(notePassword, password)) {
//               titleBar.setText(titles.getElementAt(finalTitleListIndex));
//               bodyPane.setText(notes.get(finalTitleListIndex - 1).body);
//               indvPasswordBar.setText(String.valueOf(notes.get(finalTitleListIndex - 1).password));
//               strikes.set(finalTitleListIndex - 1, 0);
//            } else {
//               strikes.set(finalTitleListIndex - 1, strikes.get(finalTitleListIndex - 1) + 1);
//            }
//            return null;
//         }
//      }.execute();
//
//      System.out.println(strikes.get(titleListIndex - 1));
//   }
//
//   void addNote(String title, String body, String password) {
//      titles.addElement(title);
//      notes.add(new Note(title, body, password));
//      strikes.add(0);
//   }
//
//   void createNewNote() {
//      if (InfoHolder.blockRequest) return;
//      addNote("[Untitled note]", "", "");
//
//      titleList.setSelectedIndex(notes.size());
//      showNote(notes.size());
//   }
//
//   void deleteNote() {
//      int index = titleList.getSelectedIndex() - 1;
//      if (index == -1) return;
//      titles.removeElementAt(index + 1);
//      notes.remove(index);
//      strikes.remove(index);
//
//      titleBar.setText("");
//      bodyPane.setText("");
//      indvPasswordBar.setText("");
//   }
//
//   void saveNote() {
//      int index = titleList.getSelectedIndex() - 1;
//      if (index == -1) return;
//      titles.setElementAt(titleBar.getText(), index + 1);
//      notes.set(index, new Note(titleBar.getText(), bodyPane.getText(), indvPasswordBar.getPassword(), notes.get(index).filepath));
//
//      // TODO: 29/08/2021 UPDATE FILE
//   }

   void showNote(int index) {
      String title = titles.getElementAt(index--);
      String body = notes.get(index).body;
      char[] password = notes.get(index).password;
      System.out.println(Arrays.toString(password));

      if (password == null) {
         titleBar.setText(title);
         bodyPane.setText(body);
         indvPasswordBar.setText("");
         return;
      }

      new EnterPasswordFrame(password, () -> {
         titleBar.setText(title);
         bodyPane.setText(body);
         indvPasswordBar.setText(new String(password));
      });
   }

   void saveNote() {

   }

   void createNote() {
      notes.add(new Note("[Untitled note]", "", (char[]) null, UserData.FILEPATH));
      titles.addElement("[Untitled note]");
   }

   void deleteNote() {

   }

   public void close() {
      if (jFrame != null) {
         jFrame.dispose();
         dbConnection.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object component = e.getSource();

      if (component == newNote) {
//         createNewNote();
      } else if (component == deleteNote) {
//         deleteNote();
      } else if (component == saveAs) {
         // -------------------------------------------- TODO: 28/09/2021 MASSIVE TODO ADD ACTUAL F***ING FILES/NOTES
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
         new TextDialog("About", aboutFile.toString());
      } else if (component == userSettings) {
         new EnterPasswordFrame(UserData.PASSWORD, () -> new ChangeUserSettingsFrame());
      } else if (component == save) {
//         saveNote();
      } else if (component == deleteUser) {
         new EnterPasswordFrame(UserData.PASSWORD, () -> new DeleteUserFrame());
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
