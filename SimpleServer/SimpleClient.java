/******
 * SimpleClient
 * Author: Christian Duncan
 *
 * This program illustrates a simple client that transmits a message
 * back and forth to a server.
 ******/
import java.net.*;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;
import java.util.Scanner;

public class SimpleClient {
    private String hostname;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    /**
     * Constructor
     * @param hostname The name of the machine to connect to
     **/
    public SimpleClient(String hostname) {
	this.hostname = hostname;
    }

    /**
     * Start running the thread for this connection
     **/
    public void run() {
	try {
	    // Establish connection with the Simple Server
	    socket = new Socket(hostname, SimpleServer.PORT);
	    out = new PrintWriter(socket.getOutputStream(), true);
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    banter();
	} catch (UnknownHostException e) {
	    System.err.println("Unknown host: " + hostname);
	    System.err.println("             " + e.getMessage());
	} catch (IOException e) {
	    System.err.println("IO Error: Error establishing communication with server.");
	    System.err.println("          " + e.getMessage());
	} 

	try {
	    // Clean up the streams by closing them
	    if (out != null) out.close();
	    if (in != null) in.close();
	    if (socket != null) socket.close();
	} catch (IOException e) { 
	    System.err.println("Error closing the streams.");
	} 
    }

    public void banter() throws IOException {
	String messageIn = null;
	String messageOut = "Hello there!";
	System.out.println(" Sending: " + messageOut);
	out.println(messageOut);
	messageIn = in.readLine();
	System.out.println("Received: " + messageIn);
	messageOut = "I know.  But do you know I know?";
	System.out.println(" Sending: " + messageOut);
	out.println(messageOut);
	while (true) {
	    messageIn = in.readLine();
	    System.out.println("Received: " + messageIn);
	    String knowledge = messageIn.substring(0, messageIn.indexOf('.')) + " I know";
	    messageOut = knowledge + ". But do you know " + knowledge + "?";
	    System.out.println(" Sending: " + messageOut);
	    out.println(messageOut);

	    // Rest a second
	    try { Thread.sleep(3000); } catch (InterruptedException e) { }
	}	    
    }

    /**
     * The main entry point.
     **/
    public static void main(String[] args) {
	String hostname = "localhost";
	if (args.length > 0) { hostname = args[0]; }
	SimpleClient c = new SimpleClient(hostname);
	c.run();
    }
}
