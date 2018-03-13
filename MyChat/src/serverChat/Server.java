//package serverChat;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//
//import chat_IP_Port.Chat_IP_Port;
//import clientChat.Client;
//
//public class Server 
//{
//	private Chat_IP_Port chat_IP_Port;
////	private Scanner inMessageScanner;
////	private PrintWriter outMessageWriter;
//	private Socket clientSocket;
//	private ServerSocket serverSocket;
//	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
////	private Client client;
//	
//	public Server() 
//	{
//		ServerGUI gui = new ServerGUI();
//		gui.createGUI();
//	}
//	class ServerGUI
//	{
//		JFrame serverFrame;
//		JPanel serverPanel;
//		JButton serverOn;
//		JButton serverOff;
//		JLabel currentStatusServerLabel;
//		JLabel infoLabel;
//		String status = "OFF";
//		String infoClose = "Перед закрытием выруби сервер";
//		String infoOnClick = "Сервер уже включен";
//		boolean isON = false;
//		int countOnCliked = 0;
//		
//		private void createGUI()
//		{
//			int widthPanel = 310;
//			int heightPanel = 120;
//			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			int x = (screenSize.width - widthPanel) / 2;
//			int y = (screenSize.height - heightPanel) / 2;
//			
//			serverFrame = new JFrame();
//			serverPanel = new JPanel();
//			serverOn = new JButton("On shit server!");
//			serverOff = new JButton("Off, asshole!");
//			currentStatusServerLabel = new JLabel("Server status: " + status);
//			infoLabel = new JLabel();
//			
//			serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			serverFrame.setResizable(false);
//			serverFrame.setBounds(x, y, widthPanel, heightPanel);
//			serverPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
//			serverOn.setPreferredSize(new Dimension(120, 30));
//			serverOff.setPreferredSize(new Dimension(120, 30));
//			currentStatusServerLabel.setHorizontalAlignment(JLabel.CENTER);
//			infoLabel.setHorizontalAlignment(JLabel.CENTER);
//			
//			serverFrame.add(currentStatusServerLabel, BorderLayout.NORTH);
//			serverFrame.add(infoLabel, BorderLayout.CENTER);
//			serverFrame.add(serverPanel, BorderLayout.SOUTH);
//			serverPanel.add(serverOn);
//			serverPanel.add(serverOff);
//			
//			serverOn.addActionListener(new ActionListener() 
//			{
//				public void actionPerformed(ActionEvent e)
//				{
//					isON = true;
//					countOnCliked++;
//					if(isON == true)
//					{
//						status = "ON";
//						currentStatusServerLabel.setText("Server status: " + status);
//						serverFrame.repaint();
//						try 
//						{
//							serverSocket = new ServerSocket(chat_IP_Port.getPort());
//							while(true)
//							{
//								clientSocket = serverSocket.accept();
//								ClientHandler clientHandler = new ClientHandler(clientSocket);
//								clients.add(clientHandler);
//								Thread thread = new Thread(clientHandler);
//								thread.start();
//								//////////////////////////////////////////////////////////
//							}
//						} 
//						catch (IOException e1) 
//						{
//							e1.printStackTrace();
//						}
//						
//					}
//					if(isON == true && countOnCliked > 1)
//					{
//						infoLabel.setForeground(Color.GREEN);
//						infoLabel.setText(infoOnClick);
//					}
//				}
//			});
//			serverOff.addActionListener(new ActionListener() 
//			{
//				public void actionPerformed(ActionEvent e)
//				{
//					status = "OFF";
//					infoLabel.setForeground(Color.RED);
//					infoLabel.setText(infoClose);
//					serverFrame.repaint();
//					try 
//					{
//						clientSocket.close();
//						 serverSocket.close();
//					} 
//					catch (IOException e1) 
//					{
//						e1.printStackTrace();
//					}
//					currentStatusServerLabel.setText("Server status: " + status);
//				}
//			});
//			serverFrame.setVisible(true);
//		}
//	}
////	class ClientHandler implements Runnable
////	{
////		String clientMessage;
////		public ClientHandler() 
////		{
////			try
////			{
////				inMessageScanner = new Scanner(clientSocket.getInputStream());
////				outMessageWriter = new PrintWriter(clientSocket.getOutputStream());
////			} 
////			catch (IOException e)
////			{
////				e.printStackTrace();
////			}
////		}
////		public void run() 
////		{
////			try 
////			{
////				while(true)
////				{
//////					if(client.getUserName() != null)
////					{
////						sendMessageToAllClients(" вошел в чат!");
////						break;
////					}
////				}
////			
////				while(true)
////				{
////					if(inMessageScanner.hasNext()) 
////					{
////						clientMessage = inMessageScanner.nextLine();
////						sendMessageToAllClients(clientMessage);
////					}
////					Thread.sleep(100);
////				}
////			}
////			catch (Exception e) 
////			{
////				e.printStackTrace();
////			}
//////			finally close???
////		}
////		private void sendMessage(String clientMessage)
////		{
////			clientMessage = inMessageScanner.nextLine();
////			outMessageWriter.println(clientMessage);
////			outMessageWriter.flush();
////		}
////		private void sendMessageToAllClients(String clientMessage)
////		{
////			for(ClientHandler clientHandler : clients)
////			{
////				clientHandler.sendMessage(clientMessage);
////			}
////		}
////	}
//	public void sendMessageToAllClients(String clientMessage)
//	{
//		for(ClientHandler clientHandler : clients)
//		{
//			clientHandler.sendMessage(clientMessage);
//		}
//	}
//	public static void main(String[] args) 
//	{
//		Server s = new Server();
//	}
//
//}
