package SecureNotes;

import java.sql.*;

class DBConnection {
   static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/secure_notes";
   static final String USER = "user1";
   static final String PASSWORD = "password";

   Connection conn;
   ResultSet resultSet;

   PreparedStatement selectPassword;
   PreparedStatement insertUser;
   PreparedStatement updateUser;
   PreparedStatement deleteUser;
   PreparedStatement updateTimeout;
   PreparedStatement selectTimeout;
   PreparedStatement nullifyTimeout;
   PreparedStatement selectNotes;
   PreparedStatement insertNote;
   PreparedStatement updateNote;
   PreparedStatement deleteNote;
   PreparedStatement selectId;

   DBConnection() {
      if (UserData.blockRequest) {
         return;
      }

      try {
         conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);

         selectPassword = conn.prepareStatement("SELECT hashed_password FROM users WHERE username = ?;");
         insertUser = conn.prepareStatement("INSERT INTO users (username, hashed_password) VALUES (?, ?);");
         updateUser = conn.prepareStatement("UPDATE users SET username = ?, hashed_password = ? WHERE username = ?;");
         deleteUser = conn.prepareStatement("DELETE FROM users WHERE username = ?;");
         updateTimeout = conn.prepareStatement("UPDATE users SET timeout = ? WHERE username = ?;");
         selectTimeout = conn.prepareStatement("SELECT timeout FROM users WHERE username = ?;");
         nullifyTimeout = conn.prepareStatement("UPDATE users SET timeout = NULL WHERE username = ?;");
         selectNotes = conn.prepareStatement("SELECT id, title, body, password FROM notes WHERE username = ?;");
         insertNote = conn.prepareStatement("INSERT INTO notes (username, title, body, password) VALUES (?, ?, ?, ?);");
         updateNote = conn.prepareStatement("UPDATE notes SET username = ?, title = ?, body = ?, password = ? WHERE id = ?;");
         deleteNote = conn.prepareStatement("DELETE FROM notes WHERE id = ?;");
         selectId = conn.prepareStatement("SELECT id FROM notes WHERE username = ?;");
      } catch (SQLException ex) {
         ex.printStackTrace();
         close();
      }
   }

   ResultSet selectUser(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectPassword.setString(1, a);
      resultSet = selectPassword.executeQuery();
      return resultSet;
   }

   void updateUser(String a, String b, String c) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      updateUser.setString(1, a);
      updateUser.setString(2, b);
      updateUser.setString(3, c);
      updateUser.executeUpdate();
   }

   void insertUser(String a, String b) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      insertUser.setString(1, a);
      insertUser.setString(2, b);
      insertUser.executeUpdate();
   }

   void deleteUser(String a) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      deleteUser.setString(1, a);
      deleteUser.executeUpdate();
   }

   void updateTimeout(String a, String b) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      updateTimeout.setString(1, a);
      updateTimeout.setString(2, b);
      updateTimeout.executeUpdate();
   }

   ResultSet selectTimeout(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectTimeout.setString(1, a);
      resultSet = selectTimeout.executeQuery();
      return resultSet;
   }

   void nullifyTimeout(String a) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      nullifyTimeout.setString(1, a);
      nullifyTimeout.executeUpdate();
   }

   ResultSet selectNotes(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectNotes.setString(1, a);
      resultSet = selectNotes.executeQuery();
      return resultSet;
   }

   void insertNote(String a, String b, String c, String d) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      insertNote.setString(1, a);
      insertNote.setString(2, b);
      insertNote.setString(3, c);
      insertNote.setString(4, d);
      insertNote.executeUpdate();
   }

   void updateNote(String a, String b, String c, String d, int e) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      updateNote.setString(1, a);
      updateNote.setString(2, b);
      updateNote.setString(3, c);
      updateNote.setString(4, d);
      updateNote.setInt(5, e);
      updateNote.executeUpdate();
   }

   ResultSet selectId(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectId.setString(1, a);
      resultSet = selectId.executeQuery();
      return resultSet;
   }

   void deleteNote(int a) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      deleteNote.setInt(1, a);
      deleteNote.executeUpdate();
   }

   void close() {
      try {
         resultSet.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectPassword.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         insertUser.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         updateUser.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         deleteUser.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         updateTimeout.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectTimeout.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         nullifyTimeout.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectNotes.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         insertNote.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         updateNote.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         deleteNote.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectId.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         conn.close();
      } catch (Exception e) { /* Ignored */ }
   }
}
