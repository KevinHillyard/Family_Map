package Request;

import Model.Person;
import Model.User;
import Service.S_PersonWithID;
import Service.ValidateAuthToken;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gets the ID from the URL path and finds the person with the ID.
 */
public class PersonWithID implements HttpHandler {

    /**
     * Handles the PersonWithID request.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        String respData = null;

        try {
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
                        URI uri = exchange.getRequestURI();
                        String url = uri.getPath();
                        String pattern = "\\/person\\/(.+)";

                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(url);
                        String personID = null;
                        if (m.find()) {
                            personID = m.group(1);
                        }
                        if (personID == null) {
                            Request.Person personHandler = new Request.Person();
                            personHandler.handle(exchange);
                            return;
                        }

                        // I have the ID for the person in question so now find and return that ID.
                        S_PersonWithID s_personWithID = new S_PersonWithID();
                        Person found_person = s_personWithID.findPerson(personID);
                        if (found_person != null) {
                            if (found_person.getAssociated_username().equals(current_user.getUsername())) {

                                // This is the JSON data we will return in the HTTP response body
                                respData =
                                        "{ " +
                                                "\"associatedUsername\": \"" + found_person.getAssociated_username() + "\", " +
                                                "\"personID\": \"" + found_person.getID() + "\", " +
                                                "\"firstName\": \"" + found_person.getFirst_name() + "\", " +
                                                "\"lastName\": \"" + found_person.getLast_name() + "\", " +
                                                "\"gender\": \"" + found_person.getGender() + "\", " +
                                                "\"fatherID\": \"" + found_person.getFather_ID() + "\", " +
                                                "\"motherID\": \"" + found_person.getMother_ID() + "\", " +
                                                "\"spouseID\": \"" + found_person.getSpouse_ID() + "\", " +
                                                "\"success\":\"true\"" +
                                                " }";

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
                    }
                }
            }

            if (!success) {
                respData =
                        "{ " +
                                "\"message\": \"Error: Invalid request type Authtoken or PersonID.\", " +
                                "\"success\":\"false\"" +
                                " }             ";

                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                long length = respData.getBytes().length;
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, length);

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
        catch (IOException | SQLException e) {
            respData =
                    "{ " +
                            "\"message\": \"Error: Internal Server Error.\", " +
                            "\"success\":\"false\"" +
                            " }";
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // Get the response body output stream.
            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            writeString(respData, respBody);
            // Close the output stream.  This is how Java knows we are done
            // sending data and the response is complete/
            respBody.close();

            // Display/log the stack trace
            e.printStackTrace();
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
