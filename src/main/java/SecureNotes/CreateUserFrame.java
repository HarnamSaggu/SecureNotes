package SecureNotes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

class CreateUserFrame extends JPanel implements ActionListener, KeyListener {
   JFrame jFrame;
   JButton newUser;
   JTextField username;
   JPasswordField password, passwordMatch;
   JLabel errorMessage, l1, l2, l3;

   DBConnection dbConnection;

   CreateUserFrame() {
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
      jFrame.setSize(600, 150);
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

      newUser = new JButton("Create new user");
      newUser.setFont(Constants.FONT);
      newUser.setBounds(10, 84, 564, 18);
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

               try {
                  ResultSet resultSet = dbConnection.selectUser(usernameStr);
                  if (!resultSet.next()) {
//                     System.out.println(usernameStr);
//                     System.out.println(passwordStr);
//                     System.out.println(passwordMatchStr);
//                     System.out.println();

                     try {
                        dbConnection.insertUser(usernameStr, Crypt.hash(new String(passwordStr)));
                        close();
                        UserData.username = usernameStr;
                        UserData.password = passwordStr;
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
               setErrorMsg("Passwords don't match", true);
            }
         } else {
            setErrorMsg("Password must be between 1 and 255 characters", true);
         }
      } else {
         setErrorMsg("Username must be between 1 and 255 characters", true);
      }
   }

   void setErrorMsg(String msg, boolean reset) {
      if (reset) {
         password.setText("");
         passwordMatch.setText("");
      }

      if (errorMessage == null) {
         jFrame.setSize(600, 166);
         errorMessage = new JLabel();
         errorMessage.setFont(Constants.FONT);
         errorMessage.setBounds(10, 81, 564, 18);
         add(errorMessage);
         newUser.setBounds(10, 100, 564, 18);
      }

      errorMessage.setText(msg);
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
