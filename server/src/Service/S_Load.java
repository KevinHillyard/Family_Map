package Service;

import Data_Access.EventDao;
import Data_Access.PersonDao;
import Data_Access.UserDao;
import HelpfulFunctions.RandomStringGenerator;
import Model.Event;
import Model.Person;
import Model.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the service class for the load request.
 * Privately calls clear service.
 * Then loads passed in users, persons, and events into respective DAO's to be added.
 */
public class S_Load extends CreateConnection {
    private Connection connection;
    private static RandomStringGenerator rsg = new RandomStringGenerator();

    /**
     * Verifies that the User info is set correctly.
     * @param user
     * @return
     */
    private boolean checkUserInfo(User user) {
        if ((user.getUsername() != null) && (user.getPassword() != null) && (user.getEmail() != null) &&
                (user.getFirst_name() != null) && (user.getLast_name() != null) && (user.getAssociated_personID() != null)) {
            if ((user.getGender() == 'm') || (user.getGender() == 'f')) {
                if (user.getID() == null) {
                    user.setID(rsg.generateRandomID(null));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies that the Person info is set correctly.
     * @param person
     * @return
     */
    private boolean checkPersonInfo(Person person) {
        if ((person.getAssociated_username() != null) && (person.getFirst_name() != null) && (person.getLast_name() != null)) {
            if ((person.getGender() == 'm') || (person.getGender() == 'f')) {
                if (person.getID() == null) {
                    person.setID(rsg.generateRandomID(person));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies that the Event info is set correctly.
     * @param event
     * @return
     */
    private boolean checkEventInfo(Event event) {
        if ((event.getEvent_type() != null) && (event.getPerson_ID() != null) && (event.getAssociated_username() != null) &&
                (event.getCountry() != null) && (event.getCity() != null) && (event.getYear() != 0) &&
                (event.getLatitude() != 0) && (event.getLongitude() != 0)) {
            if (event.getID() == null) {
                event.setID(rsg.generateRandomID(null));
            }
            return true;
        }
        return false;
    }

    /**
     * Loads the data from the different arrays.
     * @param users
     * @param persons
     * @param events
     * @return
     * @throws SQLException
     */
    public int[] loadData(User[] users, Person[] persons, Event[] events) throws SQLException {
        int[] added_data = new int[3];
        added_data[0] = users.length;
        added_data[1] = persons.length;
        added_data[2] = events.length;

        S_Clear s_clear = new S_Clear();
        try {
            s_clear.clearDataBase();
        }
        catch (SQLException error) {
            System.out.println(error.getMessage());
            return null;
        }

        try {
            this.openConnection();
            UserDao userDao = new UserDao(this.connection);
            PersonDao personDao = new PersonDao(this.connection);
            EventDao eventDao = new EventDao(this.connection);
            for (int i = 0; i < users.length; ++i) {
                if (this.checkUserInfo(users[i])) {
                    userDao.addUser(users[i], this.connection);
                }
                else {
                    int[] invalid = new int[1];
                    invalid[0] = -1;
                    return invalid;
                }
            }
            for (int i = 0; i < persons.length; ++i) {
                if (this.checkPersonInfo(persons[i])) {
                    personDao.addPerson(persons[i], this.connection);
                }
                else {
                    int[] invalid = new int[1];
                    invalid[0] = -1;
                    return invalid;
                }
            }
            for (int i = 0; i < events.length; ++i) {
                if (this.checkEventInfo(events[i])) {
                    eventDao.addEvent(events[i], this.connection);
                }
                else {
                    int[] invalid = new int[1];
                    invalid[0] = -1;
                    return invalid;
                }
            }
            this.connection.commit();
            this.closeConnection(this.connection);
            return added_data;
        }
        catch (SQLException error) {
            System.out.println(error.getMessage());
            this.connection.rollback();
            this.closeConnection(this.connection);
            return null;
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
