package Service;

import Model.Event;
import Model.AuthToken;
import Model.User;
import Data_Access.AuthTokenDao;
import Data_Access.EventDao;
import Data_Access.UserDao;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Service class to find all events associated with the user.
 * Calls Event DAO to find all events associated with the user.
 */
public class S_Event extends CreateConnection {
    private Connection connection = null;

    /**
     * Gets all of the events from the Events table associated with the username
     * @param username
     * @return returns an array of events associated to the authtoken passed in.
     */
    public Event[] getEvents(String username){
        // I need to use the authtoken to find the associated userID from AuthToken Table.
        // With the userID I search for the associated username from the user Table.
        // With the associated username I search the events table for all events with that associated username.
        this.openConnection();
        try {
            EventDao eventDao = new EventDao(connection);
            Event[] events = eventDao.findEvents(username, connection);
            this.closeConnection(connection);
            return events;
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
