package Request;

import Model.Person;
import Model.User;
import Service.GetUsersPersonID;
import Service.S_Fill;
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
 * Gets the Username and/or number of generations from the URL path and generates data for that user.
 */
public class Fill implements HttpHandler {

    /**
     * Handles the fill request.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        String respData = null;
        String error_message = null;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                URI uri = exchange.getRequestURI();
                String url = uri.getPath();
                if (!Character.isDigit(url.charAt(url.length() -1))) {
                    url = url + '/';
                }

                String pattern = "\\/fill\\/(.+)\\/(\\d+)?";

                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(url);
                String username = null;
                int num_generations = 4;
                if (m.find()) {
                    username = m.group(1);
                    if (m.group(2) != null) {
                        num_generations = Integer.parseInt(m.group(2));
                    }
                }
                if (username == null) {
                    throw new IOException();
                }

                GetUsersPersonID getUsersPersonID = new GetUsersPersonID();
                String ID = getUsersPersonID.getUsersPersonID(username);
                if ((ID != null) && (!ID.equals("not_found")) && (num_generations > 0)) {

                    // I have the username and the number of generations to fill.
                    S_Fill s_fill = new S_Fill();
                    String[] result = s_fill.fill(username, num_generations);
                    if (result != null) {

                        // This is the JSON data we will return in the HTTP response body
                        respData =
                                "{ " +
                                        "\"message\": \"Successfully added " + result[1] +
                                        " persons and " + result[2] + " events to the database.\", " +
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
                    else {
                        throw new IOException();
                    }
                }
                else if ((!ID.equals(null)) && (num_generations > 0)) {
                    if (ID.equals("not_found")) {
                        error_message = "Invalid Username";
                    }
                }
                else if (num_generations <= 0) {
                    error_message = "Invalid number of generations";
                }
                else if (ID.equals(null)) {
                    throw new IOException();
                }
            }

            if (!success) {
                if (error_message == null) {
                    error_message = "Error filling data. Please check parameters";
                }
                respData =
                        "{ " +
                                "\"message\": \"Error: " + error_message + ".\", " +
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
        catch (IOException e) {
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
