import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 클라이언트의 데이터 수신 및 처리
 * 
 * @author 조희진
 *
 */
public class YongClient extends Thread {
	private boolean running;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private long time;

	/** 클라이언트 식별자 */
	private String nickName = "손님";

	YongChatServer chatServer;

	public YongClient(Socket socket, YongChatServer chatServer) throws IOException {
		this.socket = socket;
		this.chatServer = chatServer;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		running = true;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Socket getSocket() {
		return socket;
	}

	public Long getTime() {
		return time;
	}

	public void recieveMessage() {
		while (running) {
			String clientMessage = null;
			try {
				clientMessage = in.readLine();
				System.out.println("[Debug] : 클라이언트 수신 데이터: " + clientMessage);
				process(clientMessage);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 클라이언트의 메시지를 파싱하여 서비스 제공
	 * 
	 * @param message
	 */
	public void process(String message) {
		String[] tokens = message.split(YongProtocol.DELEMETER);

		int protocol = Integer.parseInt(tokens[0]);
		nickName = tokens[1];

		switch (protocol) {
		case YongProtocol.CONNECT:
			// 대화명 중복 여부 체크
			if (chatServer.isExistNickName(nickName)) {
				sendMessage(YongProtocol.CONNECT_RESULT + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER
						+ "FAIL");

			} else {
				chatServer.addClient(this);
				System.out.println("[Debug] : 접속 클라이언트 수 : " + chatServer.getClientCount());
				sendMessage(YongProtocol.CONNECT_RESULT + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER
						+ "SUCCESS");
				chatServer.sendAllMessage(YongProtocol.UPDATELIST + YongProtocol.DELEMETER + nickName);
				chatServer.sendAllRoom(YongProtocol.ROOMLIST + YongProtocol.DELEMETER + nickName);
			}
			break;

		case YongProtocol.MULTI_CHAT:
			System.out.println("너 여기도 안들어오지?");
			Date today = new Date();
			SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
			chatServer.sendAllMessageTest(message + YongProtocol.DELEMETER + time.format(today));
			break;

		case YongProtocol.CREATE:
			chatServer.addRoom(message);
			sendMessage(message);
			chatServer.sendAllRoom(YongProtocol.ROOMLIST + YongProtocol.DELEMETER + nickName);
			chatServer.sendAllMessage(YongProtocol.UPDATELIST + YongProtocol.DELEMETER + nickName);
			break;

		case YongProtocol.ROOMIN:
			int roomNumber = Integer.parseInt(tokens[2]);
			System.out.println("받은 방번호 ::::::" + roomNumber);
			chatServer.addParticipate(roomNumber, nickName);
			chatServer.sendParticipate(roomNumber, message);
			chatServer.sendAllMessage(YongProtocol.UPDATELIST + YongProtocol.DELEMETER + nickName);
			break;

		case YongProtocol.SECRET_CHAT:
			roomNumber = Integer.parseInt(tokens[2]);
			String receiver = tokens[3];
			String text = tokens[4];
			chatServer.sendOneClient(roomNumber, nickName, receiver, text);

			sendMessage(YongProtocol.SECRET_CHAT + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER + receiver
					+ YongProtocol.DELEMETER + text);
			break;

		case YongProtocol.INVITE:
			roomNumber = Integer.parseInt(tokens[2]);
			String invited = tokens[3];
			chatServer.sendInvitedClient(roomNumber, nickName, invited);
			break;

		case YongProtocol.INVITE_REJECT:
			roomNumber = Integer.parseInt(tokens[2]);
			receiver = tokens[3];
			text = nickName + "님께서 초대를 거절하셨습니다.";
			chatServer.sendRejectMessage(roomNumber, nickName, receiver, text);
			break;

		case YongProtocol.BACK:
			roomNumber = Integer.parseInt(tokens[2]);
			if (!chatServer.isBack(roomNumber, nickName, protocol)) { // 방장이 방을
																		// 나갔을 때
				chatServer.sendParticipate(roomNumber, message);
			}
			chatServer.sendAllRoom(YongProtocol.ROOMLIST + YongProtocol.DELEMETER + nickName);
			chatServer.sendAllMessage(YongProtocol.UPDATELIST + YongProtocol.DELEMETER + nickName);

			break;

		case YongProtocol.SELECT_ROOM:
			roomNumber = Integer.parseInt(tokens[2]);
			String inuserList = chatServer.participateAll(roomNumber, message);
			sendMessage(YongProtocol.SELECT_ROOM + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER
					+ roomNumber + YongProtocol.DELEMETER + inuserList);
			break;

		case YongProtocol.SEARCH_ROOM:

			if (tokens.length < 3) {
				chatServer.sendAllRoom(YongProtocol.ROOMLIST + YongProtocol.DELEMETER + nickName);
			} else {
				String roomName = tokens[2];
				sendMessage(
						YongProtocol.SEARCH_ROOM + YongProtocol.DELEMETER + nickName + chatServer.roomInfo(roomName));
			}
			break;

		case YongProtocol.DISCONNECT:
			roomNumber = chatServer.removeClient(nickName);
			if (roomNumber != -1) {
				chatServer.sendParticipate(roomNumber, message);
				chatServer.isBack(roomNumber, nickName, protocol);
				chatServer.sendAllRoom(YongProtocol.ROOMLIST + YongProtocol.DELEMETER + nickName);
			}
			chatServer.sendAllMessage(YongProtocol.UPDATELIST + YongProtocol.DELEMETER + nickName);

			setRunning(false);
			break;

		default:
			break;
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}

	@Override
	public void run() {
		recieveMessage();
	}

}