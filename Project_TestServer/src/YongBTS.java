
import java.io.IOException;

public class YongBTS {
	
	public static void main(String[] args) {
		YongChatServer chatServer = new YongChatServer();
		try {
			chatServer.startUp();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
