package Service;

import Data_Access.EventDao;
import Model.Event;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Service class to find Event with specific ID.
 * Calls Event DAO to find event with matching ID.
 */
public class S_EventWithID extends CreateConnection {
    private Connection connection = null;

    /**
     * Gets the event that matches the ID then returns it.
     * @param ID
     * @return Returns the event matching the ID.
     */
    public Event findEvent(String ID) throws SQLException {
        EventDao eventDao = new EventDao();
        Event event;
        this.openConnection();

        try {
            event = eventDao.findEventID(ID, this.connection);
            return event;
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
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
