package Service;

import Data_Access.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Service class for clear request.
 * Clears the database.
 */
public class S_Clear extends CreateConnection {
    private Connection connection = null;

    /**
     * Clears the database and returns boolean of the result.
     * @return
     */
    public boolean clearDataBase() throws SQLException {
        this.openConnection();
        AuthTokenDao authTokenDao = new AuthTokenDao(this.connection);
        EventDao eventDao = new EventDao(this.connection);
        PersonDao personDao = new PersonDao(this.connection);
        UserDao userDao = new UserDao(this.connection);

        try {
            if (authTokenDao.clearAuthtoken(this.connection) && eventDao.clear(this.connection) &&
            personDao.clear(this.connection) && userDao.clear(this.connection)){
                this.connection.commit();
                return true;
            }
            else {
                this.connection.rollback();
                return false;
            }
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            this.connection.rollback();
            return false;
        }
        finally {
            this.closeConnection(this.connection);
        }
    }

    /**
     * Opens a connection with the database.
     */
    private void openConnection() {
        this.connection = this.createConnection();
    }
}
