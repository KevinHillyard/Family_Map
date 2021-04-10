package Data_Access;

import Model.AuthToken;

import java.sql.*;

/**
 * AuthToken DAO class to access the database regarding all AuthToken data.
 */
public class AuthTokenDao {

    public AuthTokenDao(){ }

    /**
     * Constructor that takes a connection parameter and creates the table if it does not already exist.
     * @param connection
     */
    public AuthTokenDao(Connection connection) {
        CreateTableStatements createTableStatements = new CreateTableStatements();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createTableStatements.getCreate_authToken_table());
        }
        catch (SQLException error) {
            System.out.println("Error Setting Table");
            System.out.println(error.getMessage());
        }
    }

    /**
     * Adds the given authtoken to the authtoken table
     * @param authToken
     * @param connection
     * @return Returns true if added else false.
     */
    public boolean addAuthToken(AuthToken authToken, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO auth_token ( token, associated_user_ID) " +
                    "VALUES ('" + authToken.getToken() + "', '" + authToken.getAssociated_user_ID() + "');";
            stmt = connection.prepareStatement(sql);

            if (stmt.executeUpdate() != 1){
                return false;
            }
            else {
                return true;
            }
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return false;
        }
        finally {
            if (stmt != null) stmt.close();
        }
    }

    /**
     * Find the authToken with the associated
     * @param given_userID
     * @param connection
     * @return Returns the Authtoken if found else returns null.
     * @throws SQLException
     */
    public AuthToken findAuthTokenWithUserID(String given_userID, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AuthToken authToken = new AuthToken();
        try {
            String sql = "select * " +
                    "from auth_token " +
                    "where associated_user_ID = '" + given_userID + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                authToken.setToken(rs.getString(1));
                authToken.setAssociated_user_ID(rs.getString(2));
            }
        }

        catch (SQLException error) {
            System.out.println(error.getMessage());
            return null;
        }
        finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        if (authToken.getToken() == null){
            return null;
        }
        return authToken;
    }

    /**
     * Searches for the AuthToken and returns it if found.
     * @param token
     * @param connection
     * @return Returns the AuthToken model if found else returns null.
     */
    public AuthToken findAuthToken(String token, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AuthToken authToken = new AuthToken();
        try {
            String sql = "select * " +
                    "from auth_token " +
                    "where token = '" + token + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                authToken.setToken(rs.getString(1));
                authToken.setAssociated_user_ID(rs.getString(2));
            }
        }

        catch (SQLException error) {
            System.out.println(error.getMessage());
            return null;
        }
        finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        if (authToken.getToken() == null){
            return null;
        }
        return authToken;
    }

    /**
     * Removes Authtoken from table with matching token.
     * @param token
     * @param connection
     * @return true if found and removed, or not found else false.
     */
    public boolean removeAuthtoken(String token, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM auth_token WHERE token = '" + token + "';";
            stmt = connection.prepareStatement(sql);
            if (stmt.executeUpdate() == 1){
                return true;
            }
            else {
                return false;
            }
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return false;
        }
        finally {
            if (stmt != null) stmt.close();
        }
    }

    /**
     * Clears table of all Authtokens.
     * @param connection
     * @return Returns true if table cleared, else false if an error.
     */
    public boolean clearAuthtoken(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try{
            String sql = "DELETE FROM auth_token;";
            stmt = connection.prepareStatement(sql);
            int num_updates = stmt.executeUpdate();
            return true;
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return false;
        }
        finally {
            if (stmt != null) stmt.close();
        }
    }
}
