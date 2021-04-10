package Request;

import Model.AuthToken;
import Model.User;
import Service.GetUsersPersonID;
import Service.S_Load;
import Service.S_LoginUser;
import Service.S_RegisterUser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;


/**
 * Deserializes the json file into the User variables and
 * adds them to the database if the username is not a duplicate.
 */
public class RegisterUser implements HttpHandler {
    private Gson gson = new Gson();

    /**
     * Handles the register User request.
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
                User user = gson.fromJson(sb.toString(), User.class);

                S_RegisterUser s_registerUser = new S_RegisterUser();
                AuthToken authToken = s_registerUser. registerUser(user.getUsername(), user.getPassword(), user.getEmail(),
                        user.getFirst_name(), user.getLast_name(), user.getGender());
                if (authToken != null) {
                    success = true;
                    if (authToken.getToken() == "duplicate") {
                        error_message = "Duplicate Username. Please choose another Username";
                        success = false;
                    }
                    else if (authToken.getToken() == "incorrect_parameters") {
                        error_message = "All required information not set correctly";
                        success = false;
                    }
                    if (success) {
                        GetUsersPersonID getUsersPersonID = new GetUsersPersonID();
                        String personID = getUsersPersonID.getUsersPersonID(user.getUsername());
                        S_LoginUser s_loginUser = new S_LoginUser();
                        String[] login_values = s_loginUser.checkCredentials(user.getUsername(), user.getPassword());

                        respData =
                                "{ " +
                                        "\"authToken\": \"" + login_values[0] + "\", " +
                                        "\"userName\": \"" + user.getUsername() + "\", " +
                                        "\"personID\": \"" + personID + "\"" +
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

            if (!success) {
                if (error_message == null) {
                    error_message = "Invalid request type";
                }
                respData =
                        "{ " +
                                "\"message\": \"Error: " + error_message + ".\", " +
                                "\"success\":\"false\"" + " " +
                                "}";

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
        catch (JsonSyntaxException e) {
            respData =
                    "{ " +
                            "\"message\": \"Error: Internal Server Error.\", " +
                            "\"success\":\"false\"" + " " +
                            "}";
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
