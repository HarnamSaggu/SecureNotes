package SecureNotes;

import java.sql.*;

public class DBConnection {
   static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/secure_notes";
   static final String USER = "user1";
   static final String PASSWORD = "password";

   Connection conn = null;
   ResultSet resultSet = null;

   PreparedStatement selectPassword = null;
   PreparedStatement insertUser = null;
   PreparedStatement updateUser = null;
   PreparedStatement deleteUser = null;
   PreparedStatement updateTimeout = null;
   PreparedStatement selectTimeout = null;
   PreparedStatement nullifyTimeout = null;
   PreparedStatement selectNotes = null;
   PreparedStatement insertNote = null;
   PreparedStatement updateNote = null;
   PreparedStatement selectId = null;

   public DBConnection() {
      if (UserData.blockRequest) {
         return;
      }

      try {
         conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);

         selectPassword = conn.prepareStatement("SELECT hashed_password FROM users WHERE username = ?;");
         insertUser = conn.prepareStatement("INSERT INTO users (username, hashed_password, filepath) VALUES (?, ?, ?);");
         updateUser = conn.prepareStatement("UPDATE users SET username = ?, hashed_password = ?, filepath = ? WHERE username = ?;");
         deleteUser = conn.prepareStatement("DELETE FROM users WHERE username = ?;");
         updateTimeout = conn.prepareStatement("UPDATE users SET timeout = ? WHERE username = ?;");
         selectTimeout = conn.prepareStatement("SELECT timeout FROM users WHERE username = ?;");
         nullifyTimeout = conn.prepareStatement("UPDATE users SET timeout = NULL WHERE username = ?;");
         selectNotes = conn.prepareStatement("SELECT id, title, body, password FROM notes WHERE username = ?;");
         insertNote = conn.prepareStatement("INSERT INTO notes (username, title, body, password) VALUES (?, ?, ?, ?);");
         updateNote = conn.prepareStatement("UPDATE notes SET username = ?, title = ?, body = ?, password = ? WHERE id = ?;");
         selectId = conn.prepareStatement("SELECT id FROM notes WHERE username = ?;");
      } catch (SQLException ex) {
         ex.printStackTrace();
         close();
      }
   }

   public ResultSet selectUser(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectPassword.setString(1, a);
      resultSet = selectPassword.executeQuery();
      return resultSet;
   }

   public void updateUser(String a, String b, String c, String d) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      updateUser.setString(1, a);
      updateUser.setString(2, b);
      updateUser.setString(3, c);
      updateUser.setString(4, d);
      updateUser.executeUpdate();
   }

   public void insertUser(String a, String b, String c) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      insertUser.setString(1, a);
      insertUser.setString(2, b);
      insertUser.setString(3, c);
      insertUser.executeUpdate();
   }

   public void deleteUser(String a) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      deleteUser.setString(1, a);
      deleteUser.executeUpdate();
   }

   public void updateTimeout(String a, String b) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      updateTimeout.setString(1, a);
      updateTimeout.setString(2, b);
      updateTimeout.executeUpdate();
   }

   public ResultSet selectTimeout(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectTimeout.setString(1, a);
      resultSet = selectTimeout.executeQuery();
      return resultSet;
   }

   public void nullifyTimeout(String a) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      nullifyTimeout.setString(1, a);
      nullifyTimeout.executeUpdate();
   }

   public ResultSet selectNotes(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectNotes.setString(1, a);
      resultSet = selectNotes.executeQuery();
      return resultSet;
   }

   public void insertNote(String a, String b, String c, String d) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      insertNote.setString(1, a);
      insertNote.setString(2, b);
      insertNote.setString(3, c);
      insertNote.setString(4, d);
      insertNote.executeUpdate();
   }

   public void updateNote(String a, String b, String c, String d, int e) throws SQLException {
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

   public ResultSet selectId(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectId.setString(1, a);
      resultSet = selectId.executeQuery();
      return resultSet;
   }

   public void close() {
      System.out.println("DELETE THIS - CLOSING CONNECTION");
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
         selectId.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         conn.close();
      } catch (Exception e) { /* Ignored */ }
   }
}
