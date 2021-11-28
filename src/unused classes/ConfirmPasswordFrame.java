package SecureNotes;

import java.util.Arrays;

class ConfirmPasswordFrame extends EnterPasswordFrame {
   char[] password;
   PasswordEvent event;

   ConfirmPasswordFrame(char[] password, PasswordEvent event) {
      super();
      this.password = password;
      this.event = event;
      jFrame.setVisible(true);
   }

   @Override
   void checkDetails() {
      if (Arrays.equals(password, super.passwordField.getPassword())) {
         event.ifCorrect();
      } else {
         UserData.incrementStrikes();
      }
      close();
   }
}
