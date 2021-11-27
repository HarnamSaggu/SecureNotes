package SecureNotes;

import java.sql.ResultSet;
import java.sql.SQLException;

class ReEnterPasswordFrame extends EnterPasswordFrame {
   final String choice;
   int count;

   public ReEnterPasswordFrame(String choice) {
      super();
      dbConnection = new DBConnection();
      this.choice = choice;
      jFrame.setVisible(true);
   }

   @Override
   void checkDetails() {
      char[] passwordStr = password.getPassword();
      if (passwordStr.length == 0 || passwordStr.length >= 256) {
         setErrorMsg("Enter your login details");
         return;
      }
      try {
         ResultSet resultSet = dbConnection.select(InfoHolder.USERNAME);
         resultSet.next();
         String returnedPassword = resultSet.getString("hashed_password");
         if (Crypt.hashMatches(new String(passwordStr), returnedPassword)) {
            System.out.println("uwu");
            close();
            switch (choice) {
               case "userSettings" -> new ChangeUserSettingsFrame();
               case "deleteUser" -> new DeleteUserFrame();
            }
         } else {
            count++;
            if (count == 3) {
               dbConnection.updateTimeout(DateTime.getDateTime(), InfoHolder.USERNAME);
               InfoHolder.blockRequest = true;
               InfoHolder.USERNAME = null;
               InfoHolder.PASSWORD = null;
               InfoHolder.FILEPATH = null;
            }
            setErrorMsg("Invalid password");
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }
}
