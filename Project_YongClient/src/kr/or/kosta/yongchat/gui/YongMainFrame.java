package kr.or.kosta.yongchat.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		chatClient.stopClient();
		setVisible(false);
		dispose();
		System.exit(0);
	}


	// 메시지 붙이기
	public void appendMessage(String message) {
		chatPanel.messageTA.append(message + "\n");
	}

	// 클라이언트가 방을 입장 할 때.
	public void enteredRoom() {
		String parseStr = waitingPanel.roomList.getSelectedItem();
		String[] tokens = parseStr.split("\\s+");
		System.out.println("roomNumber" + tokens[1]);
		roomNumber = tokens[1];
		chatClient.sendMessage(YongProtocol.ROOMIN + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER + tokens[1]);
	}

	// 방만들기 버튼 눌렀을때 -> 방 텍스트 + choice + 닉네임 보내기
	public void sendRoomInfo() {
		String message = dialogFrame.roomDialog.roomNameTF.getText();
		String maxRoomCnt = dialogFrame.roomDialog.numberC.getSelectedItem();
		if (message == null || message.trim().length() == 0) {
			return;
		}
		chatClient.sendMessage(YongProtocol.CREATE + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER
				+ message + YongProtocol.DELEMETER + maxRoomCnt);
	}

	// 여기서 최초 연결 호출
	public void connect() {
		nickName = joinPanel.nickNameTF.getText();
		try {
			chatClient.connectServer();
			// 최초 연결 메시지 전송
			chatClient.sendMessage(YongProtocol.CONNECT + YongProtocol.DELEMETER + nickName);
			chatClient.receiveMessage();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "연결 실패", JOptionPane.ERROR_MESSAGE);
		}

	}
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

		// 방 생성 다이어로그 정보 서버로 보내기
		dialogFrame.roomDialog.createB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendRoomInfo();
				dialogFrame.finish();
				changeCard("CHAT");
			}
		});

		waitingPanel.enterB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enteredRoom();
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

}