package secure.notes;

import java.util.Objects;

class Note {
   final String title;
   final String body;
   final String password;
   int id;

   Note(String title, String body, String password) {
      this.title = title;
      this.body = body;
      this.password = password;
   }

   Note(int id, String title, String body, String password) {
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

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Note note = (Note) o;

      if (id != note.id) return false;
      if (!Objects.equals(title, note.title)) return false;
      if (!Objects.equals(body, note.body)) return false;
      return Objects.equals(password, note.password);
   }

   @Override
   public int hashCode() {
      int result = title != null ? title.hashCode() : 0;
      result = 31 * result + (body != null ? body.hashCode() : 0);
      result = 31 * result + (password != null ? password.hashCode() : 0);
      result = 31 * result + id;
      return result;
   }
}
