package SecureNotes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class NoteIO {
   public static Object fromString(String s) throws IOException, ClassNotFoundException {
      s = Crypt.decrypt(s, new String(InfoHolder.PASSWORD));
      s = Crypt.decrypt(s, InfoHolder.USERNAME);
      byte[] data = Base64.getDecoder().decode(s);
      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
      Object o  = ois.readObject();
      ois.close();
      return o;
   }

   public static String toString(Serializable o) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(o);
      oos.close();
      return Crypt.encrypt(
              Crypt.encrypt(
                      Base64.getEncoder().encodeToString(baos.toByteArray()), InfoHolder.USERNAME
              ),
              new String(InfoHolder.PASSWORD)
      );
   }

   public static void write(Note note, String path) {
      try (BufferedWriter writer = Files.newBufferedWriter(Path.of(path), StandardCharsets.UTF_8)) {
         writer.write(toString(note));
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public static Note read(String path) {
      try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {
         int c;
         StringBuilder encryptedSerialisedNote = new StringBuilder();
         while ((c = reader.read()) != -1) {
            encryptedSerialisedNote.append((char) c);
         }
         return (Note) fromString(encryptedSerialisedNote.toString());
      } catch (IOException | ClassNotFoundException e) {
         e.printStackTrace();
      }
      return null;
   }

   public static List<Note> getAllFiles(String path) {
      File folder = new File(path);
      File[] listOfFiles = folder.listFiles();
      List<Note> allNotes = new ArrayList<>();

      for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
         String notePath = listOfFiles[i].getPath();
         if (listOfFiles[i].isFile() && notePath.endsWith(".scn")) {
            try {
               allNotes.add(read(notePath));
               System.out.println(allNotes.get(allNotes.size() - 1));
            } catch (NullPointerException ignored) {}
         }
      }

      return allNotes;
   }
}
