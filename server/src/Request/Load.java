package Request;
import Model.Event;
import Model.Person;
import Model.User;
import Service.S_Load;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

/**
 * Deserializes the json file into the different arrays then adds the data.
 */
public class Load implements HttpHandler {
    private Gson gson = new Gson();

    private class Arrays {
        private User[] users;
        private Person[] persons;
        private Event[] events;
    }

    /**
     * Handles the Load request.
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
                //Creating an InputStream object
                InputStream inputStream = exchange.getRequestBody();
                //creating an InputStreamReader object
                InputStreamReader isReader = new InputStreamReader(inputStream);
                //Creating a BufferedReader object
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String json;
                while((json = reader.readLine())!= null){
                    sb.append(json);
                }
                Arrays data_to_add = gson.fromJson(sb.toString(), Arrays.class);

                S_Load s_load = new S_Load();
                int[] result = s_load.loadData(data_to_add.users, data_to_add.persons, data_to_add.events);

                if (result == null) {
                    throw new SQLException();
                }
                else if (result.length == 1) {
                    success = false;
                    error_message = "Invalid request data (missing values, invalid values, etc.)";
                }
                else if (result.length == 3) {
                    respData =
                            "{ " +
                                    "\"message\": \"Successfully added " + result[0] +
                                    " users, " + result[1] + " persons, and " + result[2] +
                                    " events to the database.\", " +
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
            if (!success) {
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
        catch (SQLException | IOException e) {
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
