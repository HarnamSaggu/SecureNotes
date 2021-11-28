package SecureNotes;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class EnterPasswordFrame extends JPanel implements ActionListener, KeyListener {
   JFrame jFrame;
   JButton loginButton;
   JPasswordField passwordField;

   final char[] password;
   final PasswordEvent event;

   public EnterPasswordFrame(char[] password, PasswordEvent event) {
      super();
      this.password = password;
      this.event = event;
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

   public void close() {
      if (jFrame != null) {
         jFrame.dispose();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      checkDetails();
   }

   void checkDetails() {
      if (Arrays.equals(password, passwordField.getPassword())) {
         event.ifCorrect();
      } else {
         UserData.incrementStrikes();
      }
      close();
   }

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         checkDetails();
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {

   }
}
