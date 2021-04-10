package Data_Access;

import Model.Person;

import java.sql.*;
import java.util.Vector;

/**
 * Person DAO class to access the database regarding all Person data.
 */
public class PersonDao {

    public PersonDao(){

    }

    /**
     * Constructor that takes a connection parameter and creates the table if it does not already exist.
     * @param connection
     */
    public PersonDao(Connection connection) {
        CreateTableStatements createTableStatements = new CreateTableStatements();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createTableStatements.getCreate_person_table());
        }
        catch (SQLException error) {
            System.out.println("Error Setting Table");
            System.out.println(error.getMessage());
        }
    }

    /**
     * Adds the person obejct's data to the database
     * @param person
     * @return Return true if added else false.
     */
    public boolean addPerson(Person person, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String personID = person.getID();
            String uname = person.getAssociated_username();
            String fname = person.getFirst_name();
            String lname = person.getLast_name();
            String gender = Character.toString(person.getGender());
            String father_ID = person.getFather_ID();
            String mother_ID = person.getMother_ID();
            String spouse_ID = person.getSpouse_ID();
            String sql = "INSERT INTO person (id, associated_username, first_name, last_name, gender, father_ID, mother_ID, spouse_ID) " +
                    "VALUES ('" + personID + "', '" + uname + "', '" + fname + "', '" +
                    lname + "', '" + gender + "', '" + father_ID + "', '" + mother_ID + "', '" + spouse_ID + "');";
            stmt = connection.prepareStatement(sql);

            if (stmt.executeUpdate() != 1) {
                return false;
            }
        }
        catch (SQLException e){
            System.out.println("Error inserting test user into table.");
            System.out.println(e.getMessage());
            return false;
        }
        finally {
            if (stmt != null) stmt.close();
        }
        return true;
    }

    /**
     * Looks in the Person table for the person ID.
     * @param ID
     * @param connection
     * @return Returns true if found
     */
    public Person findPersonID(String ID, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Person person = new Person();
        try {
            String sql = "select * " +
                    "from person " +
                    "where id = '" + ID + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                person.setID(rs.getString(1));
                person.setAssociated_username(rs.getString(2));
                person.setFirst_name(rs.getString(3));
                person.setLast_name(rs.getString(4));
                char gender = rs.getString(5).charAt(0);
                person.setGender(gender);
                person.setFather_ID(rs.getString(6));
                person.setMother_ID(rs.getString(7));
                person.setSpouse_ID(rs.getString(8));
            }
        }

        catch (SQLException error) {
            System.out.println(error.getMessage());
            return null;
        }
        finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        if (person.getID() == null){
            return null;
        }
        return person;
    }


    /**
     * Finds all persons associated to the username.
     * @param associated_username
     * @return Returns an array of people associated with the username.
     */
    public Person[] findPersons(String associated_username, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector<Person> found_persons_vector = new Vector<>();
        try {
            String sql = "select * " +
                    "from person " +
                    "where associated_username = '" + associated_username + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                Person found_personage = new Person();
                found_personage.setID(rs.getString(1));
                found_personage.setAssociated_username(rs.getString(2));
                found_personage.setFirst_name(rs.getString(3));
                found_personage.setLast_name(rs.getString(4));
                char gender = rs.getString(5).charAt(0);
                found_personage.setGender(gender);
                found_personage.setFather_ID(rs.getString(6));
                found_personage.setMother_ID(rs.getString(7));
                found_personage.setSpouse_ID(rs.getString(8));
                found_persons_vector.add(found_personage);
            }
        }

        catch (SQLException error) {
            System.out.println(error.getMessage());
            return null;
        }
        finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        if (found_persons_vector.size() == 0) {
            return null;
        }
        Person[] found_persons_array = new Person[found_persons_vector.size()];
        for (int i = 0; i < found_persons_vector.size(); ++i){
            found_persons_array[i] = found_persons_vector.elementAt(i);
        }
        return found_persons_array;
    }

    /**
     * Removes the person with the matching ID.
     * @param ID
     * @param connection
     * @return Returns true if removed else false.
     */
    public boolean remove(String ID, Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM person WHERE id = '" + ID + "';";
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
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public boolean clear(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try{
            String sql = "DELETE FROM person;";
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
