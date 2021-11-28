package SecureNotes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class NoteIO {
   public static Object fromString(String s) throws IOException, ClassNotFoundException {
      s = Crypt.decrypt(s, new String(UserData.PASSWORD));
      s = Crypt.decrypt(s, UserData.USERNAME);
      byte[] data = Base64.getDecoder().decode(s);
      try {
         ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
         Object o = ois.readObject();
         ois.close();
         return o;
      } catch (EOFException ignored) {

      }
      return null;
   }

   public static String toString(Serializable o) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(o);
      oos.close();
      return Crypt.encrypt(
              Crypt.encrypt(
                      Base64.getEncoder().encodeToString(baos.toByteArray()), UserData.USERNAME
              ),
              new String(UserData.PASSWORD)
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
//      File folder = new File(path);
//      File[] listOfFiles = folder.listFiles();
//      List<Note> allNotes = new ArrayList<>();

//      if (listOfFiles == null) return new ArrayList<>();
//
//      for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
//         String notePath = listOfFiles[i].getPath();
//         if (listOfFiles[i].isFile() && notePath.endsWith(".scn")) {
//            try {
//               allNotes.add(read(notePath));
//               System.out.println(allNotes.get(allNotes.size() - 1));
//            } catch (Exception ignored) {}
//         } else if (listOfFiles[i].isDirectory()) {
//            allNotes.addAll(getAllFiles(listOfFiles[i].getPath()));
//         }
//      }
//
//      return allNotes;

      List<Note> list = new ArrayList<>();

      File folder = new File(path);
      File[] listOfFiles = folder.listFiles();

      if (listOfFiles == null) return list;

      for (File listOfFile : listOfFiles) {
         if (listOfFile.isDirectory()) {
            list.addAll(getAllFiles(listOfFile.getPath()));
         } else if (listOfFile.isFile() && listOfFile.getPath().endsWith(".scn")) {
            Note note;
            note = read(listOfFile.getPath());
            if (note != null) list.add(note);
         }
      }

      return list;
   }

   static List<String> list(String path) {
      List<String> list = new ArrayList<>();

      File folder = new File(path);
      File[] listOfFiles = folder.listFiles();

      if (listOfFiles == null) return list;

      for (File listOfFile : listOfFiles) {
         if (listOfFile.isDirectory()) {
            list.addAll(list(listOfFile.getPath()));
         } else if (listOfFile.isFile() && listOfFile.getPath().endsWith(".scn")) {
            list.add(listOfFile.getPath());
         }
      }

      return list;
   }
}
