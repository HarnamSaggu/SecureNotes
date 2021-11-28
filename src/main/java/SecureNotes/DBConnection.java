package SecureNotes;

import java.sql.*;

public class DBConnection {
   static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/secure_notes";
   static final String USER = "user1";
   static final String PASSWORD = "password";

   Connection conn = null;
   PreparedStatement select = null;
   PreparedStatement insert = null;
   PreparedStatement update = null;
   PreparedStatement delete = null;
   PreparedStatement updateTimeout = null;
   PreparedStatement selectTimeout = null;
   PreparedStatement nullTimeout = null;
   PreparedStatement selectFilepath = null;
   ResultSet resultSet = null;

   public DBConnection() {
      if (UserData.blockRequest) {
         return;
      }

      try {
         conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);

         select = conn.prepareStatement("SELECT hashed_password FROM users WHERE username = ?;");
         insert = conn.prepareStatement("INSERT INTO users (username, hashed_password, filepath) VALUES (?, ?, ?);");
         update = conn.prepareStatement("UPDATE users SET username = ?, hashed_password = ?, filepath = ? WHERE username = ?;");
         delete = conn.prepareStatement("DELETE FROM users WHERE username = ?;");
         updateTimeout = conn.prepareStatement("UPDATE users SET timeout = ? WHERE username = ?;");
         selectTimeout = conn.prepareStatement("SELECT timeout FROM users WHERE username = ?;");
         nullTimeout = conn.prepareStatement("UPDATE users SET timeout = NULL WHERE username = ?;");
         selectFilepath = conn.prepareStatement("SELECT filepath FROM users WHERE username = ?;");
      } catch (SQLException ex) {
         ex.printStackTrace();
         close();
      }
   }

   public ResultSet select(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      select.setString(1, a);
      resultSet = select.executeQuery();
      return resultSet;
   }

   public void update(String a, String b, String c, String d) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      update.setString(1, a);
      update.setString(2, b);
      update.setString(3, c);
      update.setString(4, d);
      update.executeUpdate();
   }

   public void insert(String a, String b, String c) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      insert.setString(1, a);
      insert.setString(2, b);
      insert.setString(3, c);
      insert.executeUpdate();
   }

   public void delete(String a) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      delete.setString(1, a);
      delete.executeUpdate();
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

   public void nullTimeout(String a) throws SQLException {
      if (UserData.blockRequest) {
         return;
      }

      nullTimeout.setString(1, a);
      nullTimeout.executeUpdate();
   }

   public ResultSet selectFilepath(String a) throws SQLException {
      if (UserData.blockRequest) {
         return null;
      }

      selectFilepath.setString(1, a);
      resultSet = selectFilepath.executeQuery();
      return resultSet;
   }

   public void close() {
      System.out.println("DELETE THIS - CLOSING CONNECTION");
      try {
         resultSet.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         select.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         insert.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         update.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         delete.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         updateTimeout.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectTimeout.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         nullTimeout.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         selectFilepath.close();
      } catch (Exception e) { /* Ignored */ }
      try {
         conn.close();
      } catch (Exception e) { /* Ignored */ }
   }
}
