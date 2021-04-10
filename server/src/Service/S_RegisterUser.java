package Service;

import Data_Access.AuthTokenDao;
import Data_Access.EventDao;
import Data_Access.PersonDao;
import Data_Access.UserDao;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import HelpfulFunctions.RandomStringGenerator;
import NamesAndLocations.Locations;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Year;
import java.util.Random;

/**
 * This is the service class for Registering a User. Calls the User DAO to add specific data.
 */
public class S_RegisterUser extends CreateConnection {
    // Create a new user with the given parameters.
    // Try to find the username in the User table to see if it is a duplicate username.
    // If not add to the user table.
    // Create person with user information and add to person table.
    // Create birth event for the user and add to event table.
    // Generate auth-token and add to table and return this token.
    private Connection connection;
    private RandomStringGenerator rsg = new RandomStringGenerator();
    private Gson gson = new Gson();
    private Random random = new Random();

    /**
     * Loads the locations from the json file.
     * @return
     */
    private Locations loadLocations() {
        try {
            Reader reader = new FileReader("C:\\Users\\pbhillya\\IdeaProjects\\FamilyMapServer\\locations.json");
            Locations locations = gson.fromJson(reader, Locations.class);
            reader.close();
            return locations;
        }
        catch (FileNotFoundException error) {
            // return 500 error with a problem loading the first names.
            return null;
        }
        catch (IOException error) {
            // error closing reader.
            return null;
        }
    }

    /**
     * Checks to make sure the information is set correctly.
     * @param username
     * @param password
     * @param email
     * @param first_name
     * @param last_name
     * @param gender
     * @return
     */
    private boolean checkParameters(String username, String password, String email, String first_name, String last_name, char gender) {
        if ((username == null) || (password == null) ||(email == null) ||(first_name == null) ||(last_name == null)) {
            return false;
        }
        if ((gender != 'm') && (gender != 'f')) {
            return false;
        }
        return true;
    }

    /**
     * Registers the user if the username is not a duplicate. Calls the fill request for 4 generations.
     * @param username
     * @param password
     * @param email
     * @param first_name
     * @param last_name
     * @param gender
     * @return
     */
    public AuthToken registerUser(String username, String password, String email, String first_name, String last_name, char gender){
        if (!checkParameters(username, password, email, first_name, last_name, gender)) {
            AuthToken authToken = new AuthToken();
            authToken.setToken("incorrect_parameters");
            return authToken;
        }
        this.connection = this.createConnection();
        // Create user with given information.
        User user = new User();
        UserDao userDao = new UserDao(this.connection);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setGender(gender);
        user.setID(rsg.generateRandomID(null));

        // Check if the username is a duplicate.
        try {
            if (userDao.findUsername(username, this.connection) != null) {
                AuthToken authToken = new AuthToken();
                authToken.setToken("duplicate");
                return authToken;
            }

            // Create Person for the user.
            PersonDao personDao = new PersonDao(this.connection);
            Person person = new Person();
            person.setFirst_name(first_name);
            person.setLast_name(last_name);
            person.setAssociated_username(username);
            person.setGender(gender);
            person.setID(rsg.generateRandomID(person));
            user.setAssociated_personID(person.getID());

            // Create birth Event for the user.
            EventDao eventDao = new EventDao(this.connection);
            Event birth = new Event();
            birth.setEvent_type("birth");
            birth.setPerson_ID(person.getID());
            birth.setAssociated_username(username);
            birth.setID(rsg.generateRandomID(null));
            int year = Year.now().getValue();
            birth.setYear(year - 20);
            Locations locations = this.loadLocations();
            assert locations != null;
            Locations location = locations.data[random.nextInt(locations.data.length)];
            birth.setLatitude(location.latitude);
            birth.setLongitude(location.longitude);
            birth.setCountry(location.country);
            birth.setCity(location.city);

            // Add user and person and birth Event
            if (!userDao.addUser(user, this.connection)) {
                return null;
            }
            if (!personDao.addPerson(person, this.connection)) {
                return null;
            }
            if (!eventDao.addEvent(birth, this.connection)) {
                return null;
            }

            // Create AuthToken and add to table.
            AuthTokenDao authTokenDao = new AuthTokenDao(this.connection);
            AuthToken authToken = new AuthToken();
            authToken.setAssociated_user_ID(username);
            authToken.setToken(rsg.generateRandomID(null));
            authTokenDao.addAuthToken(authToken, this.connection);
            this.connection.commit();
            this.closeConnection(this.connection);
            S_Fill s_fill = new S_Fill();
            s_fill.fill(username, 4);
            return authToken;
        }
        catch (SQLException error) {
            System.out.println(error.getMessage());
            this.closeConnection(this.connection);
            return null;
        }
        finally {
            this.closeConnection(this.connection);
        }
    }
}
