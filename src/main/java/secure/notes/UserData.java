package secure.notes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class UserData {
   private static final List<CallbackEvent> callbackEvents = new ArrayList<>();
   private static String username;
   private static char[] password;
   private static boolean blockRequest = false;
   private static int strikes = 0;

   private UserData() {
      throw new IllegalStateException("Utility class");
   }

   static void addCallbackEvent(CallbackEvent callbackEvent) {
      callbackEvents.add(callbackEvent);
   }

   static void removeCallbackEvent() {
      callbackEvents.remove(0);
   }

   static void incrementStrikes() {
      strikes++;

      if (strikes == Constants.MAX_ATTEMPT) {
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

      for (CallbackEvent callbackEvent : callbackEvents) {
         callbackEvent.doEvent();
      }
   }

   public static synchronized String getUsername() {
      return username;
   }

   public static synchronized void setUsername(String username) {
      UserData.username = username;
   }

   public static synchronized char[] getPassword() {
      return password;
   }

   public static synchronized void setPassword(char[] password) {
      UserData.password = password;
   }

   public static synchronized boolean isBlocked() {
      return blockRequest;
   }
}
