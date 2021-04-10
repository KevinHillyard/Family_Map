package com.example.familymapapp.Model;

public class AssociatedPeopleAndEvents {
    private PersonsList persons;
    private EventsList events;

    public void AssociatedPeopleAndEvents() {
        persons = null;
        events = null;
    }

    public PersonsList getPersons() {
        return persons;
    }

    public void setPersons(PersonsList persons) {
        this.persons = persons;
    }

    public EventsList getEvents() {
        return events;
    }

    public void setEvents(EventsList events) {
        this.events = events;
    }
}
