package SecureNotes;

import java.sql.SQLException;

public class UserData {
   public static String USERNAME;
   public static char[] PASSWORD;
   static String FILEPATH;

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
         dbConnection.updateTimeout(DateTime.getDateTime(), USERNAME);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      dbConnection.close();

      USERNAME = null;
      PASSWORD = null;
      FILEPATH = null;
      blockRequest = true;

      new TextDialog("Locked out", "You have been locked out for " + Constants.TIMEOUT_DURATION + " mins");
   }
}
