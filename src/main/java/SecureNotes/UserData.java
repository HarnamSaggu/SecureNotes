package SecureNotes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class UserData {
   static final List<CallbackEvent> callbackEvents = new ArrayList<>();
   static String username;
   static char[] password;
   static boolean blockRequest = false;
   static int strikes = 0;

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

//      new TextDialog("Locked out", "You have been locked out for " + Constants.TIMEOUT_DURATION + " mins");
   }
}
