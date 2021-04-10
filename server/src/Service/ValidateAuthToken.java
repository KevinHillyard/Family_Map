package Service;

import Data_Access.AuthTokenDao;
import Data_Access.UserDao;
import Model.AuthToken;
import Model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class ValidateAuthToken extends CreateConnection {
    private Connection connection = null;

    /**
     * Validates the Authtoken and returns the User model of the user associated with that token.
     * @param token
     * @return Returns a User model or null.
     */
    public User validate(String token) {
        this.openConnection();
        AuthToken authToken = this.validateAuthToken(token);
        if (authToken == null){
            this.closeConnection(this.connection);
            return null;
        }
        else {
            User user = this.getUser(authToken.getAssociated_user_ID());
            this.closeConnection(this.connection);
            return user;
        }
    }

    /**
     * validates the authToken passed by seeing if it is found in the authToken table.
     * @param token
     * @return returns the authToken model with the token and associated userID.
     */
    private AuthToken validateAuthToken(String token){
        AuthToken authToken = null;
        AuthTokenDao authTokenDao = new AuthTokenDao(this.connection);
        try {
            authToken = authTokenDao.findAuthToken(token, this.connection);
            if (authToken == null){
                this.closeConnection(this.connection);
                return null;
            }
            else {
                return authToken;
            }
        }
        catch (SQLException error) {
            System.out.println(error.getMessage());
            this.closeConnection(this.connection);
            return null;
        }
    }

    /**
     * Gets the User from the user table with the ID passed in.
     * @param ID
     * @return return the user found or null.
     */
    private User getUser(String ID) {
        User user = null;
        UserDao userDao = new UserDao(this.connection);

        try {
            user = userDao.findUserID(ID, this.connection);

            if (user == null){
                this.closeConnection(this.connection);
                return null;
            }
            else {
                return user;
            }
        }
        catch (SQLException error) {
            System.out.println(error.getMessage());
            this.closeConnection(this.connection);
            return null;
        }
    }

    /**
     * opens a connection with the database.
     */
    private void openConnection() {
        this.connection = this.createConnection();
    }
}