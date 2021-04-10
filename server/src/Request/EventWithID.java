package Request;

import Model.Event;
import Model.User;
import Service.S_EventWithID;
import Service.ValidateAuthToken;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gets the ID from the URL path and finds the event with the ID.
 */
public class EventWithID implements HttpHandler {

    /**
     * Handles the EventWithID request.
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
                        String pattern = "\\/event\\/(.+)";

                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(url);
                        String eventID = null;
                        if (m.find()) {
                            System.out.println(m.group(0));
                            System.out.println(m.group(1));
                            eventID = m.group(1);
                        }
                        if (eventID == null) {
                            Request.Event eventHandler = new Request.Event();
                            eventHandler.handle(exchange);
                            return;
                        }

                        // I have the ID for the event in question so now find and return that ID.
                        S_EventWithID s_eventWithID = new S_EventWithID();
                        Event found_event = s_eventWithID.findEvent(eventID);
                        if (found_event != null) {
                            if (found_event.getAssociated_username().equals(current_user.getUsername())) {

                                // This is the JSON data we will return in the HTTP response body
                                respData =
                                        "{ " +
                                                "\"associatedUsername\": \"" + found_event.getAssociated_username() + "\", " +
                                                "\"eventID\": \"" + found_event.getID() + "\", " +
                                                "\"personID\": \"" + found_event.getPerson_ID() + "\", " +
                                                "\"latitude\": \"" + found_event.getLatitude() + "\", " +
                                                "\"longitude\": \"" + found_event.getLongitude() + "\", " +
                                                "\"country\": \"" + found_event.getCountry() + "\", " +
                                                "\"city\": \"" + found_event.getCity() + "\", " +
                                                "\"eventType\": \"" + found_event.getEvent_type() + "\", " +
                                                "\"year\": \"" + found_event.getYear() + "\", " +
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
                                "\"message\": \"Error: Invalid request type, Authtoken, or EventID.\", " +
                                "\"success\":\"false\"" +
                                " }";

                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

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
