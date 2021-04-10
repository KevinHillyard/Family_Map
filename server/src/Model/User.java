package Model;

/**
 * User model class to store all User data while in memory.
 */
public class User {
    private String ID;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private char gender;
    private String personID;

    void User(){
        this.ID = null;
        this.userName = null;
        this.password = null;
        this.email = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = 'z';
        this.personID = null;
    }

    /**
     *
     * @return Returns the ID.
     */
    public String getID() {
        return ID;
    }

    /**
     * Sets ID to to the given ID param value.
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     *
     * @return Returns the Username.
     */
    public String getUsername() {
        return userName;
    }

    /**
     * Sets user_name to the given user_name value.
     * @param user_name
     */
    public void setUsername(String user_name) {
        this.userName = user_name;
    }

    /**
     *
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password to the given password value.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets eamil to the given email value.
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
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

    public String getAssociated_personID() {
        return personID;
    }

    public void setAssociated_personID(String associated_personID) {
        this.personID = associated_personID;
    }
}
