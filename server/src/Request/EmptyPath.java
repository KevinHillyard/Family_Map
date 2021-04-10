package Request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Default or empty path Handler.
 */
public class EmptyPath implements HttpHandler {
    private String file_name;

    /**
     * Handles the request to load the browser (blank request '/').
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        // I need to check if the url is garbage and matches anything in the Web folder.
        String curDir = System.getProperty("user.dir");
        String fullPath = Paths.get(curDir, "web", uri).toString();
        File file = new File(fullPath);
        if (!file.exists()) {
            String not_found_file_path = curDir + "/web/HTML/404.html";
            File not_found_file = new File(not_found_file_path);
            exchange.sendResponseHeaders(404, not_found_file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(not_found_file.toPath(), os);
            }
        }
        else {
            exchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
             }
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
