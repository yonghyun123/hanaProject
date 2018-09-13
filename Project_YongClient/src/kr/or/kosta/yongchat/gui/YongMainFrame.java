package kr.or.kosta.yongchat.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import kr.or.kosta.yongchat.common.YongProtocol;
import kr.or.kosta.yongchat.gui.dialog.YongRoomDialogFrame;
import kr.or.kosta.yongchat.network.YongChatClient;

/**
 * 메인프레임 설정 layout = cardLayout
 * 
 * @author yonghyun
 *
 */
public class YongMainFrame extends Frame {
	/** card panel list */
	// join panel
	YongJoinPanel joinPanel;

	YongChatPanel chatPanel;
	YongWaitingPanel waitingPanel;
	CardLayout cardLayout;
	Panel cardPanel;

	// properties
	private String nickName;
	private YongChatClient chatClient;
	private YongRoomDialogFrame dialogFrame;
	private String roomNumber;
	
	private String findRoomNumber;
	private String curUserCnt;
	private String maxCnt;

	public YongMainFrame() {

	}

	public YongMainFrame(String title) {
		super(title);
		joinPanel = new YongJoinPanel(this);
		chatPanel = new YongChatPanel(this);
		waitingPanel = new YongWaitingPanel(this);
		cardPanel = new Panel();
		cardLayout = new CardLayout();
		dialogFrame = new YongRoomDialogFrame();
	}

	public void init() {
		this.joinPanel.init();
		this.chatPanel.init();
		this.waitingPanel.init();

		cardPanel.setLayout(cardLayout);
		cardPanel.add("JOIN", joinPanel);
		cardPanel.add("CHAT", chatPanel);
		cardPanel.add("LOBBY", waitingPanel);

		this.add(cardPanel);
		this.setSize(500, 400);
		this.setVisible(true);
		this.setCenter();

		// event listener
		eventRegist();
	}

	public void changeCard(String name) {
		cardLayout.show(cardPanel, name);
	}

	public void setCenter() {

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		int x = (dim.width - getSize().width) / 2;
		int y = (dim.height - getSize().height) / 2;
		this.setLocation(x, y);
	}

	public void finish() {
		chatClient.sendMessage(YongProtocol.DISCONNECT + YongProtocol.DELEMETER + nickName);
		System.out.println();
		chatClient.stopClient();
		setVisible(false);
		dispose();
		System.exit(0);
	}


	// 메시지 붙이기
	public void appendMessage(String message) {
		chatPanel.messageTA.append(message + "\n");
	}

	/** 방입장시 서버로 메시지 보내기 */
	public void enteredRoomMessage() {
		//입장시 textArea 초기화
		chatPanel.messageTA.setText("");
		String parseStr = waitingPanel.roomList.getSelectedItem();
		if(parseStr == null || parseStr.trim().length() == 0){
			return;
		}
		if(Integer.parseInt(curUserCnt) < Integer.parseInt(maxCnt)){
			String[] tokens = parseStr.split("\\s+");
			System.out.println("roomNumber" + tokens[0]);
			roomNumber = tokens[0];
			chatClient.sendMessage(YongProtocol.ROOMIN + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER + roomNumber);
			changeCard("CHAT");
		}
		else {
			JOptionPane.showMessageDialog(this, "최대인원을 초과했습니다.");
			return;
		}
	}

	// 방만들기 버튼 눌렀을때 -> 방 텍스트 + choice + 닉네임 보내기
	public void sendRoomInfo() {
		//입장시 textArea 초기화
		chatPanel.messageTA.setText("");
		String message = dialogFrame.roomDialog.roomNameTF.getText();
		
		String maxRoomCnt = dialogFrame.roomDialog.numberC.getSelectedItem();
		if (message == null || message.trim().length() == 0) {
			return;
		}
		if(message.contains(" ")){
			JOptionPane.showMessageDialog(this, "방제목에 공백을 포함할 수 없습니다");
			return;
		}
		chatClient.sendMessage(YongProtocol.CREATE + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER
				+ message + YongProtocol.DELEMETER + maxRoomCnt);
		changeCard("CHAT");
	}

	// 여기서 최초 연결 호출
	public void connect() {
		nickName = joinPanel.nickNameTF.getText();
		//여기도 코드 수정 필요
		if(nickName.contains(" ")){
			JOptionPane.showMessageDialog(this, "닉네임에 공백을 포함할 수 없습니다");
			return;
		}
		try {
			chatClient.connectServer();
			// 최초 연결 메시지 전송
			chatClient.sendMessage(YongProtocol.CONNECT + YongProtocol.DELEMETER + nickName);
			chatClient.receiveMessage();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "연결 실패", JOptionPane.ERROR_MESSAGE);
		}

	}
	/**
	 * 나중에 메서드 합칠 수 있는 부분
	 */
	/** multi chat send method (다중채팅할때 사용하는 메서드) */ 
	public void sendMessage() {
		String message = chatPanel.sendTF.getText();
		if (message == null || message.trim().length() == 0) {
			return;
		}
		chatPanel.sendTF.setText("");
		chatClient.sendMessage(YongProtocol.MULTI_CHAT + YongProtocol.DELEMETER + nickName 
				+ YongProtocol.DELEMETER + roomNumber +YongProtocol.DELEMETER + message);
	}
	/**1:1 채팅 기능으로 서버에게 메시지 요청 */
	public void secretSendMessage(String selectItem){
		String message = chatPanel.sendTF.getText();
		if (message == null || message.trim().length() == 0) {
			return;
		}
		chatPanel.sendTF.setText("");
		chatClient.sendMessage(YongProtocol.SECRET_CHAT + YongProtocol.DELEMETER + nickName 
				+ YongProtocol.DELEMETER + roomNumber +YongProtocol.DELEMETER+ selectItem
				+ YongProtocol.DELEMETER + message);
	}
	/** 대기자룸에 있는 유저에게 초대메시지 보내기 */
	public void inviteMessage(){
		String invitedUser = chatPanel.waitUserList.getSelectedItem();
		if (invitedUser == null || invitedUser.trim().length() == 0) {
			return;
		}
		chatClient.sendMessage(YongProtocol.INVITE + YongProtocol.DELEMETER + nickName 
				+ YongProtocol.DELEMETER + roomNumber +YongProtocol.DELEMETER + invitedUser);
	}
	
	/** 채팅룸에서 나갈때 서버에게 메시지 보내기 */
	public void exitMessage(){
		chatClient.sendMessage(YongProtocol.BACK + YongProtocol.DELEMETER + nickName 
				+ YongProtocol.DELEMETER + roomNumber);
	}
	
	public void leaderExitedMessage(){
		changeCard("LOBBY");
	}
	
	/** 초대 다이얼로그 */
	public void showInviteDialog(String message){
		int result = JOptionPane.showConfirmDialog(this, message,"확인",JOptionPane.YES_NO_OPTION);
		//사용자가 예, 아니오 없이 창을 닫은 경우
		if(result == JOptionPane.CLOSED_OPTION){
			chatClient.sendMessage(YongProtocol.INVITE_REJECT + YongProtocol.DELEMETER + nickName 
					+ YongProtocol.DELEMETER + roomNumber + YongProtocol.DELEMETER + chatClient.getInvitingUser());
			//사용자가 예 버튼을 누른경우
		} else if(result == JOptionPane.YES_OPTION){
			chatClient.sendMessage(YongProtocol.ROOMIN + YongProtocol.DELEMETER + nickName 
					+ YongProtocol.DELEMETER + roomNumber);
			changeCard("CHAT");
			chatPanel.messageTA.setText("");
			//아니오 버튼을 누른경우
		} else {
			chatClient.sendMessage(YongProtocol.INVITE_REJECT + YongProtocol.DELEMETER + nickName 
					+ YongProtocol.DELEMETER + roomNumber + YongProtocol.DELEMETER + chatClient.getInvitingUser());
		}
		
	}
	
	/** 방이 선택되었을때 최대 인원을 보내는 메서드 */
	public String selectedMaxCnt(){
		String parseStr = waitingPanel.roomList.getSelectedItem();
		String[] tokens = parseStr.split("\\s+");
		return tokens[3];
	}
	
	/** 룸리스트에서 방이 선택됬을때 서버에게 보내는 메서드 */
	public void selectedItemMessage(){
		String parseStr = waitingPanel.roomList.getSelectedItem();
		String[] tokens = parseStr.split("\\s+");
		for(int i = 0; i < tokens.length; i++){
			System.out.println("tokens!!!!!!!!!!!"+tokens[i]);
		}
		chatClient.sendMessage(YongProtocol.SELECT_ROOM + YongProtocol.DELEMETER + nickName 
				+ YongProtocol.DELEMETER + Integer.parseInt(tokens[0]));
	}
	
	/** 방검색 메서드 */
	public void searchRoomName(){
		
		/************************************************************/
		String roomName = waitingPanel.roomSearchTF.getText().trim();
//		if (roomName == null || roomName.trim().length() == 0) {
//			return;
//		}
		chatClient.sendMessage(YongProtocol.SEARCH_ROOM + YongProtocol.DELEMETER + nickName 
				+ YongProtocol.DELEMETER + roomName);
		
	}
	
	/** 이벤트 등록 메서드 */
	public void eventRegist() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				finish();
			}
		});

		// 방생성 버튼 누르면 방생성 frame 만들기
		waitingPanel.makeB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogFrame.init();
			}
		});
		
	
		/** 방생성 버튼을 눌렀을 때 서버로 메시지 송신 */
		dialogFrame.roomDialog.createB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendRoomInfo();
				dialogFrame.finish();
				
			}
		});
		
		/** 방입장 버튼을 눌렀을 때 서버로 메시지 송신 */
		waitingPanel.enterB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String roomInfo = waitingPanel.roomList.getSelectedItem();
				String[] tokens = roomInfo.split("\\s+");
				for(int i = 0; i < tokens.length; i++){
					System.out.println("###tokes: "+tokens[i]);
					System.out.println("###현재인원: " + getCurUserCnt());
					System.out.println("###방번호: " + getFindRoomNumber());
				}
				enteredRoomMessage();
				
			}
		});
		
		/** 채팅방 다중 클라이언트 채팅 리스너 enter key */
		chatPanel.sendTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!chatPanel.choice.getSelectedItem().equals("전체")){
					System.out.println("selectedItem"+chatPanel.choice.getSelectedItem());
					secretSendMessage(chatPanel.choice.getSelectedItem());
				} else{
					sendMessage();	
				}
			}
		});
		/** 채팅방 다중 클라이언트 채팅 리스너 send Button*/
		chatPanel.sendB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!chatPanel.choice.getSelectedItem().equals("전체")){
					System.out.println("selectedItem"+chatPanel.choice.getSelectedItem());
					secretSendMessage(chatPanel.choice.getSelectedItem());
				} else{
					sendMessage();	
				}
				
			}
		});
		
		/** 대기방에 있는 유저 초대하기 */
		chatPanel.inviteB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inviteMessage();
			}
		});
		
		/** 채팅방에 있는 유저 대기룸으로 back btn */
		chatPanel.exitB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitMessage();
				changeCard("LOBBY");
			}
		});
		
		/** 대기방에 있는 방목록을 눌렀을 시 데이터 전송 */
		waitingPanel.roomList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					selectedItemMessage();
				}
			}
		});
		
		/** 방검색 버튼이벤트 */
		waitingPanel.roomSearchTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchRoomName();
			}
		});
		waitingPanel.roomSearchB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchRoomName();
			}
		});
		
	}

	/** getter and setter */
	public void setYongChatClient(YongChatClient chatClient) {
		this.chatClient = chatClient;
	}

	public YongJoinPanel getJoinPanel() {
		return joinPanel;
	}

	public void setJoinPanel(YongJoinPanel joinPanel) {
		this.joinPanel = joinPanel;
	}

	public YongChatPanel getChatPanel() {
		return chatPanel;
	}

	public void setChatPanel(YongChatPanel chatPanel) {
		this.chatPanel = chatPanel;
	}

	public YongWaitingPanel getWaitingPanel() {
		return waitingPanel;
	}

	public void setWaitingPanel(YongWaitingPanel waitingPanel) {
		this.waitingPanel = waitingPanel;
	}

	public String getNickName() {
		return this.nickName;
	}
	
	public void setRoomNumber(String roomNumber){
		this.roomNumber = roomNumber;
	}

	public void setFindRoomNumber(String findRoomNumber){
		this.findRoomNumber = findRoomNumber;
	}
	public String getFindRoomNumber(){
		return this.findRoomNumber;
	}
	
	public void setCurUserCnt(String curUserCnt){
		this.curUserCnt = curUserCnt;
	}
	public String getCurUserCnt(){
		return this.curUserCnt;
	}
	
	public void setMaxCnt(String maxCnt){
		this.maxCnt = maxCnt;
	}
}