import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

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
		for (Hashtable<String, YongClient> hashtable : participate) {
			if (hashtable.containsKey(nickName)) {
				return false;
			}
		}
		return clients.containsKey(nickName);
	}

	// 클라이언트 지우기
	public void removeClient(YongClient client) {

		// 방안에 있는 클라이언트들도 확인해야함!!!!!!!!!!
		clients.remove(client.getNickName(), client);
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

	// 방안에서 Secret Chat할 때 선택된 사람에게만 메시지 보내기
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

	// 방 입장시 참여자들 정보 보내기
	public void sendParticipate(int roomNumber, String message) {
		String[] tokens = message.split(YongProtocol.DELEMETER);
		System.out.println("입장한 방 번호 :" + roomNumber);

		String nickNameAll = "";
		Enumeration<YongClient> e = participate.get(roomNumber).elements();
		while (e.hasMoreElements()) {
			YongClient yongClient = (YongClient) e.nextElement();
			System.out.println(yongClient.getNickName());
			nickNameAll += YongProtocol.DELEMETER + yongClient.getNickName();
		}
		// System.out.println(nickNameAll);
		Enumeration<YongClient> e1 = participate.get(roomNumber).elements();
		while (e1.hasMoreElements()) {
			YongClient client = e1.nextElement(); // 현재 방안의 client들의 목록을 받음
			client.sendMessage(message + nickNameAll);
		}

	}
}