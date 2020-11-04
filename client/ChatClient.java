// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String id;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String id, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.id = id;
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    		sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  public void handleMessageFromUIServer(String message) {
	    try
	    {
	    		sendToServer(message);
	    }
	    catch(IOException e)
	    {
	      clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  protected void connectionClosed() {
		System.out.println("Connection closed");
	}
  
  protected void connectionException(Exception exception) {
		System.out.println("Abnormal termination of connection.");
	}
  
  protected void connectionEstablished() {
	  try {
		sendToServer("#login " + id);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  /**
   * *This method is responsible for handling commands from clientUI
   * @param message
   */
  public void clientCommands(String message) throws IOException {
	  if(message.equals("#quit")) {
		  quit();
		  System.exit(0);
	  }
	  else if(message.equals("#logoff")) {
		  closeConnection();
	  }
	  else if(message.equals("#login")) {
		  if(isConnected()) {
			  System.out.println("You are already logged in");
		  }
		  else {
			  try {
				  openConnection();
				  System.out.println("You are now logged in");
			  }
			  catch(IOException e) {
				  System.out.println("Cannot open connection. Awaiting command.");
			  }
		  }
	  }
	  else if(message.equals("#gethost")) {
		  System.out.println("The host is " + getHost());
	  }
	  else if(message.equals("#getport")) {
		  System.out.println("The current port is " + Integer.toString(getPort()));
	  }
	  else if(message.length() > 7) {
		  if((message.substring(0,8)).equals("#sethost")) {
			  if(isConnected()) {
				  System.out.println("You need to disconnect first");
			  }
			  else {
				  String newHost = (message.split(" "))[1]; 
				  setHost(newHost);
				  System.out.println("Host set to: " + newHost);
			  }
		  }
		  else if(message.substring(0,8).equals("#setport")){
			  if(isConnected()) {
				  System.out.println("You need to disconnect first");
			  }
			  else {
			  		int newPort = Integer.parseInt((message.split(" "))[1]);
			  		setPort(newPort);
			  		System.out.println("Port set to: " + Integer.toString(newPort));
			  }
		  }
		  else if(((message.split(" "))[0]).equals("#login")) {
			  openConnection();
		  }
	  }
	  
  }
}
//End of ChatClient class
