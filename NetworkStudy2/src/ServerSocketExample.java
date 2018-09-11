import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerSocketExample {
	public static final int port = 7777;
	
	public static void main(String[] args) {
		boolean running = true;
		InputStream in = null;
		OutputStream out = null;
		Socket socket = null;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("server running...");
			while(running) {
				socket = serverSocket.accept();
				InetAddress ia = socket.getInetAddress();
				System.out.println(ia.toString()+"클라이언트가 연결해옴...그리고 졸림... 눈이 기암긴디아......."
						+ "멈춰... 이제 그만....");
				 
				Client client = new Client(socket);
				client.start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		
		}
	}
}
