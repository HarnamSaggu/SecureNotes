import SecureNotes.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
   public static void main(String[] args) throws IOException {
//      new StartFrame();

//      InfoHolder.USERNAME = "HELLO WORLD";
//      InfoHolder.PASSWORD = "PASSWORD".toCharArray();
//      NoteIO.getAllFiles("unused resources");

//      {
//      InfoHolder.PASSWORD = "PASSWORD".toCharArray();
//      InfoHolder.USERNAME = "HELLO WORLD";
//      Note note1 = new Note("Note title", "Note body,\nHello world!", "password");
//      System.out.println("note1:\n\t" + note1);
//      NoteIO.write(note1, "unused resources/testnote.scn");
//
//      System.out.println("note1 serialised:\n\t" + Crypt.decrypt(NoteIO.toString(note1), new String(InfoHolder.PASSWORD)));
//      System.out.println("note1 serialised decrypted:\n\t" + NoteIO.toString(note1));
//
//      Note note2 = NoteIO.read("unused resources/testnote.scn");
//      System.out.println("note2:\n\t" + note2);
//      }

//      InfoHolder.PASSWORD = "password".toCharArray();
//
//      Note note = new Note("Hello world", "Hello world, this is the body\nWelcome!", "password".toCharArray());
//      String noteStr = NoteIO.toString(note);
//      System.out.println(noteStr);
//
//      try(BufferedWriter writer = Files.newBufferedWriter(Path.of("unused resources/note test.scn"), StandardCharsets.UTF_8)){
//         writer.write(noteStr);
//      }catch(IOException ex){
//         ex.printStackTrace();
//      }
//
//      String fileName = "unused resources/note test.scn";
//      try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {
//         int c;
//         System.out.println();
//         while ((c = reader.read()) != -1) {
//            System.out.print((char) c);
//         }
//      } catch (IOException e) {
//         e.printStackTrace();
//      }
   }
}
