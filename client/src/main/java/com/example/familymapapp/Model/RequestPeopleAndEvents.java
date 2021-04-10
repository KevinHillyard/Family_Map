package com.example.familymapapp.Model;

public class RequestPeopleAndEvents {
    private String server_host;
    private String server_port;
    private String auth_token;
    private String person_ID;

    public void RequestPeopleAndEvents() {
        server_host = null;
        server_port = null;
        auth_token = null;
        person_ID = null;
    }

    public String getServer_host() {
        return server_host;
    }

    public void setServer_host(String server_host) {
        this.server_host = server_host;
    }

    public String getServer_port() {
        return server_port;
    }

    public void setServer_port(String server_port) {
        this.server_port = server_port;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getPerson_ID() {
        return person_ID;
    }

    public void setPerson_ID(String person_ID) {
        this.person_ID = person_ID;
    }
}
