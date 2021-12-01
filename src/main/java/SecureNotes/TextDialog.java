package SecureNotes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class TextDialog extends JPanel implements KeyListener {
   JFrame jFrame;

   String title;
   String text;

   TextDialog(String title, String text) {
      super();
      this.title = title;
      this.text = text;
      initComponents();
   }

   void initComponents() {
      jFrame = new JFrame(title);
      jFrame.setIconImage(Constants.ICON);
      jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      jFrame.addKeyListener(this);

      setLayout(new BorderLayout(5, 5));
      setBorder(new EmptyBorder(10, 10, 10, 10));
      addKeyListener(this);
      jFrame.add(this);

      JLabel l1 = new JLabel(text);
      l1.setFont(Constants.FONT);
      l1.addKeyListener(this);
      add(l1, BorderLayout.CENTER);

      JButton ok = new JButton("Ok");
      ok.setFont(Constants.FONT);
      ok.setBackground(Constants.BUTTON_COLOR);
      ok.addActionListener(e -> jFrame.dispose());
      ok.addKeyListener(this);
      add(ok, BorderLayout.SOUTH);

      jFrame.pack();
      jFrame.setMinimumSize(jFrame.getSize());
      jFrame.setLocationRelativeTo(null);
      jFrame.setVisible(true);
   }

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         jFrame.dispose();
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {

   }
}
