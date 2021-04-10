package Request;

import Model.User;
import Service.S_Person;
import Service.ValidateAuthToken;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Locates all persons associated with the user and serializes them into json to send to client.
 */
public class Person implements HttpHandler {
    Gson gson = new Gson();
    // validate authToken.
    // if invalid then give error.
    // if valid use it to find the user to get the username.
    // use the username with S_Person to get array of persons associated with that username.

    private class AllPeople {
        private Model.Person[] data;
        private boolean success = true;
    }

    /**
     * Handles the request to get all associated persons.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        String respData = null;
        String error_message = null;

        // Determine the HTTP request type (GET, POST, etc.).
        // Only allow GET requests for this operation.
        // This operation requires a GET request, because the
        // client is "getting" information from the server, and
        // the operation is "read only" (i.e., does not modify the
        // state of the server).
        if (exchange.getRequestMethod().toLowerCase().equals("get")) {

            // Get the HTTP request headers
            Headers reqHeaders = exchange.getRequestHeaders();
            // Check to see if an "Authorization" header is present
            if (reqHeaders.containsKey("Authorization")) {

                // Extract the auth token from the "Authorization" header
                String authToken = reqHeaders.getFirst("Authorization");
                // Verify the auth token
                ValidateAuthToken validateAuthToken = new ValidateAuthToken();
                User current_user = validateAuthToken.validate(authToken);
                if (current_user != null) {
                    String username = current_user.getUsername();
                    S_Person s_person = new S_Person();
                    Model.Person[] associated_people = s_person.getPersons(username);
                    if (associated_people ==  null) {
                        error_message = "Internal server error";
                        success = false;
                    }
                    else {
                        for (int i = 0; i < associated_people.length; ++i) {
                            if (associated_people[i].getFather_ID().equals("null")) {
                                associated_people[i].setFather_ID(null);
                            }
                            if (associated_people[i].getMother_ID().equals("null")) {
                                associated_people[i].setMother_ID(null);
                            }
                            if (associated_people[i].getSpouse_ID().equals("null")) {
                                associated_people[i].setSpouse_ID(null);
                            }
                        }
                        AllPeople allPeople = new AllPeople();
                        allPeople.data = associated_people;
                        respData = gson.toJson(allPeople);

                        // Start sending the HTTP response to the client, starting with
                        // the status code and any defined headers.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        // Now that the status code and headers have been sent to the client,
                        // next we send the JSON data in the HTTP response body.

                        // Get the response body output stream.
                        OutputStream respBody = exchange.getResponseBody();
                        // Write the JSON string to the output stream.
                        writeString(respData, respBody);
                        // Close the output stream.  This is how Java knows we are done
                        // sending data and the response is complete/
                        respBody.close();

                        success = true;
                    }
                }
                else {
                    error_message = "Invalid authToken";
                    success = false;
                }

            }
        }

        if (!success) {
            if (error_message == null) {
                error_message = "Internal server error";
            }
            respData =
                    "{ " +
                            "\"message\": \"Error: " + error_message + ".\", " +
                            "\"success\":\"false\"" +
                            " }";

            // The HTTP request was invalid somehow, so we return a "bad request"
            // status code to the client.
            if (error_message.equals("Internal server error")) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            // Now that the status code and headers have been sent to the client,
            // next we send the JSON data in the HTTP response body.

            // Get the response body output stream.
            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            writeString(respData, respBody);
            // Close the output stream.  This is how Java knows we are done
            // sending data and the response is complete/
            respBody.close();
        }
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
