package Data_Access;

public class CreateTableStatements {
    /**
     * Gets the SQL statement to create the AuthToken table.
     * @return returns that statement
     */
    public String getCreate_authToken_table() {
        return create_authToken_table;
    }

    /**
     * Gets the SQL statement to create the Event table.
     * @return returns that statement
     */
    public String getCreate_event_table() {
        return create_event_table;
    }

    /**
     * Gets the SQL statement to create the Person table.
     * @return returns that statement
     */
    public String getCreate_person_table() {
        return create_person_table;
    }

    /**
     * Gets the SQL statement to create the User table.
     * @return returns that statement
     */
    public String getCreate_user_table() {
        return create_user_table;
    }

    private final String create_authToken_table = "create table if not exists auth_token ( "
            + "token varchar(255) not null primary key, "
            + "associated_user_ID varchar(255) not null)";

    private final String create_event_table = "create table if not exists events ( " +
            "id varchar(255) not null primary key, " +
            "associated_username varchar(255) not null, " +
            "associated_person_ID varchar(255) not null, " +
            "latitude double not null, " +
            "longitude double not null, " +
            "country varchar(255) not null, " +
            "city varchar(255) not null, " +
            "year integer not null, " +
            "event_type varchar(255) not null)";

    private final String create_person_table = "create table if not exists person ( " +
            "id varchar(255) not null primary key," +
            "associated_username varchar(255) not null," +
            "first_name varchar(255) not null," +
            "last_name varchar(255) not null," +
            "gender varchar(32)," +
            "father_id varchar(255)," +
            "mother_id varchar(255)," +
            "spouse_id varchar(255)," +
            "constraint ck_gender check (gender in ('f', 'm')))";

    private final String create_user_table = "create table if not exists user ( " +
            "id varchar(255) not null primary key, " +
            "username varchar(255) not null, " +
            "password varchar(255) not null, " +
            "email varchar(255) not null, " +
            "first_name varchar(255) not null, " +
            "last_name varchar(255) not null, " +
            "gender varchar(32), " +
            "associated_personID varchar(255) not null, " +
            "constraint unique_username unique (username), " +
            "constraint ck_gender check (gender in ('f', 'm')) );";
}
