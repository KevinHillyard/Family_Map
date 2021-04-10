package com.example.familymapapp.Model;

public class EventsList {
    private Event[] data;

    public void EventsList() {
        data = null;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }
}
