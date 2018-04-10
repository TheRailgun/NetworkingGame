/******
 * SimpleServer
 * Author: Christian Duncan
 *
 * This program illustrates a simple server that transmits a message
 * back and forth to a client.
 ******/
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class SimpleServer {
    public static final int PORT = 1984;  // Hard-coded port
    private ServerSocket serverSocket = null;
    private Socket socket = null;      // The socket for a given client
    private PrintWriter out = null;    // Used to send information TO the client
    private BufferedReader in = null;  // Used to receive info FROM the client
	
    /**
     * Basic Constructor 
     **/
    public SimpleServer() { }

    /**
     * Run the main server... just listen for and create connections
     **/
    public void run() {
	System.out.println("Simple Server: Starting up!");

	try {
	    // Create a server socket bound to the given port
	    serverSocket = new ServerSocket(PORT);
	    
	    while (true) {
                // Keep processing new connections (but one at a time!)
		try {
		    // Once a request is received, accept, and get the I/O streams.
		    Socket socket = serverSocket.accept();
		    //   in -- Used to read input from client
		    //   out -- Used to send output to client
		    out = new PrintWriter(socket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    banter();  // Banter with the client a bit.
		} catch (IOException e) {
		    System.err.println("I/O Error: Terminating connection.");
		    System.err.println("  Message: " + e.getMessage());
		}
	    
		// Close the socket
		try {
		    if (in != null) in.close();
		    if (out != null) out.close();
		    if (socket != null) socket.close();
		} catch (IOException e) {
		    System.err.println("Error trying to close socket. " + e.getMessage());
		}
	    }
	} catch (Exception e) {
	    System.err.println("ABORTING: " + e.getMessage());
	    System.exit(1);
	}
    }

    // Communicate with the client
    public void banter() throws IOException {
	String messageIn = in.readLine();
	System.out.println("Received: " + messageIn);
	String messageOut = "Thank you for contacting me.  You are awesome.";
	System.out.println(" Sending: " + messageOut);
	out.println(messageOut);
	while (true) {
	    messageIn = in.readLine();
	    System.out.println("Received: " + messageIn);
	    String knowledge = messageIn.substring(0, messageIn.indexOf('.')) + " you know";
	    messageOut = knowledge + ". But do you know " + knowledge + "?";
	    System.out.println(" Sending: " + messageOut);
	    out.println(messageOut);
	}
    }

    /**
     * The main entry point.  It just processes the command line argument
     * and starts an instance of the SimpleServer running.
     **/
    public static void main(String[] args) {
	SimpleServer s = new SimpleServer();
	s.run();
    }
}
