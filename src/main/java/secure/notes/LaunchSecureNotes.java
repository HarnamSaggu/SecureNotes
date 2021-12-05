package secure.notes;

public class LaunchSecureNotes {
   private static LaunchSecureNotes instance;
   private final StartFrame startFrame;

   private LaunchSecureNotes() {
      startFrame = new StartFrame();
   }

   public static LaunchSecureNotes getInstance() {
      if (instance == null) {
         instance = new LaunchSecureNotes();
      }
      return instance;
   }

   public void close() {
      startFrame.close();
   }
}
