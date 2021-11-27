package SecureNotes;

//class NotePasswordFrame extends EnterPasswordFrame {
//   volatile char[] notePassword;
//
//   NotePasswordFrame() {
//      super();
//   }
//
//   char[] run() {
//      jFrame.setVisible(true);
//      while (notePassword == null) {
//         Thread.onSpinWait();
//      }
//      close();
//      return notePassword;
//   }
//
//   @Override
//   void checkDetails() {
//      notePassword = password.getPassword();
//   }
//}

import java.util.Arrays;

class NotePasswordFrame extends EnterPasswordFrame {
   char[] password;
   NotePasswordEvent event;

   NotePasswordFrame(char[] password, NotePasswordEvent event) {
      super();
      this.password = password;
      this.event = event;
      jFrame.setVisible(true);
   }

   @Override
   void checkDetails() {
      if (Arrays.equals(password, super.password.getPassword())) {
         event.show();
      }
      close();
   }
}
