package SecureNotes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class Constants {
   static final String RESOURCE_PATH = "src/main/resources/";
   static final Image ICON = new ImageIcon(RESOURCE_PATH + "icon1.png").getImage();
   static final Color BUTTON_COLOR = new Color(255, 255, 255);
   static final Font FONT = new Font("Fira Code Regular", Font.PLAIN, 12);
   static final Font BOLD_FONT = new Font("Fira Code Regular", Font.BOLD, 13);
   static final long TIMEOUT_DURATION = 45L;

   static {
      try {
         String[] fontsToLoad = {
                 "FiraCode-Regular"
         };
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         for (String fontToLoad : fontsToLoad) {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(RESOURCE_PATH + fontToLoad + ".ttf")));
         }
      } catch (IOException | FontFormatException e) {
         e.printStackTrace();
      }
   }
}
