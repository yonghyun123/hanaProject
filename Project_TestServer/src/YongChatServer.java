import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class YongChatServer {

	public static final int PORT = 7777;
	private boolean running;
	private ServerSocket serverSocket;
	private Hashtable<String, YongClient> clients;
	private String nickNameAll;

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

	public int getClientCount() {
		return clients.size();
	}

	public boolean isExistNickName(String nickName) {
		return clients.containsKey(nickName);
	}

	public void removeClient(YongClient client) {
		clients.remove(client.getNickName(), client);
	}

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
}