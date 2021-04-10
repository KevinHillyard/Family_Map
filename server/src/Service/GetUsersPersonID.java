package Service;

import Data_Access.UserDao;
import Model.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Gets the personID for the active user.
 */
public class GetUsersPersonID extends CreateConnection {
    private Connection connection = null;

    /**
     * Returns the PersonID for the user with the given Username.
     * @param username
     * @return
     */
    public String getUsersPersonID(String username) {
        try {
            this.connection = this.createConnection();
            UserDao userDao = new UserDao(this.connection);
            User user = userDao.findUsername(username, this.connection);
            if (user == null) {
                user.setID("not_found");
                return user.getID();
            }
            return user.getAssociated_personID();
        }
        catch (SQLException error) {
            System.out.println(error.getMessage());
            return null;
        }
        finally {
            this.closeConnection(this.connection);
        }
    }
}
