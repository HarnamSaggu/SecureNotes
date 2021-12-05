package secure.notes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

class CreateUserFrame extends JPanel implements ActionListener, KeyListener {
   JFrame jFrame;
   JButton newUserButton;
   JTextField usernameTextField;
   JPasswordField passwordTextField;
   JPasswordField passwordMatchTextField;
   JLabel messageLabel;

   DBConnection dbConnection;

   CreateUserFrame() {
      super();
      initComponents();
   }

   void initComponents() {
      jFrame = new JFrame("Secure notes");
      jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      jFrame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            close();
         }
      });
      jFrame.setLayout(new BorderLayout());
      jFrame.setIconImage(Constants.ICON);
      jFrame.setResizable(false);
      jFrame.setSize(600, 150);
      jFrame.setLocationRelativeTo(null);

      setLayout(null);
      jFrame.add(this, BorderLayout.CENTER);

      JLabel l1 = new JLabel("Username");
      l1.setFont(Constants.FONT);
      l1.setBounds(10, 10, 150, 20);
      add(l1);

      JLabel l2 = new JLabel("Password");
      l2.setFont(Constants.FONT);
      l2.setBounds(10, 30, 150, 20);
      add(l2);

      JLabel l3 = new JLabel("<html>Re-enter<br>Password</html>");
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

      newUserButton = new JButton("Create new user");
      newUserButton.setFont(Constants.FONT);
      newUserButton.setBounds(10, 84, 564, 18);
      newUserButton.setBackground(Constants.BUTTON_COLOR);
      newUserButton.addActionListener(this);
      newUserButton.addKeyListener(this);
      add(newUserButton);

      dbConnection = new DBConnection();

      jFrame.setVisible(true);
   }

   void createNewUser() {
      String usernameStr = usernameTextField.getText();
      char[] passwordStr = passwordTextField.getPassword();
      char[] passwordMatchStr = passwordMatchTextField.getPassword();

      if (usernameStr.length() == 0 || usernameStr.length() > 255) {
         setMessage("Username must be between 1 and 255 characters");
         return;
      }
      if (passwordStr.length == 0 || passwordStr.length > 255) {
         setMessage("Password must be between 1 and 255 characters");
         return;
      }
      if (!Arrays.equals(passwordMatchStr, passwordStr)) {
         setMessage("Passwords don't match");
         return;
      }

      try {
         ResultSet resultSet = dbConnection.selectPassword(usernameStr);
         if (!resultSet.next()) {
            dbConnection.insertUser(usernameStr, Crypt.hash(new String(passwordStr)));
            close();
            UserData.setUsername(usernameStr);
            UserData.setPassword(passwordStr);
            new SecureNotes();
         } else {
            setMessage("Username taken");
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   void setMessage(String msg) {
      if (messageLabel == null) {
         jFrame.setSize(600, 166);
         messageLabel = new JLabel();
         messageLabel.setFont(Constants.FONT);
         messageLabel.setBounds(10, 81, 564, 18);
         add(messageLabel);
         newUserButton.setBounds(10, 100, 564, 18);
      }

      passwordTextField.setText("");
      passwordMatchTextField.setText("");
      messageLabel.setText(msg);
   }

   void close() {
      if (jFrame != null) {
         jFrame.dispose();
         dbConnection.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      createNewUser();
   }

   @Override
   public void keyTyped(KeyEvent e) {
      // Unused listener method
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         createNewUser();
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      // Unused listener method
   }
}
