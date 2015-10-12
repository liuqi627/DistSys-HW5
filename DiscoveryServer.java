

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.Map; 

public class DiscoveryServer {
    
    public static HashMap<String,ArrayList<String>> table = new HashMap<String,ArrayList<String>>();
    public static HashMap<String, Integer> loadBalancingTable = new HashMap<String, Integer>();
    public static int number;

    public static void process (Socket clientSocket) throws IOException {
        // open up IO streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        /* Write a welcome message to the client */
        out.println("Welcome, please enter command");

        /* read and print the client's request */
        // readLine() blocks until the server receives a new line from client
        String input;
        input = in.readLine();
        System.out.println("input is " + input);
        String a[] = input.split(" ");
        String command = a[0];
        if(command.equals("ADD") || command.equals("Add") || command.equals("add"))
        {
            //add
            String unit1 = a[1];
            String unit2 = a[2];
            String ip = a[3];
            String port = a[4];
            String key1 = unit1 + "-" + unit2;
            String key1_alter = unit2 + "-" + unit1;
            String value1 = ip + "-" + port;
            String realKey = null;
            if(table.containsKey(key1)){
            	realKey = key1;
            }
            if(table.containsKey(key1_alter)){
            	realKey = key1_alter;
            }
            if(realKey != null){
            	if(table.get(realKey).contains(value1)){
            		out.println("[FAILURE] already exists");
            		System.out.println("[FAILURE] already exists");
            	}
            	else{
            		table.get(realKey).add(value1);
            		out.println("[SUCCESS] add new server");
                    System.out.println("[SUCCESS] add new server");
            	}
            }
            else if(realKey == null){
            	ArrayList<String> al = new ArrayList<String>();
            	al.add(value1);
                table.put(key1,al);
                out.println("[SUCCESS] add new conversion type");
                System.out.println("[SUCCESS] add new conversion type");
            }
        }
        
        if(command.equals("REMOVE") || command.equals("Remove") || command.equals("remove"))
        {
            //remove
            String ip = a[1];
            String port = a[2];
            String value1 = ip + "-" + port;
            Iterator<Map.Entry<String, ArrayList<String>>> it = table.entrySet().iterator();  
            while(it.hasNext())
            { 
                Map.Entry<String, ArrayList<String>> entry=it.next();  
                ArrayList<String> value2 = entry.getValue();  
                if(value2.contains(value1) && value2.size() == 1){  
                    it.remove();        //OK   
                    out.println("[SUCCESS] remove service");
                    System.out.println("[SUCCESS] remove service");
                    out.close();
                    in.close();
                    clientSocket.close();
                    return;
                }  
                else if(value2.contains(value1) && value2.size() > 1){
                	value2.remove(value1);
                	entry.setValue(value2);
                	out.println("[SUCCESS] remove server");
                    System.out.println("[SUCCESS] remove server");
                	out.close();
                    in.close();
                    clientSocket.close();
                    return;
                }
            }
            out.println("[FAILURE] no such ip-port pairs");
            System.out.println("[FAILURE] no such ip-port pairs");
        }
        if(command.equals("LOOKUP") || command.equals("Lookup") || command.equals("lookup"))
        {
            String unit1 = a[1];
            String unit2 = a[2];
            String key1 = unit1 + "-" + unit2;
            String key1_alter = unit2 + "-" + unit1;
            String realKey = null;
            if(table.containsKey(key1)){
            	realKey = key1;
            }
            if(table.containsKey(key1_alter)){
            	realKey = key1_alter;
            }
            if(realKey != null)
            {
                String str = getServer(realKey);
                if(str.equals("AllCrashed")){
                	out.println("[FAILURE] no server responding");
                	System.out.println("[FAILURE] no server responding");
                }
                else{
                	out.println("[SUCCESS] " + str);
                    System.out.println("[SUCCESS] success lookup " + str);
                }
            }
            else
            {
                out.println("none");
                System.out.println("failed lookup");
            }
            
        }
        out.close();
        in.close();
        clientSocket.close();
    }
    
    public static boolean isWorking(String ip, String port){
    	try{
            Socket socket = new Socket(ip, Integer.valueOf(port));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("check");
            out.close();
            socket.close();
            return true;
        }
        catch(Exception e){
            System.out.println("server down");
            return false;
        }
    }
    
    public static String getServer(String str){
    	if(loadBalancingTable.containsKey(str)){
    		Integer integer = loadBalancingTable.get(str);
    		integer++;
    		loadBalancingTable.put(str, integer);
    	}
    	else{
    		Integer integer = 1;
    		loadBalancingTable.put(str, integer);
    	}
    	//round robin load balancing policy
    	int numOfServers = table.get(str).size();
    	int index = loadBalancingTable.get(str) % numOfServers;
    	for(int i=0; i<numOfServers; i++){
    		String ss[] = table.get(str).get(index).split("-");
        	if(isWorking(ss[0], ss[1])){
        		return ss[0] + " " + ss[1];
        	}
        	else{
        		index++;
        	}
    	}
    	return "AllCrashed";
    	
    	
    }


    public static void main(String[] args){

        //check if argument length is invalid
        if(args.length != 1) {
            System.err.println("port problem occured");
            System.exit(0);
        }
        
        try {
        // create socket
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);

        // wait for connections, and process
            while(true) {
                // a "blocking" call which waits until a connection is requested
                Socket clientSocket = serverSocket.accept();
                System.err.println("\nAccepted connection from client");
                process(clientSocket);
                System.err.println("this while loop is ended");
            }

        }catch (IOException e) {
            System.err.println("Connection Error");
        }
        System.exit(0);
    }
}




