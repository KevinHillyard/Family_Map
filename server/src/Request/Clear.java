package Request;

import Service.S_Clear;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.SQLException;

/**
 * Calls the clear service for all tables.
 */
public class Clear implements HttpHandler {

    /**
     * Handles the Clear request.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        String respData = null;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                S_Clear s_clear = new S_Clear();
                success = s_clear.clearDataBase();
            }
        }
        catch (SQLException e) {
            // For clear only an internal server error is possible so set success to false.
            success = false;

            // This is the JSON data we will return in the HTTP response body if success is false.
            respData =
                    "{ " +
                            "\"message\": \"Error: Failed to clear data.\", " +
                            "\"success\": \"false\"" +
                            " }";

            // Display/log the stack trace
            e.printStackTrace();
        }
        if (!success) {
            if (respData == null) {
                respData =
                        "{ " +
                                "\"message\": \"Error: Failed to clear data.\", " +
                                "\"success\": \"false\"" +
                                " }";
            }
            // The HTTP request failed somehow, so we return a "server error"
            // status code to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            // Now that the status code and headers have been sent to the client,
            // next we send the JSON data in the HTTP response body.

            // Get the response body output stream.
            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            writeString(respData, respBody);
            // Close the output stream.  This is how Java knows we are done
            // sending data and the response is complete/
            exchange.getResponseBody().close();
        }
        else {
            // This is the JSON data we will return in the HTTP response body if success is true.
            respData =
                    "{ " +
                            "\"message\": \"Clear succeeded.\", " +
                            "\"success\": \"true\"" +
                            " }";

            // Start sending the HTTP response to the client, starting with
            // the status code and any defined headers.
            long responseLength = respData.getBytes().length;
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseLength);

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
