package Service;

import Data_Access.AuthTokenDao;
import Data_Access.PersonDao;
import Data_Access.UserDao;
import Model.AuthToken;
import Model.Person;
import Model.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the service class for Person, and calls the person DAO
 * to get all persons associated to the user.
 */
public class S_Person extends CreateConnection {
    private Connection connection = null;

    /**
     * Gets all of the Persons from the Person table associated with the username
     * @param username
     * @return returns an array of persons associated to the authtoken passed in.
     */
    public Person[] getPersons(String username){
        // I need to use the authtoken to find the associated userID from AuthToken Table.
        // With the userID I search for the associated username from the user Table.
        // With the associated username I search the person table for all persons with that associated username.
        this.openConnection();
        try {
            PersonDao personDao = new PersonDao(connection);
            Person[] persons = personDao.findPersons(username, connection);
            this.closeConnection(connection);
            return persons;
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
