package kr.or.kosta.yongchat.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import kr.or.kosta.yongchat.common.YongProtocol;
import kr.or.kosta.yongchat.gui.YongMainFrame;
import kr.or.kosta.yongchat.gui.dialog.YongRoomDialogFrame;



public class YongChatClient {
	// 192.168.0.121
	// localhost
	public static final String SERVER = "192.168.0.121";
	public static final int PORT = 7777;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private boolean running;
	private YongMainFrame mainFrame;
	private YongRoomDialogFrame dialogFrame;
	private String roomNumber;
	private String invitingUser;
	
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
					JOptionPane.showMessageDialog(null, "채팅에 세계에 오신걸 환영합니다", "알림", JOptionPane.INFORMATION_MESSAGE);
					mainFrame.changeCard("LOBBY");

				} else {
					JOptionPane.showMessageDialog(null, "이미 사용중인 대화명입니다.\n다른 대화명을 사용하세요.", "경고", JOptionPane.ERROR_MESSAGE);
				}  
				break;
				
			/** 방목록이 생성될때마다 대기실에 있는 유저목록 최신화 프로토콜 */
			case YongProtocol.ROOMLIST:
				
				mainFrame.getWaitingPanel().roomList.removeAll();
				for(int i = 2; i < tokens.length; i++){
					roomNumber = tokens[2];
					System.out.println("ROOMLIST!!!:" + roomNumber);
					mainFrame.getWaitingPanel().roomList.add(tokens[i]);
				}
				break;
				
			/** 방을 처음으로 생성할때 클라이언트가 받는 메시지 */
			case YongProtocol.CREATE:
				mainFrame.appendMessage("###"+nickName+"님이 입장하셨습니다.###");
				System.out.println("nickName!!!!!!!1"+nickName);
				mainFrame.getChatPanel().inUserList.add(nickName+"(나)");
				break;
				
			/** 방안으로 들어가서 클라이언트가 받는 메시지 */
			case YongProtocol.ROOMIN:
				mainFrame.getChatPanel().inUserList.removeAll();
				mainFrame.getChatPanel().choice.removeAll();
				mainFrame.getChatPanel().choice.add("전체");
				
				mainFrame.getChatPanel().messageTA.setText("");
				for(int i = 3; i < tokens.length; i++){
					if(mainFrame.getNickName().equals(tokens[i])){
						tokens[i] += "(나)";
					} else{
						mainFrame.getChatPanel().choice.add(tokens[i]);
					}
					mainFrame.getChatPanel().inUserList.add(tokens[i]);
				}
				System.out.println("ROOMIN!!!:" + roomNumber);
				mainFrame.appendMessage("###"+nickName+"님이 입장하셨습니다.###");
				break;
			/**멀티채팅 메시지를 보낸 후 받을 때 이벤트 처리 */
			case YongProtocol.MULTI_CHAT:
				String chatMessage = tokens[3];
				System.out.println("MULTI_CHAT"+chatMessage);
				mainFrame.appendMessage("["+nickName+"]: " + chatMessage);
				break;
				
			/**방정보 메시지를 보낸 후 받을 때 이벤트 처리 */
			case YongProtocol.ROOMNUMBER:
				mainFrame.setRoomNumber(tokens[2]);
				System.out.println("roomNumber:"+tokens[2]);
				break;
				
			case YongProtocol.SECRET_CHAT:
				String secretMessage = tokens[3];
				System.out.println("SECRET CHAT"+secretMessage);
				mainFrame.appendMessage("["+tokens[1]+">>"+tokens[2]+"]: " + secretMessage);
				break;
				
			case YongProtocol.INVITE:
				invitingUser = tokens[1];
				mainFrame.setRoomNumber(tokens[2]);
				mainFrame.showInviteDialog("초대에 응하시겠습니까?");
				break;
				
			case YongProtocol.INVITE_REJECT:
				JOptionPane.showMessageDialog(mainFrame, tokens[3]);
				break;

			case YongProtocol.DISCONNECT:
				mainFrame.getWaitingPanel().participateList.add("###"+nickName+"님이 퇴장###");
				//끊어질때 기능 구현
				
			case YongProtocol.UPDATELIST:
				mainFrame.getChatPanel().waitUserList.removeAll();
				mainFrame.getWaitingPanel().waitingList.removeAll();
				for(int i = 2; i < tokens.length; i++) {
					if(mainFrame.getNickName().equals(tokens[i])){
						tokens[i] += "(나)";
					} 
					mainFrame.getWaitingPanel().waitingList.add(tokens[i]);
					mainFrame.getChatPanel().waitUserList.add(tokens[i]);
				}
				break;
//			default:
//				break;
		}
	}
	//getter and setter
	
	public String getRoomNumber(){
		return this.roomNumber; 
	}
	
	public String getInvitingUser(){
		return this.invitingUser;
	}
	
	public void setDialogFrame(YongRoomDialogFrame dialogFrame){
		this.dialogFrame = dialogFrame;
	}
	
	
}
