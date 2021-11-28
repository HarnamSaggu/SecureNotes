package SecureNotes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Note implements Serializable {
   String title;
   String body;
   char[] password;
   String filepath;
   String filename;

   public Note(String title, String body, char[] password, String filepath) {
      this.title = title;
      this.body = body;
      this.password = password;
      this.filepath = filepath;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Note note = (Note) o;

      if (!Objects.equals(title, note.title)) return false;
      if (!Objects.equals(body, note.body)) return false;
      if (!Arrays.equals(password, note.password)) return false;
      if (!Objects.equals(filepath, note.filepath)) return false;
      return Objects.equals(filename, note.filename);
   }

   @Override
   public int hashCode() {
      int result = title != null ? title.hashCode() : 0;
      result = 31 * result + (body != null ? body.hashCode() : 0);
      result = 31 * result + Arrays.hashCode(password);
      result = 31 * result + (filepath != null ? filepath.hashCode() : 0);
      result = 31 * result + (filename != null ? filename.hashCode() : 0);
      return result;
   }

   @Override
   public String toString() {
      return "Note{" +
              "title='" + title + '\'' +
              ", body='" + body + '\'' +
              ", password=" + Arrays.toString(password) +
              ", filepath='" + filepath + '\'' +
              ", filename='" + filename + '\'' +
              '}';
   }
}
