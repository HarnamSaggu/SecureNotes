package secure.notes;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

class EnterPasswordFrame extends JPanel implements ActionListener, KeyListener {
   final char[] password;
   final CallbackEvent event;
   JFrame jFrame;
   JButton loginButton;
   JPasswordField passwordField;

   EnterPasswordFrame(char[] password, CallbackEvent event) {
      super();
      this.password = password;
      this.event = event;
      initComponents();
      UserData.addCallbackEvent(() -> new TextDialog("Locked out", "You have been locked out for " + Constants.TIMEOUT_DURATION + " mins"));
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
      jFrame.setSize(600, 95);
      jFrame.setLocationRelativeTo(null);

      setLayout(null);
      jFrame.add(this);

      JLabel l1 = new JLabel("Password");
      l1.setFont(Constants.FONT);
      l1.setBounds(10, 10, 150, 20);
      add(l1);

      passwordField = new JPasswordField();
      passwordField.setFont(Constants.FONT);
      passwordField.setBounds(80, 11, 495, 18);
      passwordField.addKeyListener(this);
      add(passwordField);

      loginButton = new JButton("Login");
      loginButton.setFont(Constants.FONT);
      loginButton.setBounds(10, 31, 564, 18);
      loginButton.setBackground(Constants.BUTTON_COLOR);
      loginButton.addActionListener(this);
      loginButton.addKeyListener(this);
      add(loginButton);

      jFrame.setVisible(true);
   }

   void close() {
      if (jFrame != null) {
         jFrame.dispose();
         UserData.removeCallbackEvent();
      }
   }

   void checkDetails() {
      if (DateTime.getDateTime() == null) {
         return;
      }

      if (Arrays.equals(password, passwordField.getPassword())) {
         event.doEvent();
      } else {
         UserData.incrementStrikes();
      }
      close();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      checkDetails();
   }

   @Override
   public void keyTyped(KeyEvent e) {
      // Unused listener method
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         checkDetails();
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      // Unused listener method
   }
}
