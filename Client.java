
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



public class Client {
	public static String host;
	public static int portNum;
	public static String parseInput(String input) throws UnknownHostException, IOException{
		String[] parsed = input.split(" ");
		Socket socket = new Socket(host,portNum);
		System.out.println("Connection to discovery server created!");
        System.out.println("");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String lookupLine = "lookup" + " " + parsed[0] + " " + parsed[1];
        String number = parsed[2];
        out.println(lookupLine);
        String response = "";
        while((response = in.readLine()) != null){
        	if(response.contains("Welcome"))
        			continue;
            String[] responseArr = response.split(" ");
            return responseArr[1] + " " + responseArr[2];
        }
        return "parseInput wrong";
		
	}
	public static String getAnswer(String ipPort, String amount, String unit1, String unit2) throws UnknownHostException, IOException{
		String[] str = ipPort.split(" ");
		String ip = str[0];
		Integer port = Integer.valueOf(str[1]);
		Socket socket = new Socket(ip,port);
		System.out.println("Connection to conversion server created!");
        System.out.println("");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(unit1 + " " + unit2 + " " + amount);
        in.readLine();
        return in.readLine();
		
	}
    public static void main(String[] args) throws UnknownHostException, IOException {
        if(args.length != 2){
            System.out.println("input invalid, parameters should be: host port");
            return; 
        }
        host = args[0];
        portNum = Integer.parseInt(args[1]);
        System.out.println("Now you can type your command...(format: <unit1> <unit2> <amount>)");
        
        try{
            String thisLine = null;
            Scanner scanner = new Scanner(System.in);
            while((thisLine = scanner.nextLine()) != null){
                // Create a socket everytime upon receiving user command
            	String[] line = thisLine.split(" ");
                String ipAndPort = parseInput(thisLine);
                System.out.println("ip and port are: " + ipAndPort);
                String answer = getAnswer(ipAndPort,line[2],line[0],line[1]);
                System.out.println("the amount is: " + answer);
                // Get response from server
                System.out.println("");
                System.out.println("Now you can type your command...");
            }
        }
        catch(Exception e){
            System.out.println("Connection error occured. Auto-connect to standby discoveryserver...");
            portNum = 22222;
            System.out.println("Now you can type your command...");
            String thisLine = null;
            Scanner scanner = new Scanner(System.in);
            while((thisLine = scanner.nextLine()) != null){
                // Create a socket everytime upon receiving user command
            	String[] line = thisLine.split(" ");
                String ipAndPort = parseInput(thisLine);
                System.out.println("ip and port are: " + ipAndPort);
                String answer = getAnswer(ipAndPort,line[2],line[0],line[1]);
                System.out.println("the amount is: " + answer);
                // Get response from server
                System.out.println("");
                System.out.println("Now you can type your command...");
                
            }
        }
    }
}
    
    




