package Request;

import Model.User;
import Service.S_LoginUser;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * Deserializes the json file into the Username and password and validates that they are correct.
 */
public class LoginUser implements HttpHandler {
    private Gson gson = new Gson();

    /**
     * Handles the Login request.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        String respData = null;
        String error_message = null;


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
            reader.close();
            isReader.close();
            inputStream.close();

            S_LoginUser s_loginUser = new S_LoginUser();
            String[] result = s_loginUser.checkCredentials(user.getUsername(), user.getPassword());
            if (result == null) {
                error_message = "Internal server error";
                success = false;
            } else if ((result.length == 1) || (result.length == 2)) {
                error_message = "Invalid Username or Password";
                success = false;
            } else if (result.length == 3) {
                success = true;
            }
            if (success) {
                respData =
                        "{ " +
                                "\"authToken\": \"" + result[0] + "\", " +
                                "\"userName\": \"" + result[1] + "\", " +
                                "\"personID\": \"" + result[2] + "\"" +
                                " }";

                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, respData.length());

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
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, respData.length());

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
