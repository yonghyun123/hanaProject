package kr.or.kosta.yongchat.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import kr.or.kosta.yongchat.common.YongProtocol;
import kr.or.kosta.yongchat.gui.YongMainFrame;



public class YongChatClient {
	public static final String SERVER = "localhost";
	public static final int PORT = 7777;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private boolean running;
	private YongMainFrame mainFrame;
	
	public YongChatClient(YongMainFrame mainFrame) {
		 this.mainFrame = mainFrame;
	}
	
	public void connectServer() throws Exception {
		try {
			socket = new Socket(SERVER, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			running = true;
		}catch (Exception e) {
			throw new Exception("서버를 찾을 수 없습니다..");
		}
	}
	
	public void stopClient() {
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	public void sendMessage(String message) {
		if(out != null) out.println(message);
	}
	
	public void receiveMessage() {
		new Thread() {
			@Override
			public void run() {
				while(running) {
					String serverMessage = null;
					try {
						serverMessage = in.readLine();
						System.out.println("[Debug] : 서버로부터 받은 메시지 " + serverMessage);
						process(serverMessage);
						
					} catch (IOException e) {
						System.out.println("네트워크가 단절되었습니다..");
						break;
					}
				}
				
				if(socket != null) {
					try {
						socket.close();
					} catch (IOException e) {}
				}
			}
			
		}.start();
	}
	
	public void process(String message) {
		String[] tokens = message.split(YongProtocol.DELEMETER);
		int protocol = Integer.parseInt(tokens[0]);
		String nickName = tokens[1];
		
		switch (protocol) {
			case YongProtocol.CONNECT_RESULT:
				String result = tokens[2];
				if (result.equalsIgnoreCase("SUCCESS")) {
					JOptionPane.showMessageDialog(null, "접속에 성공하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);

					
					mainFrame.changeCard("LOBBY");
				} else {
					JOptionPane.showMessageDialog(null, "이미 사용중인 대화명입니다.\n다른 대화명을 사용하세요.", "경고", JOptionPane.ERROR_MESSAGE);
				}  
				break;

//			case YongProtocol.MULTI_CHAT:
//				String chatMessage = tokens[2];
//				mainFrame.appendMessage("["+nickName+"]: " + chatMessage);
//				break;
//			case YongProtocol.GUEST_LIST:
//				mainFrame.userList.removeAll();
//				for(int i = 1; i < tokens.length; i++) {
//					mainFrame.userList.add(tokens[i]);
//				}
//				break;
			case YongProtocol.DISCONNECT:
				mainFrame.getWaitingPanel().participateList.add("###"+nickName+"님이 퇴장###");
				//끊어질때 기능 구현
				
			case YongProtocol.UPDATELIST:
				mainFrame.getWaitingPanel().waitingList.removeAll();
				for(int i = 2; i < tokens.length; i++) {
					mainFrame.getWaitingPanel().waitingList.add(tokens[i]);
				}
//			
//				break;
//			default:
//				break;
		}
		
	}
}
