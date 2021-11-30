package SecureNotes;

import java.sql.SQLException;

public class UserData {
   static String username;
   static char[] password;
   static boolean blockRequest = false;
   static int strikes = 0;

   static void incrementStrikes() {
      strikes++;

      if (strikes == 3) {
         block();
      }
   }

   static void block() {
      DBConnection dbConnection = new DBConnection();
      try {
         dbConnection.updateTimeout(DateTime.getDateTime(), username);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      dbConnection.close();

      username = null;
      password = null;
      blockRequest = true;

      new TextDialog("Locked out", "You have been locked out for " + Constants.TIMEOUT_DURATION + " mins");
   }
}
