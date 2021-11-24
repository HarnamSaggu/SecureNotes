package SecureNotes;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class NoteListModel extends AbstractListModel {
   private final List<Note> noteList = new ArrayList<>();

   public void addElement(Note obj) {
      noteList.add(obj);
      fireIntervalAdded(this, noteList.size() - 1, noteList.size() - 1);
   }

   public Note get(int index) {
      return noteList.get(index);
   }

   @Override
   public Object getElementAt(int index) { return noteList.get(index).title; }

   @Override
   public int getSize() { return noteList.size(); }
}
