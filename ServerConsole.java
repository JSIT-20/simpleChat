import java.io.*;
import java.util.Scanner;
import client.*;
import common.*;


public class ServerConsole implements ChatIF {

	final public static int DEFAULT_PORT = 5555;
	EchoServer server;
	Scanner fromConsole;
	boolean stop;
	
	public ServerConsole(int port) {
	    try 
	    {
	      server= new EchoServer(port);
	      server.listen();
	      stop = false;
	      
	    } 
	    catch(IOException exception) 
	    {
	      System.out.println("Error: Can't setup connection!"
	                + " Terminating Server.");
	      System.exit(1);
	    }
	    fromConsole = new Scanner(System.in);
	}
	
	  public void accept() 
	  {
	    try
	    {

	      String message;
	      
	      //modified from true to client.isConnected()
	      while (!stop) 
	      {
	        message = fromConsole.nextLine();
	        String word = message.substring(0,1);
	    	String hash = "#";
	    	if(word.equals(hash)) {
	    		if(message.equals("quit")) {
	    			stop = true;
	    		}
	    		server.handleCommands(message);
	    	}
	    	else {
	    		server.sendToAllClients("SERVER MSG> " + message);
	    	}
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	  
	  
	  
	  public void display(String message) {
		  System.out.println("> " + message);
	  }
	  public static void main(String[] args) 
	  {
		  //main now takes two arguments first for host name and second for port
		  //default is still DEFAULT_PORT
	    int port = 0;

	    try
	    {
	      port = Integer.parseInt(args[0]);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      port = DEFAULT_PORT;
	    }
	    ServerConsole chat= new ServerConsole(port);
	    chat.accept();  //Wait for console data
	  }
}
