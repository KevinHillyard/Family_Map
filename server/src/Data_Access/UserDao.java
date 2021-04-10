package Data_Access;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import Model.User;

/**
 * User DAO class to access the database regarding all User data.
 */
public class UserDao {

    public UserDao(){

    }

    /**
     * Constructor that takes a connection parameter and creates the table if it does not already exist.
     * @param connection
     */
    public UserDao(Connection connection) {
        CreateTableStatements createTableStatements = new CreateTableStatements();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createTableStatements.getCreate_user_table());
        }
        catch (SQLException error) {
            System.out.println("Error Setting Table");
            System.out.println(error.getMessage());
        }
    }

    /**
     * Searches User table for the given username.
     * @param find_username
     * @return Returns true if found else false.
     */
    public User findUsername(String find_username, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = new User();
        try {
            String sql = "select * " +
                    "from user " +
                    "where username = '" + find_username + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                user.setID(rs.getString(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setFirst_name(rs.getString(5));
                user.setLast_name(rs.getString(6));
                char gender = rs.getString(7).charAt(0);
                user.setGender(gender);
                user.setAssociated_personID(rs.getString(8));
            }
        }
        catch (SQLException error) {
            return null;
        }
        finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        if (user.getID() == null){
            return null;
        }
        return user;
    }

    /**
     * Searches User table for the given username.
     * @param find_ID
     * @return Returns true if found else false.
     */
    public User findUserID(String find_ID, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = new User();
        try {
            String sql = "select * " +
                    "from user " +
                    "where id = '" + find_ID + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                user.setID(rs.getString(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setFirst_name(rs.getString(5));
                user.setLast_name(rs.getString(6));
                char gender = rs.getString(7).charAt(0);
                user.setGender(gender);
                user.setAssociated_personID(rs.getString(8));
            }
        }
        catch (SQLException error) {
            return null;
        }
        finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        if (user.getID() == null){
            return null;
        }
        return user;
    }

    /**
     * Adds the data for the User object passed in.
     * @param user
     * @return Returns true if added else false.
     */
    public boolean addUser(User user, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String userID = user.getID();
            String uname = user.getUsername();
            String pword = user.getPassword();
            String email = user.getEmail();
            String fname = user.getFirst_name();
            String lname = user.getLast_name();
            String gender = Character.toString(user.getGender());
            String personID = user.getAssociated_personID();
            String sql = "INSERT INTO user (id, username, password, email, first_name, last_name, gender, associated_personID) " +
                    "VALUES ('" + userID + "', '" + uname + "', '" + pword + "', '" + email + "', '" + fname + "', '" +
                    lname + "', '" + gender + "', '" + personID + "');";
            stmt = connection.prepareStatement(sql);

           if (stmt.executeUpdate() != 1) {
               return false;
           }
        }
        catch (SQLException e){
            System.out.println("Error inserting test user into table.");
            System.out.println(e.getMessage());
            return false;
        }
        finally {
            if (stmt != null) stmt.close();
        }
        return true;
    }

    /**
     *
     * @param connection
     * @return Retruns true if clear happened, else false if an error occured.
     * @throws SQLException
     */
    public boolean clear(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try{
            String sql = "DELETE FROM user;";
            stmt = connection.prepareStatement(sql);
            int num_updates = stmt.executeUpdate();
            return true;
        }
        catch (SQLException error){
            System.out.println(error);
            return false;
        }
        finally {
            if (stmt != null) stmt.close();
        }
    }
}
