package Service;

import Data_Access.AuthTokenDao;
import Data_Access.UserDao;
import HelpfulFunctions.RandomStringGenerator;
import Model.AuthToken;
import Model.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the service class for logging in a user.
 * Calls User DAO to check the credentials passed in.
 */
public class S_LoginUser extends CreateConnection {
    private Connection connection;

    /**
     * Checks if the username and password match that found in the database
     * by calling the User Dao to check the private username and password strings.
     * @return returns a string array with the authToken, username, and personID.
     */
    public String[] checkCredentials(String given_username, String given_password) {
        // Check User table to find the username and then check if the password matches.
        // Once I confirm that generate an authToken with the matching ID.
        // Then I get the personID from the user and return the username, authToken, and personID for the user.
        try {
            this.openConnection();
            UserDao userDao = new UserDao(this.connection);
            User user = userDao.findUsername(given_username, this.connection);
            if (user == null) {
                this.closeConnection(this.connection);
                String[] not_found = new String[1];
                not_found[0] = "not_found";
                return not_found;
            }
            if (!user.getPassword().equals(given_password)){
                this.connection.rollback();
                this.closeConnection(this.connection);
                String[] incorrect_password = new String[2];
                incorrect_password[0] = "incorrect";
                incorrect_password[1] = "password";
                return incorrect_password;
            }
            RandomStringGenerator rsg = new RandomStringGenerator();
            AuthTokenDao authTokenDao = new AuthTokenDao(this.connection);
            AuthToken authToken = new AuthToken();
            authToken.setToken(rsg.generateRandomID(null));
            authToken.setAssociated_user_ID(user.getID());
            authTokenDao.addAuthToken(authToken, this.connection);

            this.connection.commit();
            this.closeConnection(this.connection);

            String[] result_array = new String[3];
            result_array[0] = authToken.getToken();
            result_array[1] = user.getUsername();
            result_array[2] = user.getAssociated_personID();

            return result_array;
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return null;
        }
    }

    private void openConnection() {
        this.connection = this.createConnection();
    }
}
