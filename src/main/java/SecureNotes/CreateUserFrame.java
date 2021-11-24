package SecureNotes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CreateUserFrame extends JPanel implements ActionListener, KeyListener {
   JFrame jFrame;
   JButton newUser, choosePath;
   JTextField username, path;
   JPasswordField password, passwordMatch;
   JLabel errorMessage, l1, l2, l3, l4;
   JFileChooser fileChooser;

   DBConnection dbConnection;

   public CreateUserFrame() {
      super();
      initComponents();
   }

   void initComponents() {
      jFrame = new JFrame("Secure notes");

      jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      jFrame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            close();
         }
      });

      jFrame.setLayout(new BorderLayout());
      jFrame.setIconImage(Constants.ICON);
      jFrame.setResizable(false);
      jFrame.setSize(600, 180);
      jFrame.setLocationRelativeTo(null);

      setLayout(null);
      jFrame.add(this, BorderLayout.CENTER);

      l1 = new JLabel("Username");
      l1.setFont(Constants.FONT);
      l1.setBounds(10, 10, 150, 20);
      add(l1);

      l2 = new JLabel("Password");
      l2.setFont(Constants.FONT);
      l2.setBounds(10, 30, 150, 20);
      add(l2);

      l3 = new JLabel("<html>Re-enter<br>Password</html>");
      l3.setFont(Constants.FONT);
      l3.setBounds(10, 50, 150, 30);
      add(l3);

      username = new JTextField();
      username.setFont(Constants.FONT);
      username.setBounds(80, 11, 495, 18);
      username.addKeyListener(this);
      add(username);

      password = new JPasswordField();
      password.setFont(Constants.FONT);
      password.setBounds(80, 31, 495, 18);
      password.addKeyListener(this);
      add(password);

      passwordMatch = new JPasswordField();
      passwordMatch.setFont(Constants.FONT);
      passwordMatch.setBounds(80, 51, 495, 18);
      passwordMatch.addKeyListener(this);
      add(passwordMatch);

      l4 = new JLabel("<html>Note<br>Folder</html>");
      l4.setFont(Constants.FONT);
      l4.setBounds(10, 83, 150, 30);
      add(l4);

      path = new JTextField("C:\\");
      path.setFont(Constants.FONT);
      path.setBounds(80, 81, 460, 18);
      path.addKeyListener(this);
      add(path);

      choosePath = new JButton("...");
      choosePath.setFont(Constants.FONT);
      choosePath.setBounds(545, 81, 30, 18);
      choosePath.setBackground(Constants.BUTTON_COLOR);
      choosePath.addActionListener(this);
      add(choosePath);

      newUser = new JButton("Create new user");
      newUser.setFont(Constants.FONT);
      newUser.setBounds(10, 116, 564, 18);
      newUser.setBackground(Constants.BUTTON_COLOR);
      newUser.addActionListener(this);
      newUser.addKeyListener(this);
      add(newUser);

      dbConnection = new DBConnection();

      jFrame.setVisible(true);
   }

   void createNewUser() {
      String usernameStr = username.getText();
      char[] passwordStr = password.getPassword();
      char[] passwordMatchStr = passwordMatch.getPassword();
      if (usernameStr.length() > 0 && usernameStr.length() < 256) {
         if (passwordStr.length > 0 && passwordStr.length < 256) {
               if (Arrays.equals(passwordMatchStr, passwordStr)) {
                  File folder = new File(path.getText());
                  if (folder.exists() && folder.isDirectory()) {
                     try {
                        ResultSet resultSet = dbConnection.select(usernameStr);
                        if (!resultSet.next()) {
                           System.out.println(usernameStr);
                           System.out.println(passwordStr);
                           System.out.println(passwordMatchStr);
                           System.out.println(path.getText());
                           System.out.println();

                           try {
                              dbConnection.insert(usernameStr, Crypt.hash(new String(passwordStr)), path.getText());
                              close();
                              InfoHolder.USERNAME = usernameStr;
                              InfoHolder.PASSWORD = passwordStr;
                              new SecureNotes();
                           } catch (SQLException e) {
                              e.printStackTrace();
                           }
                        } else {
                           setErrorMsg("Username taken", false);
                        }
                     } catch (SQLException e) {
                        e.printStackTrace();
                     }
                  } else {
                     setErrorMsg("Note folder location is invalid", false);
                  }
               } else {
                  setErrorMsg("Passwords don't match", true);
               }
         } else {
            setErrorMsg("Password must be between 1 and 255 characters", true);
         }
      } else {
         setErrorMsg("Username must be between 1 and 255 characters", true);
      }
   }

   void choosePath() {
      fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(new java.io.File("C:/"));
      fileChooser.setDialogTitle("Choose notes folder");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fileChooser.setAcceptAllFileFilterUsed(false);
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
         String pathStr = fileChooser.getSelectedFile().getPath();
         if (!(pathStr.endsWith("\\") || pathStr.endsWith("/"))) {
            if (pathStr.contains("\\")) pathStr += "\\";
            else if (pathStr.contains("/")) pathStr += "/";
         }
         path.setText(pathStr);
      }
   }

   void setErrorMsg(String msg, boolean reset) {
      if (reset) {
         password.setText("");
         passwordMatch.setText("");
      }

      if (errorMessage == null) {
         jFrame.setSize(600, 200);
         errorMessage = new JLabel();
         errorMessage.setFont(Constants.FONT);
         errorMessage.setBounds(10, 116, 564, 18);
         add(errorMessage);
         newUser.setBounds(10, 136, 564, 18);
      }

      errorMessage.setText(msg);
   }

   public void close() {
      if (jFrame != null) {
         jFrame.dispose();
         dbConnection.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == newUser) {
         createNewUser();
      } else {
         choosePath();
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         createNewUser();
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {

   }
}
