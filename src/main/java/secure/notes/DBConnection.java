package secure.notes;

import java.sql.*;

class DBConnection {
   static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/secure_notes";
   static final String USER = "user1";
   static final String PASSWORD = "password";

   Connection conn;
   ResultSet resultSet;

   PreparedStatement selectPasswordStatement;
   PreparedStatement updateUserStatement;
   PreparedStatement insertUserStatement;
   PreparedStatement deleteUserStatement;
   PreparedStatement updateTimeoutStatement;
   PreparedStatement selectTimeoutStatement;
   PreparedStatement nullifyTimeoutStatement;
   PreparedStatement selectNotesStatement;
   PreparedStatement insertNoteStatement;
   PreparedStatement updateNoteStatement;
   PreparedStatement deleteNoteStatement;
   PreparedStatement selectIdStatement;

   DBConnection() {
      if (UserData.isBlocked()) {
         return;
      }

      try {
         conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);

         selectPasswordStatement = conn.prepareStatement("SELECT hashed_password FROM users WHERE username = ?;");
         updateUserStatement = conn.prepareStatement("UPDATE users SET username = ?, hashed_password = ? WHERE username = ?;");
         insertUserStatement = conn.prepareStatement("INSERT INTO users (username, hashed_password) VALUES (?, ?);");
         deleteUserStatement = conn.prepareStatement("DELETE FROM users WHERE username = ?;");
         updateTimeoutStatement = conn.prepareStatement("UPDATE users SET timeout = ? WHERE username = ?;");
         selectTimeoutStatement = conn.prepareStatement("SELECT timeout FROM users WHERE username = ?;");
         nullifyTimeoutStatement = conn.prepareStatement("UPDATE users SET timeout = NULL WHERE username = ?;");
         selectNotesStatement = conn.prepareStatement("SELECT id, title, body, password FROM notes WHERE username = ?;");
         insertNoteStatement = conn.prepareStatement("INSERT INTO notes (username, title, body, password) VALUES (?, ?, ?, ?);");
         updateNoteStatement = conn.prepareStatement("UPDATE notes SET username = ?, title = ?, body = ?, password = ? WHERE id = ?;");
         deleteNoteStatement = conn.prepareStatement("DELETE FROM notes WHERE id = ?;");
         selectIdStatement = conn.prepareStatement("SELECT id FROM notes WHERE username = ?;");
      } catch (SQLException ex) {
         ex.printStackTrace();
         close();
      }
   }

   ResultSet selectPassword(String a) throws SQLException {
      if (UserData.isBlocked()) {
         return null;
      }

      selectPasswordStatement.setString(1, a);
      resultSet = selectPasswordStatement.executeQuery();
      return resultSet;
   }

   void updateUser(String a, String b, String c) throws SQLException {
      if (UserData.isBlocked()) {
         return;
      }

      updateUserStatement.setString(1, a);
      updateUserStatement.setString(2, b);
      updateUserStatement.setString(3, c);
      updateUserStatement.executeUpdate();
   }

   void insertUser(String a, String b) throws SQLException {
      if (UserData.isBlocked()) {
         return;
      }

      insertUserStatement.setString(1, a);
      insertUserStatement.setString(2, b);
      insertUserStatement.executeUpdate();
   }

   void deleteUser(String a) throws SQLException {
      if (UserData.isBlocked()) {
         return;
      }

      deleteUserStatement.setString(1, a);
      deleteUserStatement.executeUpdate();
   }

   void updateTimeout(String a, String b) throws SQLException {
      if (UserData.isBlocked()) {
         return;
      }

      updateTimeoutStatement.setString(1, a);
      updateTimeoutStatement.setString(2, b);
      updateTimeoutStatement.executeUpdate();
   }

   ResultSet selectTimeout(String a) throws SQLException {
      if (UserData.isBlocked()) {
         return null;
      }

      selectTimeoutStatement.setString(1, a);
      resultSet = selectTimeoutStatement.executeQuery();
      return resultSet;
   }

   void nullifyTimeout(String a) throws SQLException {
      if (UserData.isBlocked()) {
         return;
      }

      nullifyTimeoutStatement.setString(1, a);
      nullifyTimeoutStatement.executeUpdate();
   }

   ResultSet selectNotes(String a) throws SQLException {
      if (UserData.isBlocked()) {
         return null;
      }

      selectNotesStatement.setString(1, a);
      resultSet = selectNotesStatement.executeQuery();
      return resultSet;
   }

   void insertNote(String a, String b, String c, String d) throws SQLException {
      if (UserData.isBlocked()) {
         return;
      }

      insertNoteStatement.setString(1, a);
      insertNoteStatement.setString(2, b);
      insertNoteStatement.setString(3, c);
      insertNoteStatement.setString(4, d);
      insertNoteStatement.executeUpdate();
   }

   void updateNote(String a, String b, String c, String d, int e) throws SQLException {
      if (UserData.isBlocked()) {
         return;
      }

      updateNoteStatement.setString(1, a);
      updateNoteStatement.setString(2, b);
      updateNoteStatement.setString(3, c);
      updateNoteStatement.setString(4, d);
      updateNoteStatement.setInt(5, e);
      updateNoteStatement.executeUpdate();
   }

   ResultSet selectId(String a) throws SQLException {
      if (UserData.isBlocked()) {
         return null;
      }

      selectIdStatement.setString(1, a);
      resultSet = selectIdStatement.executeQuery();
      return resultSet;
   }

   void deleteNote(int a) throws SQLException {
      if (UserData.isBlocked()) {
         return;
      }

      deleteNoteStatement.setInt(1, a);
      deleteNoteStatement.executeUpdate();
   }

   void close() {
      try {
         resultSet.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectPasswordStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         insertUserStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         updateUserStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         deleteUserStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         updateTimeoutStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectTimeoutStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         nullifyTimeoutStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectNotesStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         insertNoteStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         updateNoteStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         deleteNoteStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectIdStatement.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         conn.close();
      } catch (Exception e) { /* Ignored */ }
   }
}
