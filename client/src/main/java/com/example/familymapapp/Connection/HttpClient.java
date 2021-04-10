package com.example.familymapapp.Connection;

import com.example.familymapapp.Model.AssociatedPeopleAndEvents;
import com.example.familymapapp.Model.EventsList;
import com.example.familymapapp.Model.LoginResult;
import com.example.familymapapp.Model.Person;
import com.example.familymapapp.Model.PersonsList;
import com.example.familymapapp.Model.RegisterInput;
import com.example.familymapapp.Model.RequestPeopleAndEvents;
import com.example.familymapapp.Model.UserModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient {
    // this guy needs to build the url from the server ip address and port that I get from login. Then creates
    // the connection and send the request.
    // take the ip_address + ':' + port_num + "/user/login"
    private Gson gson = new Gson();
    private final String USER_AGENT = "Mozilla/5.0";

    public LoginResult loginRequest(String given_IP_address, String given_port_num, String given_username, String given_password) {
        LoginResult loginResult = new LoginResult();
        String url_string = "http://" + given_IP_address + ":" + given_port_num + "/user/login";

        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(url_string);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);

            connection.setDoOutput(true);
            UserModel loginUser = new UserModel();
            loginUser.setUsername(given_username);
            loginUser.setPassword(given_password);
            String json_string = gson.toJson(loginUser);
            connection.setFixedLengthStreamingMode(json_string.length());

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);

            osw.write(json_string);
            osw.flush();
            osw.close();
            os.close();

            InputStream inputStream;
            // get result
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = connection.getInputStream();
            } else {
                /* error from server */
                inputStream = connection.getErrorStream();
            }

            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String json;
            while((json = reader.readLine())!= null){
                sb.append(json);
            }
            loginResult = gson.fromJson(sb.toString(), LoginResult.class);

            reader.close();
            isReader.close();
            inputStream.close();
            return loginResult;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LoginResult registerRequest(RegisterInput input) {
        LoginResult loginResult = new LoginResult();
        String url_string = "http://" + input.getIP_address() + ":" + input.getPort_num() + "/user/register";

        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(url_string);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);

            connection.setDoOutput(true);
            UserModel registerUser = new UserModel();
            registerUser.setUsername(input.getUsername());
            registerUser.setPassword(input.getPassword());
            registerUser.setFirstName(input.getFirst_name());
            registerUser.setLastName(input.getLast_name());
            registerUser.setEmail(input.getEmail());
            registerUser.setGender(input.getGender());
            String json_string = gson.toJson(registerUser);
            connection.setFixedLengthStreamingMode(json_string.length());

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);

            osw.write(json_string);
            osw.flush();
            osw.close();
            os.close();

            InputStream inputStream;
            int responseCode = connection.getResponseCode();
            // get result
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = connection.getInputStream();
            }
            else {
                /* error from server */
                inputStream = connection.getErrorStream();
            }

            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String json;
            while((json = reader.readLine())!= null){
                sb.append(json);
            }
            loginResult = gson.fromJson(sb.toString(), LoginResult.class);

            reader.close();
            isReader.close();
            inputStream.close();
            return loginResult;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AssociatedPeopleAndEvents loadPeopleAndEvents(RequestPeopleAndEvents requestData) {
        AssociatedPeopleAndEvents result = new AssociatedPeopleAndEvents();
        PersonsList personsList = new PersonsList();
        EventsList eventsList = new EventsList();

        try {
            String person_url_string = "http://" + requestData.getServer_host() + ":" + requestData.getServer_port() + "/person";
            URL person_url;
            HttpURLConnection person_connection;
            person_url = new URL(person_url_string);
            person_connection = (HttpURLConnection) person_url.openConnection();
            person_connection.setRequestMethod("GET");
            person_connection.setRequestProperty("User-Agent", USER_AGENT);
            person_connection.setRequestProperty("Authorization", requestData.getAuth_token());
            person_connection.setRequestProperty("Accept", "application/json");
            int response_code_person = person_connection.getResponseCode();

            InputStream inputStream;
            // get result
            if (person_connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = person_connection.getInputStream();
            } else {
                /* error from server */
                inputStream = person_connection.getErrorStream();
            }
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String json;
            while((json = reader.readLine())!= null){
                sb.append(json);
            }
            personsList = gson.fromJson(sb.toString(), PersonsList.class);

            reader.close();
            isReader.close();
            inputStream.close();

            result.setPersons(personsList);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            String event_url_string = "http://" + requestData.getServer_host() + ":" + requestData.getServer_port() + "/event";
            URL event_url;
            HttpURLConnection event_connection;
            event_url = new URL(event_url_string);
            event_connection = (HttpURLConnection) event_url.openConnection();
            event_connection.setRequestMethod("GET");
            event_connection.setRequestProperty("User-Agent", USER_AGENT);
            event_connection.setRequestProperty("Authorization", requestData.getAuth_token());
            event_connection.setRequestProperty("Accept", "application/json");
            int response_code = event_connection.getResponseCode();

            InputStream inputStream;
            // get result
            if (event_connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = event_connection.getInputStream();
            } else {
                /* error from server */
                inputStream = event_connection.getErrorStream();
            }
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String json;
            while((json = reader.readLine())!= null){
                sb.append(json);
            }
            eventsList = gson.fromJson(sb.toString(), EventsList.class);

            reader.close();
            isReader.close();
            inputStream.close();

            result.setEvents(eventsList);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public Person getPersonWithID(String auth_token, String server_host, String server_port, String person_ID) {
        Person result = new Person();

        try {
            String person_url_string = "http://" + server_host + ":" + server_port + "/person/" + person_ID;
            URL person_url;
            HttpURLConnection person_connection;
            person_url = new URL(person_url_string);
            person_connection = (HttpURLConnection) person_url.openConnection();
            person_connection.setRequestMethod("GET");
            person_connection.setRequestProperty("User-Agent", USER_AGENT);
            //person_connection.setDoOutput(true);
            person_connection.setRequestProperty("Authorization", auth_token);
            person_connection.setRequestProperty("Accept", "application/json");
            int response_code = person_connection.getResponseCode();

            InputStream inputStream;
            // get result
            if (response_code < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = person_connection.getInputStream();
            } else {
                /* error from server */
                inputStream = person_connection.getErrorStream();
            }
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String json;
            while((json = reader.readLine())!= null){
                sb.append(json);
            }
            result = gson.fromJson(sb.toString(), Person.class);

            reader.close();
            isReader.close();
            inputStream.close();

            return result;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
