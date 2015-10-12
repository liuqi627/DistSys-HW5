

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CvsLbsOz {

    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome to the pounds(lbs) to ounce(oz) conversion server!");

        /* read and print the client's request */
        // readLine() blocks until the server receives a new line from client
        String userInput;
        if ((userInput = in.readLine()) == null) {
            System.out.println("Error reading message");
            out.close();
            in.close();
            clientSocket.close();
            return;
        }
        System.out.println("Received message: " + userInput);
        
        //split userInput to individual items
        String[] tokens = userInput.split(" ");  
        if(tokens[0].equals("check")){
            out.println("ack");
            out.close();
            in.close();
            clientSocket.close();
            return;
        }
        
        
        //convert function
        Double msg;
        msg = ConvResult(tokens[0], tokens[2]);  
        out.println(msg.toString());
        
        // close IO streams, then socket
        out.close();
        in.close();
        clientSocket.close();
    }
    
    public static double ConvResult (String s, String amt) {
        double result;
        result = 0;
        
        if (s.equals("lbs")){
            result = Double.parseDouble(amt) * 16;
        }else if (s.equals("oz")){
            result = Double.parseDouble(amt) / 16;
        }
        
        return result;
        
    }

    public static void main(String[] args) throws Exception {

        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java ConvServer port");
        }
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);

        // wait for connections, and process
        try {
            while(true) {
                // a "blocking" call which waits until a connection is requested
                Socket clientSocket = serverSocket.accept();
                System.err.println("\nAccepted connection from client");
                process(clientSocket);
            }

        }catch (IOException e) {
            System.err.println("Connection Error");
        }
        System.exit(0);
    }
}

