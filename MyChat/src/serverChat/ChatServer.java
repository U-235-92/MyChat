package serverChat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import chat_IP_Port.Chat_IP_Port;

public class ChatServer 
{
	private Chat_IP_Port chat_IP_Port;
	private ServerSocket serverSocket;
	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private Socket clientSocket;
	
	public ChatServer()
	{
		try
		{
			serverSocket = new ServerSocket(chat_IP_Port.getPort());
			System.out.println("Сервер запущен!");
			while(true)
			{
				clientSocket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(clientSocket, this);
				clients.add(clientHandler);
				Thread threadChat = new Thread(clientHandler);
				threadChat.start();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void sendMessageToAllClients(String clientMessage)
	{
		for(ClientHandler clientHandler : clients)
		{
			clientHandler.sendMessage(clientMessage);
		}
	}
	public void removeClient(ClientHandler client) 
	{
		clients.remove(client);
	}
	public static void main(String[] args) 
	{
		ChatServer s = new ChatServer();
	}
}
