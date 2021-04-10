package com.example.familymapapp.Model;

/**
 * Event model class to store all Event data while in memory.
 */
public class Event {
    private String eventID;
    private String associatedUsername;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private int year;
    private String eventType;

    void Event(){
        this.eventID = null;
        this.associatedUsername = null;
        this.personID = null;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.country = null;
        this.city = null;
        this.year = 0;
        this.eventType = null;
    }

    /**
     *
     * @return Returns the ID.
     */
    public String getID() {
        return eventID;
    }

    /**
     * Sets ID to the given ID value.
     * @param ID
     */
    public void setID(String ID) {
        this.eventID = ID;
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
     * @return Returns the person ID.
     */
    public String getPerson_ID() {
        return personID;
    }

    /**
     * Sets person_ID to the given person_ID value.
     * @param person_ID
     */
    public void setPerson_ID(String person_ID) {
        this.personID = person_ID;
    }

    /**
     *
     * @return Returns the latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude to the given latitude value.
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return Returns the longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude to the given longitude value.
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return Returns the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets county to the given county value.
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return Returns the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets city to the given city value.
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return Returns the year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets year to the given year value.
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     *
     * @return Returns the event type.
     */
    public String getEvent_type() {
        return eventType;
    }

    /**
     * Sets event_type to the given event_type value.
     * @param event_type
     */
    public void setEvent_type(String event_type) {
        this.eventType = event_type;
    }
}

