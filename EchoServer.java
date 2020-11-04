// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	
	if(((String)msg).length() > 7 && ((String)msg).substring(0,1).equals("#")) {
		if(((String)msg).substring(0,6).equals("#login")) {
			if((Boolean)client.getInfo("justConnected")) {
				client.setInfo("loginId", (((String)msg).split(" "))[1]);
				System.out.println("Message received: " + msg + " from null.");
				System.out.println(client.getInfo("loginId") + " has logged on.");
				sendToAllClients(client.getInfo("loginId") + " has logged on.");
				client.setInfo("justConnected", false);
			}
			else {
				System.out.println("You have already logged in, ending connection");
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	else {
	    System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));
	    this.sendToAllClients(client.getInfo("loginId") + ": " + msg);
	}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client is attempting to connnect to the server.");
	  client.setInfo("justConnected", true);
  }
  
  synchronized protected void clientDisconnected(
		    ConnectionToClient client) {
			  System.out.println(client.getInfo("loginId") + " has disconnected");
			  sendToAllClients(client.getInfo("loginId") + " has disconnected");
		  }
  
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	  clientDisconnected(client);
  }
  
  public void handleCommands(String message) {
	  if(message.equals("#quit")) {
		  try {
			close();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  else if(message.equals("#stop")) {
		  stopListening();
		  sendToAllClients("WARNING - The server has stopped listening for conections");
	  }
	  else if(message.equals("#close")) {
		  try {
			sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  else if(message.equals("#start")) {
		  if(!isListening()) {
			  try {
				listen();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  else {
			  System.out.println("The server is already started");
		  }
	  }
	  else if(message.equals("#getport")) {
		  System.out.println(Integer.toString(getPort()));
	  }
	  else if(message.length() > 7) {
		  if(message.substring(0,8).equals("#setport")){
			  if(isListening()) {
				  System.out.println("You need to disconnect first");
			  }
			  else {
			  		int newPort = Integer.parseInt((message.split(" "))[1]);
			  		setPort(newPort);
			  		System.out.println("Port set to: " + Integer.toString(newPort));
			  }
		  }
	  }
  }
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  /*public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
      System.out.println(ex);
    }
  }*/
}
//End of EchoServer class
