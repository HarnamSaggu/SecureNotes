package SecureNotes;

public class LaunchSecureNotes {
   private static LaunchSecureNotes launchSecureNotes;

   private LaunchSecureNotes() {
      new StartFrame();
   }

   public static LaunchSecureNotes launch() {
      if (launchSecureNotes == null) {
         launchSecureNotes = new LaunchSecureNotes();
      }

      return launchSecureNotes;
   }
}
