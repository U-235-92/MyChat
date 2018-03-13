package clientChat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat_IP_Port.Chat_IP_Port;

public class Client 
{
	private Chat_IP_Port chat_IP_Port;
	private Scanner inMessageScanner;
	private PrintWriter outMessageWriter;
	private Socket clientSocket;
	
	private JTextField outgoingTextField;
	private JTextArea incommingAreaMessge;
	
	private String userName;
	private String exitUserMesage = "user%%%Exit";
	
	int widthPanel;
	int heightPanel;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int xPosPanel;
	int yPosPanel;
	
	public String getUserName() 
	{
		return userName;
	}
	public String getExitUserMesage() 
	{
		return exitUserMesage;
	}

	public Client()
	{
		ChatStartPage page = new ChatStartPage();
		page.createGUI();
	}
	class ChatStartPage
	{
		JFrame startPageFrame;
		JPanel panel;
		JButton button;
		JTextField nameClientField;
		JLabel label;
		public ChatStartPage()
		{
			startPageFrame = new JFrame();
			panel = new JPanel();
			button = new JButton("Go");
			nameClientField = new JTextField();
			label = new JLabel("Введите в поле ваше имя");
		}
		private void createGUI()
		{
			int widthPanel = 300;
			int heightPanel = 80;
			xPosPanel = (screenSize.width - widthPanel) / 2;
			yPosPanel = (screenSize.height - heightPanel) / 2;
			startPageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			startPageFrame.setBounds(xPosPanel, yPosPanel, widthPanel, heightPanel);
			
			nameClientField.setFont(new Font("", Font.BOLD, 13));
			
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			label.setHorizontalAlignment(JLabel.CENTER);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					userName = nameClientField.getText();
					if(userName.equals("")) //if(textUser.trim().length() > 0)
					{
						label.setForeground(Color.RED);
						startPageFrame.repaint();
					}
					else if(userName.trim().equals(""))
					{
						userName = "";
						label.setForeground(Color.RED);
						nameClientField.setText("");
						startPageFrame.repaint();
					}
					else
					{
						startPageFrame.setVisible(false);
						ChatFrame chatFrame = new ChatFrame();
						chatFrame.createGUI();
						try 
						{
							clientSocket = new Socket(chat_IP_Port.getIp(), chat_IP_Port.getPort());
							inMessageScanner = new Scanner(clientSocket.getInputStream());
							outMessageWriter = new PrintWriter(clientSocket.getOutputStream());
							sendUserNameToServer(userName);
							//////////////////////////////////////////////////////////////////////////////////////
						} 
						catch (UnknownHostException ex) 
						{
							// TODO Auto-generated catch block
							ex.printStackTrace();
						} 
						catch (IOException ex) 
						{
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
						Thread thread = new Thread(new DisplayInMessage());
						thread.start();
						startPageFrame.dispose();
					}
				}
			});
			startPageFrame.add(label, BorderLayout.NORTH);
			startPageFrame.add(panel);
			panel.add(nameClientField, BorderLayout.CENTER);
			panel.add(button, BorderLayout.EAST);
			
			startPageFrame.setVisible(true);
		}
	}
	class ChatFrame
	{
		private void createGUI()
		{
			widthPanel = 600;
			heightPanel = 400;
			xPosPanel = (screenSize.width - widthPanel) / 2;
			yPosPanel = (screenSize.height - heightPanel) / 2;
			
			JFrame chatFrame = new JFrame();
			chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			chatFrame.setBounds(xPosPanel, yPosPanel, widthPanel, heightPanel);
			JPanel writePanel = new JPanel(new BorderLayout());
			JLabel numberOfClients = new JLabel("Сейчас в чате: ");
			chatFrame.add(numberOfClients, BorderLayout.NORTH);
			chatFrame.add(writePanel, BorderLayout.SOUTH);
			incommingAreaMessge = new JTextArea();
			incommingAreaMessge.setLineWrap(true);
			incommingAreaMessge.setEditable(false);
			JScrollPane srollPane = new JScrollPane(incommingAreaMessge);
			srollPane.setHorizontalScrollBar(null);
			chatFrame.add(srollPane, BorderLayout.CENTER);
			outgoingTextField = new JTextField("Введите ваше сообщение: ");
			JButton buttonSend = new JButton("Send");
			writePanel.add(outgoingTextField, BorderLayout.CENTER);
			writePanel.add(buttonSend, BorderLayout.EAST);
			outgoingTextField.addFocusListener(new FocusAdapter() 
			{
				public void focusGained(FocusEvent e) 
				{
					if(outgoingTextField.getText().equals("Введите ваше сообщение: "))
					{
						outgoingTextField.setText("");
					}
				}
			});
			buttonSend.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					sendMessage();
					outgoingTextField.setText("");
					outgoingTextField.requestFocus();
				}
			});
			outgoingTextField.addKeyListener(new KeyAdapter() 
			{
				public void keyPressed(KeyEvent e) 
				{
					if(e.getKeyCode() == e.VK_ENTER)
					{
						sendMessage();
						outgoingTextField.setText("");
						outgoingTextField.requestFocus();
					}
				}
			});
			chatFrame.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					super.windowClosing(e);
					try 
					{
						outMessageWriter.println(userName + " вышел из чата");
						outMessageWriter.println(exitUserMesage);
						outMessageWriter.flush();
						clientSocket.close();
						chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						chatFrame.dispose();
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
			});
			chatFrame.setVisible(true);
		}
		private void sendMessage()
		{
			String textUser = outgoingTextField.getText();
			if(textUser.equals("Введите ваше сообщение: "))
			{
				textUser = "";
			}
			String message = "";
			if(textUser.trim().length() > 0) //textUser.length() > 0 
			{
				message = "<" + userName + ">" + ": " + textUser;
				outMessageWriter.println(message);
				outMessageWriter.flush();
			}
		}
	}
	class DisplayInMessage implements Runnable
	{
		public void run() 
		{
			String message = "";
			while(true)
			{
				if(inMessageScanner.hasNext())
				{
					message = inMessageScanner.nextLine();
					if(message.equals(exitUserMesage))
					{
						message = "";
					}
					else
					{
						incommingAreaMessge.setFont(new Font(userName, Font.BOLD, 13));
						incommingAreaMessge.append(message + "\n");
					}
				}
			}
		}
	}
	private void sendUserNameToServer(String name)
	{
		outMessageWriter.println(name);
		outMessageWriter.flush();
	}
	public static void main(String[] args) 
	{
		Client c = new Client();
	}

}
