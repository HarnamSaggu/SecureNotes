package SecureNotes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

class ChangeUserSettingsFrame extends JPanel implements ActionListener, KeyListener {
   JFrame jFrame;
   JButton changeSettingsButton, choosePathButton;
   JTextField usernameTextField, pathTextField;
   JPasswordField passwordTextField, passwordMatchTextField;
   JLabel messageLabel, l1, l2, l3, l4;
   JFileChooser fileChooser;

   DBConnection dbConnection;

   ChangeUserSettingsFrame() {
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
            dbConnection.close();
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

      usernameTextField = new JTextField();
      usernameTextField.setFont(Constants.FONT);
      usernameTextField.setBounds(80, 11, 495, 18);
      usernameTextField.addKeyListener(this);
      add(usernameTextField);

      passwordTextField = new JPasswordField();
      passwordTextField.setFont(Constants.FONT);
      passwordTextField.setBounds(80, 31, 495, 18);
      passwordTextField.addKeyListener(this);
      add(passwordTextField);

      passwordMatchTextField = new JPasswordField();
      passwordMatchTextField.setFont(Constants.FONT);
      passwordMatchTextField.setBounds(80, 51, 495, 18);
      passwordMatchTextField.addKeyListener(this);
      add(passwordMatchTextField);

      l4 = new JLabel("<html>Note<br>Folder</html>");
      l4.setFont(Constants.FONT);
      l4.setBounds(10, 83, 150, 30);
      add(l4);

      pathTextField = new JTextField("C:\\");
      pathTextField.setFont(Constants.FONT);
      pathTextField.setBounds(80, 81, 460, 18);
      pathTextField.addKeyListener(this);
      add(pathTextField);

      choosePathButton = new JButton("...");
      choosePathButton.setFont(Constants.FONT);
      choosePathButton.setBounds(545, 81, 30, 18);
      choosePathButton.setBackground(Constants.BUTTON_COLOR);
      choosePathButton.addActionListener(this);
      add(choosePathButton);

      changeSettingsButton = new JButton("Change settings");
      changeSettingsButton.setFont(Constants.FONT);
      changeSettingsButton.setBounds(10, 116, 564, 18);
      changeSettingsButton.setBackground(Constants.BUTTON_COLOR);
      changeSettingsButton.addActionListener(this);
      changeSettingsButton.addKeyListener(this);
      add(changeSettingsButton);

      dbConnection = new DBConnection();

      jFrame.setVisible(true);
   }

   void changeUserSettings() {
      String usernameStr = usernameTextField.getText();
      char[] passwordStr = passwordTextField.getPassword();
      char[] passwordMatchStr = passwordMatchTextField.getPassword();

      if (usernameStr.length() > 0 && usernameStr.length() < 256) {
         if (passwordStr.length > 0 && passwordStr.length < 256) {
            if (Arrays.equals(passwordStr, passwordMatchStr)) {
               File folder = new File(pathTextField.getText());
               if (folder.exists() && folder.isDirectory()) {
                  try {
                     ResultSet resultSet = dbConnection.selectUser(usernameStr);
                     System.out.println(UserData.username + ", " + usernameStr + ", " + UserData.username.equals(usernameStr));
                     if (usernameStr.equals(UserData.username)) resultSet.next();
                     if (!resultSet.next()) {
                        System.out.println(usernameStr);
                        System.out.println(passwordStr);
                        System.out.println(pathTextField.getText());
                        System.out.println();

                        try {
                           dbConnection.updateUser(usernameStr, Crypt.hash(new String(passwordStr)), pathTextField.getText(), UserData.username);
                           UserData.username = usernameStr;
                           UserData.password = passwordStr;
                           setMessage("Settings changed successfully");
                        } catch (SQLException e) {
                           e.printStackTrace();
                        }
                     } else {
                        setMessage("Username taken");
                     }
                  } catch (SQLException e) {
                     e.printStackTrace();
                  }
               } else {
                  setMessage("Note folder location is invalid");
               }
            } else {
               setMessage("Passwords don't match");
            }
         } else {
            setMessage("Password must be between 1 and 255 characters");
         }
      } else {
         setMessage("Username must be between 1 and 255 characters");
      }
   }

   void setMessage(String msg) {
      if (messageLabel == null) {
         jFrame.setSize(600, 200);
         messageLabel = new JLabel();
         messageLabel.setFont(Constants.FONT);
         messageLabel.setBounds(10, 116, 564, 18);
         add(messageLabel);
         changeSettingsButton.setBounds(10, 136, 564, 18);
      }

      messageLabel.setText(msg);
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
         pathTextField.setText(pathStr);
      }
   }

   public void close() {
      if (jFrame != null) {
         jFrame.dispose();
         dbConnection.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == changeSettingsButton) {
         changeUserSettings();
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
         changeUserSettings();
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {

   }
}
