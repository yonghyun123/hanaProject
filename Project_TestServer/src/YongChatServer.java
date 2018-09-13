import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * 
 * @author 조희진
 *
 */
public class YongChatServer {

	public static final int PORT = 7777;
	private boolean running;
	private ServerSocket serverSocket;
	private Hashtable<String, YongClient> clients; // 대기방에 있는 클라이언트들
	private List<Hashtable<String, YongClient>> participate; // 채팅방에 있는 클라이언트들
	private Hashtable<String, YongRoom> rooms; // 방 목록

	private Hashtable<String, YongClient> tempH;

	public boolean isRunning() {
		return running;
	}

	public Hashtable<String, YongClient> getClients() {
		return clients;
	}

	public void startUp() throws IOException {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			throw new IOException("[" + PORT + "] 포트 충돌로 ChatServer를 구동할 수 없습니다.");
		}

		running = true;
		clients = new Hashtable<String, YongClient>();
		participate = new ArrayList<Hashtable<String, YongClient>>();
		rooms = new Hashtable<String, YongRoom>();
		System.out.println("BTS[" + PORT + "] ChatServer Start....");

		while (running) {
			try {
				Socket socket = serverSocket.accept();
				YongClient client = new YongClient(socket, this);
				client.start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void shutDown() {

	}

	public void addClient(YongClient client) {
		clients.put(client.getNickName(), client);
	}

	// 방 추가하기
	public void addRoom(String message) {
		String[] tokens = message.split(YongProtocol.DELEMETER);
		String nickName = tokens[1];
		String roomName = tokens[2];
		String maxRoomCnt = tokens[3];
		YongClient client = clients.get(nickName);

		tempH = new Hashtable<>();
		tempH.put(nickName, client);
		int roomNumber = participate.size();
		participate.add(roomNumber, tempH); // 방 번호에 해당하는 리스트에 클라이언트 추가하기
		clients.remove(nickName); // 방에 입장한 클라이언트를 대기 방의 클라이언트 목록에서 지우기
		rooms.put(nickName, new YongRoom(nickName, roomName, maxRoomCnt, String.valueOf(roomNumber))); // 추가

		// 방 번호 보내주기
		Enumeration<YongClient> clients = participate.get(roomNumber).elements();
		YongClient yongClient = clients.nextElement();
		yongClient.sendMessage(
				YongProtocol.ROOMNUMBER + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER + roomNumber);

	}

	// 방에 입장할 때 사람 추가하기
	public void addParticipate(int roomNumber, String nickName) {
		YongClient client = clients.get(nickName);

		tempH = new Hashtable<>();
		tempH.put(nickName, client);
		clients.remove(nickName);
		participate.get(roomNumber).put(nickName, client);
	}

	// 대기방에 있는 클라이언트들의 수
	public int getClientCount() {
		return clients.size();
	}

	// 채팅방에 있는 클라이언트들의 수
	public int getParticipateCount(int roomNumber) {
		return participate.get(roomNumber).size();
	}

	// 닉네임 중복 검사
	public boolean isExistNickName(String nickName) {

		for (int i = 0; i < participate.size(); i++) {
			Enumeration<YongClient> client = participate.get(i).elements();
			while (client.hasMoreElements()) {
				YongClient yongClient = (YongClient) client.nextElement();
				if (yongClient.getNickName().equals(nickName)) {
					return true;
				}
			}
		}
		return clients.containsKey(nickName);
	}

	// 모든 클라이언트들에게 메시지 보내기
	public void sendAllMessage(String message) {
		Enumeration<String> nick = getClients().keys();
		String nickNameAll = "";
		while (nick.hasMoreElements()) {
			String string = YongProtocol.DELEMETER + nick.nextElement();
			nickNameAll += string;
		}

		Enumeration<YongClient> e = clients.elements();
		while (e.hasMoreElements()) {
			YongClient client = e.nextElement(); // 현재 접속한 client들의 목록을 받음
			client.sendMessage(message + nickNameAll);
		}
		for (int i = 0; i < participate.size(); i++) {
			Enumeration<YongClient> client = participate.get(i).elements();
			while (client.hasMoreElements()) {
				YongClient yongClient = (YongClient) client.nextElement();
				yongClient.sendMessage(message + nickNameAll);
			}
		}

	}

	// 방안에 있는 클라이언트들끼리의 MultiChat
	public void sendAllMessageTest(String message) {
		String[] tokens = message.split(YongProtocol.DELEMETER);
		int roomNumber = Integer.parseInt(tokens[2]);
		Enumeration<YongClient> e = participate.get(roomNumber).elements();
		while (e.hasMoreElements()) {
			YongClient yongClient = (YongClient) e.nextElement();
			System.out.println(roomNumber + "에 들어와 있는 " + yongClient.getNickName());
			yongClient.sendMessage(message);
		}
	}

	// 방안에서 Secret Chat할 때 선택된 사람에게만 채팅메시지 전달하기
	public void sendOneClient(int roomNumber, String sender, String receiver, String message) {
		Enumeration<YongClient> client = participate.get(roomNumber).elements();
		while (client.hasMoreElements()) {
			YongClient yongClient = (YongClient) client.nextElement();
			if (yongClient.getNickName().equals(receiver)) {
				yongClient.sendMessage(YongProtocol.SECRET_CHAT + YongProtocol.DELEMETER + sender
						+ YongProtocol.DELEMETER + receiver + YongProtocol.DELEMETER + message);
			}
		}
	}

	// 방안에서 리스트 선택했을 때 초대된 사람에게만 메시지 보내기
	public void sendInvitedClient(int roomNumber, String nickName, String invited) {
		Enumeration<YongClient> client = clients.elements();
		while (client.hasMoreElements()) {
			YongClient yongClient = (YongClient) client.nextElement();
			if (yongClient.getNickName().equals(invited)) {
				yongClient.sendMessage(YongProtocol.INVITE + YongProtocol.DELEMETER + nickName + YongProtocol.DELEMETER
						+ roomNumber + YongProtocol.DELEMETER + invited);
			}
		}
	}

	// 거절했을 때 거절메시지를 보내기
	public void sendRejectMessage(int roomNumber, String sender, String receiver, String message) {
		Enumeration<YongClient> client = participate.get(roomNumber).elements();
		while (client.hasMoreElements()) {
			YongClient yongClient = (YongClient) client.nextElement();
			if (yongClient.getNickName().equals(receiver)) {
				yongClient.sendMessage(YongProtocol.INVITE_REJECT + YongProtocol.DELEMETER + sender
						+ YongProtocol.DELEMETER + receiver + YongProtocol.DELEMETER + message);
			}
		}
	}

	// 방 정보들 보내기
	public void sendAllRoom(String message) {
		Enumeration<YongRoom> room = rooms.elements();
		String roomsAll = "";
		while (room.hasMoreElements()) {
			YongRoom yongRoom = (YongRoom) room.nextElement();
			roomsAll += yongRoom.toString();
		}
		Enumeration<YongClient> e = clients.elements();
		while (e.hasMoreElements()) {
			YongClient client = e.nextElement(); // 현재 접속한 client들의 목록을 받음
			client.sendMessage(message + roomsAll);

		}

	}

	// 다시 대기실로 갈 때 방에서 삭제하고 대기실클라이언트 Hashtable에 추가
	public boolean isBack(int roomNumber, String nickName, int protocol) {
		YongClient client = participate.get(roomNumber).get(nickName);
		if (rooms.containsKey(nickName)) { // 방장이 나갈 시
			Enumeration<YongClient> client1 = participate.get(roomNumber).elements();
			while (client1.hasMoreElements()) {
				YongClient yongClient = (YongClient) client1.nextElement();
				if (!yongClient.getNickName().equals(nickName)) { // 방장 아닌 사람들은
																	// 대기실로
					clients.put(yongClient.getNickName(), yongClient);
					participate.get(roomNumber).remove(yongClient.getNickName());
					yongClient.sendMessage(YongProtocol.BACK + YongProtocol.DELEMETER + yongClient.getNickName()
							+ YongProtocol.DELEMETER + roomNumber + YongProtocol.DELEMETER + nickName);
				}
				yongClient.sendMessage(YongProtocol.OUT + YongProtocol.DELEMETER + yongClient.getNickName());
			}
			rooms.remove(nickName); // 방 삭제
			if (protocol != YongProtocol.DISCONNECT) { // DISCONNECT가 아니면 대기실로
														// 다시 돌려보내줌
				clients.put(nickName, client);
			}
			participate.get(roomNumber).remove(nickName);
			participate.remove(roomNumber);
			return true;
		} else { // 방장 아닌 사람이 나갈 시
			participate.get(roomNumber).remove(nickName);

			if (protocol != YongProtocol.DISCONNECT) { // DISCONNECT가 아니면 대기실로
														// 다시 돌려보내줌
				clients.put(nickName, client);
			} else {
				sendAllMessage(YongProtocol.DISCONNECT + YongProtocol.DELEMETER + nickName);
			}
			return false;
		}
	}

	// 방에 있는 클라이언트들의 닉네임, 현재인원 얻기
	public String participateAll(int roomNumber, String message) {
		String nickNameAll = ""; // 방에 있는 클라이언트들의 닉네임을 저장하기 위한 String

		int currentCnt = participate.get(roomNumber).size(); // 현재 방 인원수

		Enumeration<YongClient> e = participate.get(roomNumber).elements(); // 방번호에
																			// 해당하는
																			// 방에
																			// 있는
																			// 클라이언트들
		while (e.hasMoreElements()) {
			YongClient yongClient = (YongClient) e.nextElement();
			nickNameAll += YongProtocol.DELEMETER + yongClient.getNickName();
		}

		return currentCnt + nickNameAll;
	}

	// 방 입장,퇴장시 참여자들 정보 보내기
	public void sendParticipate(int roomNumber, String message) {
		String nickNameAll = ""; // 방에 있는 클라이언트들의 닉네임을 저장하기 위한 String
		String[] tokens = message.split(YongProtocol.DELEMETER);
		int protocol = Integer.parseInt(tokens[0]);

		Enumeration<YongClient> e = participate.get(roomNumber).elements(); // 방번호에
																			// 해당하는
																			// 방에
																			// 있는
																			// 클라이언트들
		while (e.hasMoreElements()) {
			YongClient yongClient = (YongClient) e.nextElement();
			nickNameAll += YongProtocol.DELEMETER + yongClient.getNickName();
		}

		Enumeration<YongClient> e1 = participate.get(roomNumber).elements();
		while (e1.hasMoreElements()) {
			YongClient client = e1.nextElement(); // 현재 방안의 client들의 목록을 받음
			if (protocol != YongProtocol.DISCONNECT) {
				client.sendMessage(message);
			}
			client.sendMessage(YongProtocol.UPDATE_INLIST + YongProtocol.DELEMETER + "nickName" + nickNameAll);
			System.out.println("방안에 있는 사람들!!!!!!!!!" + nickNameAll);
		}

	}

	// 방정보
	public String roomInfo(String roomName) {
		String info = "";
		Enumeration<YongRoom> room = rooms.elements();
		while (room.hasMoreElements()) {
			YongRoom yongRoom = (YongRoom) room.nextElement();
			if (yongRoom.getRoomName().contains(roomName)) {
				info += yongRoom.toString();
			}
		}
		System.out.println("방정보!!!!!!!!!!!!" + info);
		return info;
	}

	// 종료 시 대기실이나 대화방 참여자 정보에서 삭제
	public int removeClient(String nickName) {
		int roomNumber = 0;
		if (clients.containsKey(nickName)) { // 삭제하고자 하는 대상이 대기실에 있을 경우
			clients.remove(nickName);
			roomNumber = -1;
		} else {
			for (int i = 0; i < participate.size(); i++) { // 삭제하고자 하는 대상이 방안에
															// 있을 경우
				Enumeration<YongClient> client = participate.get(i).elements();
				while (client.hasMoreElements()) {
					YongClient yongClient = (YongClient) client.nextElement();
					if (yongClient.getNickName().equals(nickName)) {
						participate.get(i).remove(nickName);
						roomNumber = i;

					}
				}
			}

		}
		return roomNumber;

	}

}