package Data_Access;

import Model.Event;

import java.sql.*;
import java.util.Vector;

/**
 * Event DAO class to access the database regarding all Event data.
 */
public class EventDao {

    public EventDao(){

    }

    /**
     * Constructor that takes a connection parameter and creates the table if it does not already exist.
     * @param connection
     */
    public EventDao(Connection connection) {
        CreateTableStatements createTableStatements = new CreateTableStatements();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createTableStatements.getCreate_event_table());
        }
        catch (SQLException error) {
            System.out.println("Error Setting Table");
            System.out.println(error.getMessage());
        }
    }

    /**
     * Adds the event object's data to the database
     * @param event
     * @param connection
     * @return Return true if added else false.
     */
    public boolean addEvent(Event event, Connection connection) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO events (id, associated_username, associated_person_ID, latitude, longitude, country, city, year, event_type) " +
                    "VALUES ('" + event.getID() + "', '" + event.getAssociated_username() + "', '" + event.getPerson_ID()  + "', '" +
                    event.getLatitude() + "', '" + event.getLongitude() + "', '" + event.getCountry() + "', '" + event.getCity() + "', '" +
                    event.getYear() + "', '" + event.getEvent_type() + "');";
            stmt = connection.prepareStatement(sql);

            if (stmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Looks in the Event table for the Event ID.
     * @param ID
     * @param connection
     * @return Returns the Event found
     */
    public Event findEventID(String ID, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Event found_event = new Event();

        try {
            String sql = "select * " +
                    "from events " +
                    "where id = '" + ID + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();

            while (rs.next()){
                found_event.setID(rs.getString(1));
                found_event.setAssociated_username(rs.getString(2));
                found_event.setPerson_ID(rs.getString(3));
                found_event.setLatitude(rs.getDouble(4));
                found_event.setLongitude(rs.getDouble(5));
                found_event.setCountry(rs.getString(6));
                found_event.setCity(rs.getString(7));
                found_event.setYear(rs.getInt(8));
                found_event.setEvent_type(rs.getString(9));
            }
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return null;
        }
        finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        if (found_event.getID() == null) {
            return null;
        }
        return found_event;
    }

    /**
     * Finds all events associated to the username.
     * @param find_associated_username
     * @param connection
     * @return Returns an array of events associated with the username.
     */
    public Event[] findEvents(String find_associated_username, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector<Event> found_events_vector = new Vector<>();

        try {
            String sql = "select * " +
                    "from events " +
                    "where associated_username = '" + find_associated_username + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();

            while (rs.next()){
                Event found_event = new Event();
                found_event.setID(rs.getString(1));
                found_event.setAssociated_username(rs.getString(2));
                found_event.setPerson_ID(rs.getString(3));
                found_event.setLatitude(rs.getDouble(4));
                found_event.setLongitude(rs.getDouble(5));
                found_event.setCountry(rs.getString(6));
                found_event.setCity(rs.getString(7));
                found_event.setYear(rs.getInt(8));
                found_event.setEvent_type(rs.getString(9));
                found_events_vector.add(found_event);
            }
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return null;
        }
        finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        if (found_events_vector.size() == 0) {
            return null;
        }
        Event[] found_events_array = new Event[found_events_vector.size()];
        for (int i = 0; i < found_events_vector.size(); ++i){
            found_events_array[i] = found_events_vector.elementAt(i);
        }
        return found_events_array;
    }

    /**
     * Removes the event with the matching ID.
     * @param ID
     * @param connection
     * @return Returns true if removed else false.
     */
    public boolean remove(String ID, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM events WHERE id = '" + ID + "';";
            stmt = connection.prepareStatement(sql);
            if (stmt.executeUpdate() == 1){
                return true;
            }
            else {
                return false;
            }
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return false;
        }
        finally {
            if (stmt != null) stmt.close();
        }
    }

    /**
     * Removes all events from the table.
     * @param connection
     * @return Returns true if removed else false.
     */
    public boolean clear(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try{
            String sql = "DELETE FROM events;";
            stmt = connection.prepareStatement(sql);
            int num_updates = stmt.executeUpdate();
            return true;
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            return false;
        }
        finally {
            if (stmt != null) stmt.close();
        }
    }
}
