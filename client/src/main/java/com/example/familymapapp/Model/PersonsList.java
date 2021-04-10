package com.example.familymapapp.Model;

public class PersonsList {
    private Person[] data;

    public void PersonsList() {
        data = null;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }
}
