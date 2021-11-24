package SecureNotes;

class NotePasswordFrame extends EnterPasswordFrame {
   volatile char[] notePassword;

   NotePasswordFrame() {
      super();
   }

   char[] run() {
      jFrame.setVisible(true);
      while (notePassword == null) {
         Thread.onSpinWait();
      }
      close();
      return notePassword;
   }

   @Override
   void checkDetails() {
      notePassword = password.getPassword();
   }
}
