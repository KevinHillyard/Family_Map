package Service;

import java.sql.*;

/**
 * Super class that implements making a connection and closing one.
 */
public class CreateConnection {
    /**
     * Creates a connection with the database and returns the connection.
     * @return
     */
    protected Connection createConnection() {
        Connection connection = null;
        String dbName = "familymap.sqlite";
        String connectionURL = "jdbc:sqlite:" + dbName;
        try {
            connection = DriverManager.getConnection(connectionURL);

            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return connection;
    }

    /**
     * Closes the connection with the database.
     */
    protected void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }
}
