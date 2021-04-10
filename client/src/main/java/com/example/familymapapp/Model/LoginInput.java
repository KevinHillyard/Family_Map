package com.example.familymapapp.Model;

public class LoginInput {
    String IP_address;
    String port_num;
    String username;
    String password;

    public void LoginInput() {
        IP_address = null;
        port_num = null;
        username = null;
        password = null;
    }

    public String getIP_address() {
        return IP_address;
    }

    public void setIP_address(String IP_address) {
        this.IP_address = IP_address;
    }

    public String getPort_num() {
        return port_num;
    }

    public void setPort_num(String port_num) {
        this.port_num = port_num;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
