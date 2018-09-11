import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 클라이언트의 데이터 수신 및 처리
 * @author 김용현
 *
 */
public class Client extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private boolean running;
	
	public Client(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		running = true;
	}
	
	public void recieve(){
		
		while(running) {
			String clientMsg = null;
			try {
				clientMsg = in.readLine();
				System.out.println("클라이언트로부터 수신 데이터:" + clientMsg);
				if(clientMsg.equalsIgnoreCase("quit")) {
					break;
				}
				out.println(clientMsg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {

			}

		}
	}
	
	@Override
	public void run() {
		recieve();
		
	}
}
