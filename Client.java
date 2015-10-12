
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("input invalid, parameters should be: host port");
            return; 
        }
        String host = args[0];
        int portNum = Integer.parseInt(args[1]);
        System.out.println("Now you can type your command...(add/remove/lookup)");
        
        try{
            String thisLine = null;
            Scanner scanner = new Scanner(System.in);
            while((thisLine = scanner.nextLine()) != null){
                // Create a socket everytime upon receiving user command
                Socket socket = new Socket(host,portNum);
                System.out.println("Connection to discovery server created!");
                System.out.println("");
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println(thisLine);

                // Get response from server
                String response = "";
                while((response = in.readLine()) != null){
                	if(response.contains("Welcome"))
                			continue;
                    System.out.println(response);
                }
                System.out.println("");
                System.out.println("Now you can type your command...");
                
            }
        }
        catch(Exception e){
            System.out.println("Connection error occured. Please try again!");
			e.printStackTrace();
        }
    }
}
    
    



