package secure.notes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

class DeleteUserFrame extends JPanel implements ActionListener {
   final CallbackEvent callbackEvent;
   JFrame jFrame;
   JLabel messageLabel;
   JButton yesButton;
   JButton noButton;

   DBConnection dbConnection;

   DeleteUserFrame(CallbackEvent callbackEvent) {
      super();
      this.callbackEvent = callbackEvent;
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
      jFrame.setIconImage(Constants.ICON);
      jFrame.setResizable(false);
      jFrame.setSize(508, 345);
      jFrame.setLocationRelativeTo(null);

      setLayout(null);
      jFrame.add(this);

      messageLabel = new JLabel("<html>Are you sure you would like to delete your account?<br>This cannot be undone</html>");
      messageLabel.setFont(Constants.BOLD_FONT);
      messageLabel.setBounds(20, 10, 470, 40);
      add(messageLabel);

      yesButton = new JButton("Yes");
      yesButton.setFont(Constants.FONT);
      yesButton.setBackground(Constants.BUTTON_COLOR);
      yesButton.setBounds(10, 280, 230, 20);
      yesButton.addActionListener(this);
      add(yesButton);

      noButton = new JButton("No");
      noButton.setFont(Constants.FONT);
      noButton.setBackground(Constants.BUTTON_COLOR);
      noButton.setBounds(248, 280, 230, 20);
      noButton.addActionListener(this);
      add(noButton);

      dbConnection = new DBConnection();

      jFrame.setVisible(true);
   }

   void close() {
      if (jFrame != null) {
         jFrame.dispose();
         dbConnection.close();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == yesButton) {
         if (UserData.isBlocked()) {
            new TextDialog("Secure notes", "Either your account has already been deleted or you are locked out");
            callbackEvent.doEvent();
         } else {
            try {
               dbConnection.deleteUser(UserData.getUsername());
               UserData.block();
               callbackEvent.doEvent();
               close();
            } catch (SQLException ex) {
               ex.printStackTrace();
            }
         }
      } else if (e.getSource() == noButton) {
         close();
      }
   }
}
