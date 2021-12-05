package secure.notes;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class StartFrame extends JPanel implements ActionListener, KeyListener {
   JFrame jFrame;
   JButton loginButton;
   JButton newUserButton;
   JTextField usernameTextField;
   JPasswordField passwordTextField;
   JLabel messageLabel;
   DBConnection dbConnection;

   StartFrame() {
      super();
      initComponents();
   }

   public static void main(String[] args) {
      new StartFrame();
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

   void checkLoginDetails() {
      String currentDateTime = DateTime.getDateTime();
      if (currentDateTime == null) {
         return;
      }

      String username = usernameTextField.getText();
      char[] password = passwordTextField.getPassword();

      if (password.length == 0 || password.length > 255) {
         setMessage("Enter your login details");
         return;
      }

      try {
         ResultSet resultSet = dbConnection.selectPassword(username);
         if (!resultSet.next()) {
            setMessage("Invalid username/password");
            return;
         }

         String returnedPassword = resultSet.getString("hashed_password");
         if (!Crypt.hashMatches(new String(password), returnedPassword)) {
            setMessage("Invalid username/password");
            UserData.incrementStrikes();
            return;
         }

         resultSet = dbConnection.selectTimeout(username);
         resultSet.next();
         String timeout = resultSet.getString("timeout");
         if (timeout != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localTimeout = LocalDateTime.parse(timeout, formatter);
            LocalDateTime localCurrent = LocalDateTime.parse(currentDateTime, formatter);
            long minutesSince = ChronoUnit.MINUTES.between(localTimeout, localCurrent);
            if (minutesSince >= Constants.TIMEOUT_DURATION) {
               dbConnection.nullifyTimeout(username);
            } else {
               setMessage("You have been locked out: " + (45L - minutesSince) + " min(s)");
               return;
            }
         }
         UserData.setUsername(username);
         UserData.setPassword(password);
         close();
         new SecureNotes();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   void setMessage(String msg) {
      passwordTextField.setText("");

      if (messageLabel == null) {
         messageLabel = new JLabel();
         messageLabel.setFont(Constants.FONT);
         messageLabel.setBounds(10, 51, 564, 18);
         add(messageLabel);
         loginButton.setBounds(10, 71, 564, 18);
         newUserButton.setBounds(10, 91, 564, 18);
         jFrame.setSize(600, 155);
      }

      messageLabel.setText(msg);
   }

   void close() {
      jFrame.dispose();
      dbConnection.close();
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

   @Override
   public void keyTyped(KeyEvent e) {
      // Unused listener method
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         checkLoginDetails();
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      // Unused listener method
   }
}
