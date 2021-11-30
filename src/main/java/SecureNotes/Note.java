package SecureNotes;

public class Note {
   int id;

   String title;
   String body;
   String password;

   public Note(String title, String body, String password) {
      this.title = title;
      this.body = body;
      this.password = password;
   }

   public Note(int id, String title, String body, String password) {
      this.id = id;
      this.title = title;
      this.body = body;
      this.password = password;
   }

   @Override
   public String toString() {
      return "Note{" +
              "id=" + id +
              ", title='" + title + '\'' +
              ", body='" + body + '\'' +
              ", password='" + password + '\'' +
              '}';
   }
}
