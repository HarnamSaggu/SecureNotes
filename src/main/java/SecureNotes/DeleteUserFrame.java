package SecureNotes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

class DeleteUserFrame extends JPanel implements ActionListener {
   final CallbackEvent callbackEvent;
   JFrame jFrame;
   JLabel msg;
   JButton yes, no;
   DBConnection dbConnection;

   DeleteUserFrame(CallbackEvent callbackEvent) {
      super();
      this.callbackEvent = callbackEvent;
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
//      jFrame.setResizable(false);
      jFrame.setSize(508, 345);
      jFrame.setLocationRelativeTo(null);

      setLayout(null);
      jFrame.add(this);

      msg = new JLabel("<html>Are you sure you would like to delete your account?<br>This cannot be undone</html>");
      msg.setFont(Constants.BOLD_FONT);
      msg.setBounds(20, 10, 470, 40);
      add(msg);

      yes = new JButton("Yes");
      yes.setFont(Constants.FONT);
      yes.setBackground(Constants.BUTTON_COLOR);
      yes.setBounds(10, 280, 230, 20);
      yes.addActionListener(this);
      add(yes);

      no = new JButton("No");
      no.setFont(Constants.FONT);
      no.setBackground(Constants.BUTTON_COLOR);
      no.setBounds(248, 280, 230, 20);
      no.addActionListener(this);
      add(no);

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
      if (e.getSource() == yes) {
         if (UserData.blockRequest) {
            new TextDialog("Secure notes", "Either your account has already been deleted or you are locked out");
            callbackEvent.doEvent();
         } else {
            try {
               dbConnection.deleteUser(UserData.username);
               UserData.block();
               callbackEvent.doEvent();
               close();
            } catch (SQLException ex) {
               ex.printStackTrace();
            }
         }
      } else if (e.getSource() == no) {
         close();
      }
   }
}
