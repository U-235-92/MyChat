package serverChat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import chat_IP_Port.Chat_IP_Port;
import clientChat.Client;

public class ClientHandler implements Runnable
{
	private Scanner inMessageScanner;
	private PrintWriter outMessageWriter;
	private Socket clientSocket;
	private String clientMessage;
	private ChatServer server;
	private Client client;
	
	public ClientHandler(Socket socket, ChatServer server)
	{
		try 
		{
			this.clientSocket = socket;
			this.server = server;
			inMessageScanner = new Scanner(clientSocket.getInputStream());
			outMessageWriter = new PrintWriter(clientSocket.getOutputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void run() 
	{
		while(true)
		{
			try 
			{
	            while(true) 
	            {
	            	clientMessage = inMessageScanner.nextLine();
                    server.sendMessageToAllClients(clientMessage + " зашел в чат!");
	                break;
	            }
	            while(true) 
	            {
	                if(inMessageScanner.hasNext())
	                {
	                    clientMessage = inMessageScanner.nextLine();
	                    if(clientMessage.equals("user%%%Exit"))
	                    {
	                    	this.close();
	                    	try 
	                    	{
								clientSocket.close();
							} 
	                    	catch (IOException e) 
	                    	{
								e.printStackTrace();
							}
	                    }
	                    server.sendMessageToAllClients(clientMessage);
	                }
	                Thread.sleep(100);
	            }
	        }
	        catch (InterruptedException ex) 
			{
	            ex.printStackTrace();
	        }
		}
		
	}
	public void sendMessage(String clientMessage)
	{
		outMessageWriter.println(clientMessage);
		outMessageWriter.flush();
	}
	public void close()
	{
		server.removeClient(this);
	}
}
