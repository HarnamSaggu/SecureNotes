package SecureNotes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TextDialog extends JPanel {
   public TextDialog(String title, String text) {
      super();

      JFrame jFrame = new JFrame(title);
      jFrame.setIconImage(Constants.ICON);
      jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      setLayout(new BorderLayout(5, 5));
      setBorder(new EmptyBorder(10, 10, 10, 10));
      jFrame.add(this);

      JLabel l1 = new JLabel(text);
      l1.setFont(Constants.FONT);
      add(l1, BorderLayout.CENTER);

      JButton ok = new JButton("Ok");
      ok.setFont(Constants.FONT);
      ok.setBackground(Constants.BUTTON_COLOR);
      ok.addActionListener(e -> jFrame.dispose());
      add(ok, BorderLayout.SOUTH);

      jFrame.pack();
      jFrame.setMinimumSize(jFrame.getSize());
      jFrame.setLocationRelativeTo(null);
      jFrame.setVisible(true);
   }
}
