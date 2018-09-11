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
   private Hashtable<String, YongClient> clients;
   private List<Hashtable<String, YongClient>> participate;
   private Hashtable<String, YongRoom> rooms;

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

   public void addRoom(String message) {
      String[] tokens = message.split(YongProtocol.DELEMETER);
      String nickName = tokens[1];
      String roomName = tokens[2];
      String maxRoomCnt = tokens[3];
      YongClient client = clients.get(nickName);
      Hashtable<String, YongClient> temp = new Hashtable<String, YongClient>();
      temp.put(nickName, client);
      participate.add(participate.size(), temp);
      clients.remove(nickName);
      rooms.put(nickName, new YongRoom(nickName, roomName, maxRoomCnt, String.valueOf(participate.size())));
   }

   public void addParticipate(int roomNumber, String nickName) {
      YongClient client = clients.get(nickName);
      Hashtable<String, YongClient> temp = new Hashtable<String, YongClient>();
      temp.put(nickName, client);
      clients.remove(nickName);
      participate.add(roomNumber, temp);
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

   public void sendParticipate(int roomNumber) {

   }
}