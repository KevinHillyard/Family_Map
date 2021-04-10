package Model;



/**
 * Person model class to store all Person data while in memory.
 */
public class Person {
    private String firstName;
    private String lastName;
    private char gender;
    private String personID;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private String associatedUsername;

    void Person(){
        this.personID = null;
        this.associatedUsername = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = 'z';
        this.fatherID = null;
        this.motherID = null;
        this.spouseID = null;
    }

    /**
     *
     * @return Returns the ID.
     */
    public String getID() {
        return personID;
    }

    /**
     * Sets ID to the given ID value.
     * @param ID
     */
    public void setID(String ID) {
        this.personID = ID;
    }

    /**
     *
     * @return Returns the associated username.
     */
    public String getAssociated_username() {
        return associatedUsername;
    }

    /**
     * Sets associated_username to the given associated_username value.
     * @param associated_username
     */
    public void setAssociated_username(String associated_username) {
            this.associatedUsername = associated_username;
    }

    /**
     *
     * @return Returns the first name.
     */
    public String getFirst_name() {
        return firstName;
    }

    /**
     * Sets first_name to the given first_name value.
     * @param first_name
     */
    public void setFirst_name(String first_name) {
        this.firstName = first_name;
    }

    /**
     *
     * @return Returns the last name.
     */
    public String getLast_name() {
        return lastName;
    }

    /**
     * Sets last_name to the given last_name value.
     * @param last_name
     */
    public void setLast_name(String last_name) {
        this.lastName = last_name;
    }

    /**
     *
     * @return Returns the gender.
     */
    public char getGender() {
        return gender;
    }

    /**
     * Sets gender to the given gender value.
     * @param gender
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     *
     * @return Returns the father ID.
     */
    public String getFather_ID() {
        return fatherID;
    }

    /**
     * Sets father_ID to the given father_ID value.
     * @param father_ID
     */
    public void setFather_ID(String father_ID) {
        this.fatherID = father_ID;
    }

    /**
     *
     * @return Returns the mother ID.
     */
    public String getMother_ID() {
        return motherID;
    }

    /**
     * Sets mother_ID to the given mother_ID value.
     * @param mother_ID
     */
    public void setMother_ID(String mother_ID) {
        this.motherID = mother_ID;
    }

    /**
     *
     * @return Returns the spouse ID.
     */
    public String getSpouse_ID() {
        return spouseID;
    }

    /**
     * Sets spouse_ID to the given spouse_ID value.
     * @param spouse_ID
     */
    public void setSpouse_ID(String spouse_ID) {
        this.spouseID = spouse_ID;
    }
}
