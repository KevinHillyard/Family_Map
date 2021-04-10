package Service;

import Data_Access.PersonDao;
import Model.Person;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Service class to find Person with specific ID.
 * Calls Person DAO to find person with matching ID.
 */
public class S_PersonWithID extends CreateConnection {
    private Connection connection = null;

    /**
     * Gets the person that matches the ID then returns it.
     * @param ID
     * @return Returns the person matching the ID.
     */
    public Person findPerson(String ID) throws SQLException {
        PersonDao personDao = new PersonDao();
        Person person;
        this.openConnection();

        try {
            person = personDao.findPersonID(ID, this.connection);
            return person;
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
