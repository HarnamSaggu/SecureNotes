package SecureNotes;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class StartFrame extends JPanel implements ActionListener, KeyListener {
   JFrame jFrame;
   JButton loginButton, newUserButton;
   JTextField usernameTextField;
   JPasswordField passwordTextField;
   JLabel errorMessageLabel;

   DBConnection dbConnection;

   int failCount;

   public StartFrame() {
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

      jFrame.setIconImage(Constants.ICON);
      jFrame.setResizable(false);
      jFrame.setSize(600, 135);
      jFrame.setLocationRelativeTo(null);

      setLayout(null);
      jFrame.add(this);

      JLabel l1 = new JLabel("Username");
      l1.setFont(Constants.FONT);
      l1.setBounds(10, 10, 150, 20);
      add(l1);

      JLabel l2 = new JLabel("Password");
      l2.setFont(Constants.FONT);
      l2.setBounds(10, 30, 150, 20);
      add(l2);

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

      loginButton = new JButton("Login");
      loginButton.setFont(Constants.FONT);
      loginButton.setBounds(10, 51, 564, 18);
      loginButton.setBackground(Constants.BUTTON_COLOR);
      loginButton.addActionListener(this);
      add(loginButton);

      newUserButton = new JButton("Create new user");
      newUserButton.setFont(Constants.FONT);
      newUserButton.setBounds(10, 71, 564, 18);
      newUserButton.setBackground(Constants.BUTTON_COLOR);
      newUserButton.addActionListener(this);
      newUserButton.addKeyListener(this);
      add(newUserButton);

      dbConnection = new DBConnection();

      jFrame.setVisible(true);
   }

   public void close() {
      if (jFrame != null) {
         jFrame.dispose();
         dbConnection.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == loginButton) {
         checkLoginDetails();
      } else if (e.getSource() == newUserButton) {
         close();
         new CreateUserFrame();
      }
   }

   void checkLoginDetails() {
      String username = usernameTextField.getText();
      System.out.println(username);
      char[] password = passwordTextField.getPassword();
      System.out.println(password);
      if (password.length == 0 || password.length >= 256) {
         setErrorMsg("Enter your login details");
         return;
      }
      try {
         ResultSet resultSet = dbConnection.selectUser(username);
         if (resultSet.next()) {
            String returnedPassword = resultSet.getString("hashed_password");
            if (Crypt.hashMatches(new String(password), returnedPassword)) {
               resultSet = dbConnection.selectTimeout(username);
               if (resultSet.next()) {
                  String timeout = resultSet.getString("timeout");
                  System.out.println(timeout);

                  if (timeout == null) {
                     UserData.username = username;
                     UserData.password = password;
                        close();
                        new SecureNotes();
                  } else {
                     String currentDateTime = DateTime.getDateTime();
                     System.out.println(currentDateTime);

                     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                     LocalDateTime localTimeout = LocalDateTime.parse(timeout, formatter);
                     assert currentDateTime != null;
                     LocalDateTime localCurrent = LocalDateTime.parse(currentDateTime, formatter);

                     System.out.println(localTimeout);
                     System.out.println(localCurrent);

//                     Duration duration = Duration.between(localTimeout, localCurrent);
//                     long minutesSince = duration.toMinutes();

//                     long minutesSince = localCurrent.getMinute() - localTimeout.getMinute();

                     long minutesSince = ChronoUnit.MINUTES.between(localTimeout, localCurrent);

                     System.out.println(minutesSince);

                     if (minutesSince >= Constants.TIMEOUT_DURATION) {
                        dbConnection.nullifyTimeout(username);

                        UserData.username = username;
                        UserData.password = password;
                        close();
                        new SecureNotes();
                     } else {
                        setErrorMsg("You have been locked out: " + (45L - minutesSince) + " min(s)");
                     }
                  }
               }
            } else {
               failCount++;
               setErrorMsg("Invalid username/password");
            }
         } else {
            setErrorMsg("Invalid username/password");
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   void setErrorMsg(String msg) {
      passwordTextField.setText("");

      if (errorMessageLabel == null) {
         errorMessageLabel = new JLabel();
         errorMessageLabel.setFont(Constants.FONT);
         errorMessageLabel.setBounds(10, 51, 564, 18);
         add(errorMessageLabel);
         loginButton.setBounds(10, 71, 564, 18);
         newUserButton.setBounds(10, 91, 564, 18);
         jFrame.setSize(600, 155);
      }

      errorMessageLabel.setText(msg);

      if (failCount == 3) {
         try {
            dbConnection.updateTimeout(DateTime.getDateTime(), UserData.username);
         } catch (SQLException e) {
            e.printStackTrace();
         }
         UserData.blockRequest = true;
         UserData.username = null;
         UserData.password = null;

         errorMessageLabel.setText("You have been locked out for " + Constants.TIMEOUT_DURATION + " mins");
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         checkLoginDetails();
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {

   }
}
