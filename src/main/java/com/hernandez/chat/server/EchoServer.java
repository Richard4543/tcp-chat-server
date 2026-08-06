package com.hernandez.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * A single-client echo server.
 *
 * <p>Listens on a TCP port, accepts exactly one client, writes every line it
 * receives straight back to that client, and shuts down cleanly once the client
 * disconnects. This is deliberately the smallest thing that proves the network
 * plumbing works — chat logic (multiple clients, broadcasting, nicknames)
 * arrives in later tickets.
 *
 * <p>Run it with:
 * <pre>{@code
 * java com.hernandez.chat.server.EchoServer        // listens on 5000
 * java com.hernandez.chat.server.EchoServer 6000   // listens on 6000
 * }</pre>
 */
public final class EchoServer {

    /** Port used when the caller does not supply one. */
    private static final int DEFAULT_PORT = 5000;

    /** Highest port number a TCP socket can bind to. */
    private static final int MAX_PORT = 65535;

    /** Exit status for bad command-line arguments. */
    private static final int EXIT_USAGE = 2;

    /** Exit status when the server could not run (port in use, connection lost). */
    private static final int EXIT_IO_ERROR = 1;

    /** Utility-style entry point class; not meant to be instantiated. */
    private EchoServer() {
    }

    /**
     * Starts the server, optionally on a port given as the first argument.
     *
     * @param args optional single argument: the port number to listen on
     */
    public static void main(String[] args) {
        final int port;
        try {
            port = parsePort(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Usage: java " + EchoServer.class.getName() + " [port]");
            // Non-zero tells a shell, CI job or service manager that this failed.
            // Returning normally here would report success.
            System.exit(EXIT_USAGE);
            return;
        }

        try {
            start(port);
        } catch (IOException e) {
            // The ticket asks for no stack trace on a normal failure: a readable
            // message is far more useful than 30 lines of frames.
            System.err.println("Server failed on port " + port + ": " + e.getMessage());
            System.exit(EXIT_IO_ERROR);
        }
    }

    /**
     * Works out which port to listen on from the command-line arguments.
     *
     * @param args the raw command-line arguments
     * @return the port to bind, or {@link #DEFAULT_PORT} when none was supplied
     * @throws IllegalArgumentException if too many arguments were given, or the
     *                                  argument is not a valid port number
     */
    private static int parsePort(String[] args) {
        if (args.length == 0) {
            return DEFAULT_PORT;
        }
        if (args.length > 1) {
            throw new IllegalArgumentException("expected at most one argument (the port number)");
        }

        final int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            // Re-thrown as IllegalArgumentException so callers deal with one
            // exception type; the cause is kept so nothing is lost.
            throw new IllegalArgumentException("'" + args[0] + "' is not a number", e);
        }

        if (port < 1 || port > MAX_PORT) {
            throw new IllegalArgumentException("port must be between 1 and " + MAX_PORT + ", got " + port);
        }
        return port;
    }

    /**
     * Listens on the given port, serves exactly one client, then returns.
     *
     * <p>Package-private rather than private so tests can drive it directly on
     * an arbitrary port without going through {@code main}.
     *
     * @param port the TCP port to listen on
     * @throws IOException if the port cannot be bound, or the connection fails
     *                     mid-conversation
     */
    static void start(int port) throws IOException {
        // Two nested try-with-resources rather than one: the server socket has to
        // be open (and reported) before accept() blocks, otherwise you cannot tell
        // a listening server from a hung one.
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening on port " + serverSocket.getLocalPort()
                    + " — waiting for a client...");

            try (Socket client = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(
                         client.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(
                         client.getOutputStream(), StandardCharsets.UTF_8), true)) {

                System.out.println("Client connected from " + client.getRemoteSocketAddress());
                echoUntilDisconnect(in, out);
            }
        }
        System.out.println("Client disconnected. Server shutting down.");
    }

    /**
     * Reads lines until the client hangs up, echoing each one back.
     *
     * @param in  the client's incoming lines
     * @param out the channel back to that client
     * @throws IOException if the connection drops abruptly while reading
     */
    private static void echoUntilDisconnect(BufferedReader in, PrintWriter out) throws IOException {
        String line;
        // readLine() returns null — not "" — once the client closes its end.
        // A blank line the user typed still arrives as an empty string.
        while ((line = in.readLine()) != null) {
            System.out.println("  <- " + line);
            out.println(line);
        }
    }
}
