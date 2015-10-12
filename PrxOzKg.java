

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Proxy {
    
    public static boolean isWorking(String ip, String port){
    	try{
            Socket socket = new Socket(ip, Integer.valueOf(port));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("check");
            String response = in.readLine();
            response = in.readLine();
            if(response.equals("ack")){
                in.close();
                out.close();
                socket.close();
                return true;
            }
            return false;
        }
        catch(Exception e){
            System.out.println("server down");
            return false;
        }
    }

    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome to the Ounces (ounces) to Kilograms (kg) conversion server!");

        /* read and print the client's request */
        // readLine() blocks until the server receives a new line from client
        String userInput;
        if ((userInput = in.readLine()) == null) {
            System.out.println("Error reading message");
            out.close();
            in.close();
            clientSocket.close();
        }

        String[] tokens = userInput.split(" ");
        //to test if the related two conversion servers work
        if(tokens[0].equals("check")){
            
            if(isWorking("127.0.0.1","1234") && isWorking("127.0.0.1","2345")){
                out.println("ack");
            }
            else{
                out.println("non-ack");
            }
            out.close();
                in.close();
                clientSocket.close();
                return;
            
        }
        
        
        String inputUnit = tokens[0];
        String outputUnit = tokens[1];
        Double number = Double.parseDouble(tokens[2]);
        Double result = null;
        String result1, result2;
        String transitUnit = "lbs";
        
        if(inputUnit.equals("ounces") && outputUnit.equals("kg"))
        {
            Socket socket1 = new Socket("127.0.0.1", 1234);
            Socket socket2 = new Socket("127.0.0.1", 2345);
        }
        else if(inputUnit.equals("kg")) && outputUnit.equals("ounces")
        {
            Socket socket1 = new Socket("127.0.0.1", 2345);
            Socket socket2 = new Socket("127.0.0.1", 1234);
        }
        
        PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
        BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        out1.println(inputUnit + " " + transitUnit + " " + number.toString());
        if ((result1 = in.readLine()) == null) {
            System.out.println("Error reading message");
            out1.close();
            in1.close();
            socket1.close();
        }
        result1 = in1.readLine();
        System.out.println("received from Server 1 and the result1 is " + result1);
        
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);
        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        out2.println(transitUnit + " " + outputUnit + " " + result1);
        if ((result2 = in.readLine()) == null) {
            System.out.println("Error reading message");
            out2.close();
            in2.close();
            socket2.close();
        }
        result2 = in2.readLine();
        System.out.println("received from Server 2 result2 is " + result2);

        out.println(result2.toString());
        
           
        System.out.println("Received message: " + userInput);

        // close IO streams, then socket
        out.close();
        in.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws Exception {

        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("Usage: java ConvServer port");
        }
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Proxy Server started server on port " + port);

        // wait for connections, and process
        try {
             while(true){
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

