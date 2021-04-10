package Server;

import Request.*;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

/**
 * HTTP server.
 */
public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    // This method initializes and runs the server.
    // The "portNumber" parameter specifies the port number on which the
    // server should accept incoming client connections.
    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server");

        try {

            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");

        // Create and install the HTTP handler for the "/user/register" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/user/register" URL path, it will forward the request to RegisterUserRequest
        // for processing.
        server.createContext("/user/register", new RegisterUser());

        // Create and install the HTTP handler for the "/user/login" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/user/login" URL path, it will forward the request to LoginUser handler
        // for processing.
        server.createContext("/user/login", new LoginUser());

        // Create and install the HTTP handler for the "/clear" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/clear" URL path, it will forward the request to Clear handler
        // for processing.
        server.createContext("/clear", new Clear());

        // Create and install the HTTP handler for the "/fill" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/fill" URL path, it will forward the request to Fill handler
        // for processing.
        server.createContext("/fill/", new Fill());

        // Create and install the HTTP handler for the "/load" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/load" URL path, it will forward the request to Load handler
        // for processing.
        server.createContext("/load", new Load());

        // Create and install the HTTP handler for the "/person" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/person" URL path, it will forward the request to Person handler
        // for processing.
        server.createContext("/person", new Person());

        // Create and install the HTTP handler for the "/person/" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/person/" URL path, it will forward the request to PersonWithID handler
        // for processing.
        server.createContext("/person/", new PersonWithID());

        // Create and install the HTTP handler for the "/event" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/event" URL path, it will forward the request to Event handler
        // for processing.
        server.createContext("/event", new Event());

        // Create and install the HTTP handler for the "/event/" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/event/" URL path, it will forward the request to EventWithID handler
        // for processing.
        server.createContext("/event/", new EventWithID());

        // Create and install the HTTP handler for the "/" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/" URL path, it will forward the request to EmptyPath handler
        // for processing.
        server.createContext("/", new EmptyPath());

        // Log message indicating that the HttpServer is about the start accepting
        // incoming client connections.
        System.out.println("Starting server");

        // Tells the HttpServer to start accepting incoming client connections.
        // This method call will return immediately, and the "main" method
        // for the program will also complete.
        // Even though the "main" method has completed, the program will continue
        // running because the HttpServer object we created is still running
        // in the background.
        server.start();

        // Log message indicating that the server has successfully started.
        System.out.println("Server started");
    }

    // "main" method for the server program
    // "args" should contain one command-line argument, which is the port number
    // on which the server should accept incoming client connections.
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
