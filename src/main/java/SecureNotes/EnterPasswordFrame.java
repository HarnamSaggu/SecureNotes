package SecureNotes;

import javax.swing.*;
import java.awt.event.*;

public class EnterPasswordFrame extends JPanel implements ActionListener, KeyListener {
   JFrame jFrame;
   JButton login;
   JPasswordField password;
   JLabel errorMessage;

   DBConnection dbConnection;

   public EnterPasswordFrame() {
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
      jFrame.setSize(600, 95);
      jFrame.setLocationRelativeTo(null);

      setLayout(null);
      jFrame.add(this);

      JLabel l1 = new JLabel("Password");
      l1.setFont(Constants.FONT);
      l1.setBounds(10, 10, 150, 20);
      add(l1);

      password = new JPasswordField();
      password.setFont(Constants.FONT);
      password.setBounds(80, 11, 495, 18);
      password.addKeyListener(this);
      add(password);

      login = new JButton("Login");
      login.setFont(Constants.FONT);
      login.setBounds(10, 31, 564, 18);
      login.setBackground(Constants.BUTTON_COLOR);
      login.addActionListener(this);
      login.addKeyListener(this);
      add(login);

//      dbConnection = new DBConnection();
   }

   public void close() {
      if (jFrame != null) {
         jFrame.dispose();
      }
      if (dbConnection != null) {
         dbConnection.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      checkDetails();
   }

   void checkDetails() {

   }

   void setErrorMsg(String msg) {
      password.setText("");

      if (errorMessage == null) {
         errorMessage = new JLabel();
         errorMessage.setFont(Constants.FONT);
         errorMessage.setBounds(10, 31, 564, 18);
         add(errorMessage);
         login.setBounds(10, 51, 564, 18);
         jFrame.setSize(600, 115);
      }

      errorMessage.setText(msg);
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
