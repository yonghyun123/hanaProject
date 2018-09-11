package kr.or.kosta.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.Hashtable;

import kr.or.kosta.chat.common.Protocol;

/**
 * Chatting Server
 * @author 김기정
 */
public class ChatServer {
	public static final int PORT = 7777;
	private boolean running;
	private ServerSocket serverSocket;
	private Hashtable<String, Client> clients;
	
	public boolean isRunning() {
		return running;
	}

	public Hashtable<String, Client> getClients() {
		return clients;
	}
	
	public void startUp() throws IOException {
		try {
			serverSocket = new ServerSocket(PORT);
		}catch (Exception e) {
			throw new IOException("["+PORT + "] 포트 충돌로 ChatServer를 구동할 수 없습니다.");
		}
		
		running = true;
		clients = new Hashtable<String, Client>();
		System.out.println("BTS["+PORT + "] ChatServer Start....");
		
		while(running) {
			try {
				Socket socket = serverSocket.accept();
				Client client = new Client(socket, this);
				client.start();
				
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void shutDown() {}
	
	public void addClient(Client client) {
		clients.put(client.getNickName(), client);
	}
	
	public int getClientCount() {
		return clients.size();
	}
	
	public boolean isExistNickName(String nickName) {
		return clients.containsKey(nickName);
	}
	
	public void removeClient(Client client) {
		clients.remove(client.getNickName());
	}
	
	public void sendGuestList(String protocolName) {
		System.out.println("여기는 서버 userList크기는");
		Enumeration<Client> eObjects = clients.elements();
		Enumeration<String> ekeys = clients.keys();
		String userList = "";
		//tempArr에는 각 유저 이름이 들어갔음
		while(ekeys.hasMoreElements()) {
			userList += (ekeys.nextElement() + Protocol.DELEMETER);
		}
	
		if(userList != null) {
			while (eObjects.hasMoreElements()) {
				Client client = eObjects.nextElement();
				client.sendMessage((protocolName + Protocol.DELEMETER + userList ));
			}
		}

	}
	
	
	public void sendAllMessage(String message) {		
		Enumeration<Client> e = clients.elements();
		while (e.hasMoreElements()) {
			Client client = e.nextElement();
			client.sendMessage(message);
		}
	}
	
}






