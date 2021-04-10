package Service;

import Data_Access.EventDao;
import Data_Access.PersonDao;
import Data_Access.UserDao;
import Model.Event;
import Model.Person;
import Model.User;
import com.google.gson.Gson;
import NamesAndLocations.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import HelpfulFunctions.RandomStringGenerator;

/**
 * Service class for the Fill request.
 * Generates Persons and Events for the specified number of generations.
 * Calls Person and Event DAO's to add these to database.
 */
public class S_Fill extends CreateConnection {
    // I need to use the username to find the personID for that user.
    // Then I check if they have a father and mother ID and if not generate them.
    // Do that for each person until I have done this for the given number of generations or the default of 4.
    private Connection connection;
    private Gson gson = new Gson();
    private Random random = new Random();
    private RandomStringGenerator rsg = new RandomStringGenerator();
    private int num_added_people = 0;
    private int num_added_events = 0;

//    private Event getPersonBirth(String personID, String associated_username) {
//        EventDao eventDao = new EventDao(this.connection);
//        try {
//            Event[] events = eventDao.findEvents(associated_username, this.connection);
//            for (int i = 0; i < events.length; ++i) {
//                if (events[i].getPerson_ID() == personID) {
//                    if (events[i].getEvent_type() == "birth") {
//                        return events[i];
//                    }
//                }
//            }
//        }
//        catch (SQLException error) {
//            error.printStackTrace();
//            return null;
//        }
//        return null;
//    }

    /**
     * Removes all associated people and events from the user.
     * @param people
     * @param events
     * @param personDao
     * @param eventDao
     * @return
     */
    private boolean removeAllAssociatedPeopleAndEvents(Person[] people, Event[] events, PersonDao personDao, EventDao eventDao) {
        try {
            for (int i = 0; i < people.length; ++i) {
                personDao.remove(people[i].getID(), this.connection);
            }
            for (int j = 0; j < events.length; ++j) {
                eventDao.remove(events[j].getID(), this.connection);
            }
            return true;
        }
        catch (SQLException error) {
            System.out.println(error.getMessage());
            return false;
        }
    }

    /**
     * creates the father.
     * @param maleNames
     * @param child_surname
     * @param child_associated_username
     * @return
     */
    private Person createFather(MaleNames maleNames, String child_surname, String child_associated_username) {
        Person father = new Person();
        father.setFirst_name(maleNames.data[random.nextInt(maleNames.data.length)]);
        father.setLast_name(child_surname);
        father.setGender('m');
        father.setAssociated_username(child_associated_username);
        father.setID(rsg.generateRandomID(father));
        return father;
    }

    /**
     * creates the mother.
     * @param femaleNames
     * @param surnames
     * @param child_associated_username
     * @return
     */
    private Person createMother(FemaleNames femaleNames, Surnames surnames, String child_associated_username) {
        Person mother = new Person();
        mother.setFirst_name(femaleNames.data[random.nextInt(femaleNames.data.length)]);
        mother.setLast_name(surnames.data[random.nextInt(surnames.data.length)]);
        mother.setGender('f');
        mother.setAssociated_username(child_associated_username);
        mother.setID(rsg.generateRandomID(mother));
        return mother;
    }

    /**
     * loads the female names from the json file.
     * @return
     */
    private FemaleNames loadFemaleNames() {
        try {
            Reader reader = new FileReader("C:\\Users\\pbhillya\\IdeaProjects\\FamilyMapServer\\fnames.json");
            FemaleNames femaleNames = gson.fromJson(reader, FemaleNames.class);
            reader.close();
            return femaleNames;
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
     * loads the males names from the json file.
     * @return
     */
    private MaleNames loadMaleNames() {
        try {
            Reader reader = new FileReader("C:\\Users\\pbhillya\\IdeaProjects\\FamilyMapServer\\mnames.json");
            MaleNames maleNames = gson.fromJson(reader, MaleNames.class);
            reader.close();
            return maleNames;
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
     * Loads the surnames from the json file.
     * @return
     */
    private Surnames loadSurnames() {
        try {
            Reader reader = new FileReader("C:\\Users\\pbhillya\\IdeaProjects\\FamilyMapServer\\snames.json");
            Surnames surnames = gson.fromJson(reader, Surnames.class);
            reader.close();
            return surnames;
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
     * Sets the birth event for the given person.
     * @param locations
     * @param personID
     * @param child_birth_year
     * @param associated_username
     * @param female
     * @return
     */
    private Event setBirth(Locations locations, String personID, int child_birth_year, String associated_username, boolean female) {
        Event birth = new Event();
        Locations location = locations.data[random.nextInt(locations.data.length)];
        birth.setCity(location.city);
        birth.setCountry(location.country);
        birth.setLatitude(location.latitude);
        birth.setLongitude(location.longitude);
        birth.setPerson_ID(personID);
        birth.setAssociated_username(associated_username);
        birth.setID(rsg.generateRandomID(null));
        birth.setEvent_type("birth");
        int birth_year = 0;
        if (female){
            birth_year = random.nextInt(50 - 13);
            birth_year = child_birth_year - birth_year - 13;
        }
        else{
            birth_year = random.nextInt(60 - 13);
            birth_year = child_birth_year - birth_year - 13;
        }
        birth.setYear(birth_year);
        return birth;
    }

    /**
     * Sets the marriage event for the husband.
     * @param locations
     * @param personID
     * @param associated_username
     * @param father_birth_year
     * @param mother_birth_year
     * @return
     */
    private Event setMarriageFather(Locations locations, String personID, String associated_username, int father_birth_year, int mother_birth_year) {
        Event marriage = new Event();
        Locations location = locations.data[random.nextInt(locations.data.length)];
        marriage.setCity(location.city);
        marriage.setCountry(location.country);
        marriage.setLatitude(location.latitude);
        marriage.setLongitude(location.longitude);
        marriage.setPerson_ID(personID);
        marriage.setAssociated_username(associated_username);
        marriage.setID(rsg.generateRandomID(null));
        marriage.setEvent_type("marriage");
        int age = random.nextInt(20);
        age = age + 15;
        int birth_year = 0;
        if (father_birth_year > mother_birth_year) birth_year = father_birth_year;
        else birth_year = mother_birth_year;
        marriage.setYear(birth_year + age);
        return marriage;
    }

    /**
     * Sets the marriage event for the wife to be the same as the husband's marriage event.
     * @param marriageFather
     * @param personID
     * @return
     */
    private Event setMarriageMother(Event marriageFather, String personID) {
        Event marriage = new Event();
        marriage.setCity(marriageFather.getCity());
        marriage.setCountry(marriageFather.getCountry());
        marriage.setLatitude(marriageFather.getLatitude());
        marriage.setLongitude(marriageFather.getLongitude());
        marriage.setPerson_ID(personID);
        marriage.setAssociated_username(marriageFather.getAssociated_username());
        marriage.setID(rsg.generateRandomID(null));
        marriage.setYear(marriageFather.getYear());
        marriage.setEvent_type("marriage");
        return marriage;
    }

    /**
     * Sets the death event for the given person.
     * @param locations
     * @param personID
     * @param associated_username
     * @param child_birth_year
     * @param person_birth_year
     * @return
     */
    private Event setDeath(Locations locations, String personID, String associated_username, int child_birth_year , int person_birth_year) {
        int min_death_age = child_birth_year - person_birth_year + 1;
        int max_death_age = 120;
        int death_age = random.nextInt(max_death_age - min_death_age);
        death_age = death_age + min_death_age;
        int death_year = person_birth_year + death_age;
        Event death = new Event();
        death.setEvent_type("death");
        death.setYear(death_year);
        death.setID(rsg.generateRandomID(null));
        death.setAssociated_username(associated_username);
        death.setPerson_ID(personID);
        Locations location = locations.data[random.nextInt(locations.data.length)];
        death.setCountry(location.country);
        death.setCity(location.city);
        death.setLatitude(location.latitude);
        death.setLongitude(location.longitude);
        return death;
    }

    /**
     * Sets all of the default events for the given person.
     * @param locations
     * @param fatherID
     * @param motherID
     * @param child_birth_year
     * @param associated_username
     * @return
     * @throws SQLException
     */
    private int[] setEvents(Locations locations, String fatherID, String motherID, int child_birth_year,
                            String associated_username) throws SQLException {
        Event fatherBirth = this.setBirth(locations, fatherID, child_birth_year, associated_username, false);
        Event motherBirth = this.setBirth(locations, motherID, child_birth_year, associated_username, true);
        Event marriageFather = this.setMarriageFather(locations, fatherID, associated_username, fatherBirth.getYear(), motherBirth.getYear());
        Event marriageMother = this.setMarriageMother(marriageFather, motherID);
        Event fatherDeath = this.setDeath(locations, fatherID, associated_username, child_birth_year, fatherBirth.getYear());
        Event motherDeath = this.setDeath(locations, motherID, associated_username, child_birth_year, motherBirth.getYear());

        EventDao eventDao = new EventDao(this.connection);
        if (eventDao.addEvent(fatherBirth, this.connection) && eventDao.addEvent(motherBirth, this.connection) &&
        eventDao.addEvent(marriageFather, this.connection) && eventDao.addEvent(marriageMother, this.connection) &&
        eventDao.addEvent(fatherDeath, this.connection) && eventDao.addEvent(motherDeath, this.connection)) {
            int[] birth_years = new int[2];
            birth_years[0] = fatherBirth.getYear();
            birth_years[1] = motherBirth.getYear();
            this.num_added_events = this.num_added_events + 6;
            return birth_years;
        }
        else {
            // 500 error;
            return null;
        }
    }

    /**
     * Recursively creates parents for the given number of generations.
     * @param femaleNames
     * @param maleNames
     * @param surnames
     * @param locations
     * @param personDao
     * @param child
     * @param child_birth_year
     * @param num_generations
     * @param completed_generations
     * @return
     * @throws SQLException
     */
    private boolean generatePersons(FemaleNames femaleNames, MaleNames maleNames, Surnames surnames, Locations locations,
                                 PersonDao personDao, Person child, int child_birth_year, int num_generations, int completed_generations) throws SQLException {
        if (completed_generations == num_generations) {
            return true;
        }
        try {
            Person father = personDao.findPersonID(child.getFather_ID(), this.connection);
            if (father == null) {
                father = this.createFather(maleNames, child.getLast_name(), child. getAssociated_username());
                child.setFather_ID(father.getID());
                Person mother = this.createMother(femaleNames, surnames, child.getAssociated_username());
                child.setMother_ID(mother.getID());
                father.setSpouse_ID(mother.getID());
                mother.setSpouse_ID(father.getID());
                int[] birth_years = this.setEvents(locations, father.getID(), mother.getID(), child_birth_year, child.getAssociated_username());
                if (birth_years == null) {
                    return false;
                }
                 if (!this.generatePersons(femaleNames, maleNames, surnames, locations,
                        personDao, father, birth_years[0], num_generations, completed_generations + 1)) {
                     return false;
                 }
                if (!this.generatePersons(femaleNames, maleNames, surnames, locations,
                        personDao, mother, birth_years[1], num_generations, completed_generations + 1)) {
                    return false;
                }
                personDao.addPerson(father, this.connection);
                personDao.addPerson(mother, this.connection);
                this.num_added_people = this.num_added_people + 2;
            }
//            else {
//                Person mother = personDao.findPersonID(child.getMother_ID(), this.connection);
//                if (!this.generatePersons(femaleNames, maleNames, surnames, locations,
//                        personDao, father, this.getPersonBirth(father.getID(), child.getAssociated_username()).getYear(),
//                        num_generations, completed_generations + 1)) {
//                    return false;
//                }
//                if (!this.generatePersons(femaleNames, maleNames, surnames, locations,
//                        personDao, mother, this.getPersonBirth(mother.getID(), child.getAssociated_username()).getYear(),
//                        num_generations, completed_generations + 1)) {
//                    return false;
//                }
//            }
        }
        catch (SQLException error) {
            // 500 error
            this.connection.rollback();
            return false;
        }
        return true;
    }

    /**
     * Fills data for the user with that username for the given number of generations.
     * @param given_username
     * @param num_generations
     * @return returns the person object for the username.
     */
    public String[] fill(String given_username, int num_generations){
        // first find the username and get the personID.
        try {
            this.openConnection();
            UserDao userDao = new UserDao(this.connection);
            User user = userDao.findUsername(given_username, this.connection);
            if (user == null) {
                // 400 error user not found
                return null;
            }

            // then load the json names and locations
            FemaleNames femaleNames = this.loadFemaleNames();
            MaleNames maleNames = this.loadMaleNames();
            Surnames surnames = this.loadSurnames();
            Locations locations = this.loadLocations();

            PersonDao personDao = new PersonDao(this.connection);
            Person child = personDao.findPersonID(user.getAssociated_personID(), this.connection);
            EventDao eventDao = new EventDao(this.connection);
            Event[] events = eventDao.findEvents(user.getUsername(), this.connection);
            Person[] people = personDao.findPersons(user.getUsername(), this.connection);
            Event child_birth_event = null;
            for (int i = 0; i < events.length; ++i) {
                if (events[i].getEvent_type().equals("birth")) {
                    if (events[i].getPerson_ID().equals(child.getID())){
                        child_birth_event = events[i];
                        break;
                    }
                }
            }
            this.removeAllAssociatedPeopleAndEvents(people, events, personDao, eventDao);
            this.connection.commit();
            eventDao.addEvent(child_birth_event, this.connection);
            this.num_added_events++;
            int child_birth_year = child_birth_event.getYear();
            int completed_generations = 0;
            if (this.generatePersons(femaleNames, maleNames, surnames, locations,
                    personDao, child, child_birth_year, num_generations, completed_generations)) {
                personDao.remove(child.getID(), this.connection);
                this.connection.commit();
                personDao.addPerson(child, this.connection);
                this.num_added_people++;
                this.connection.commit();
                String[] result = new String[3];
                result[0] = child.getID();
                result[1] = Integer.toString(this.num_added_people);
                result[2] = Integer.toString(this.num_added_events);
                return result;
            }
            else return null;
        }
        catch (SQLException error) {
            // 500 error finding the user.
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
