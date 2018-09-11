import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

public class URLExample {
	public static void main(String[] args) {
		String urlString = "https://www.daum.net/?t__nil_top=refresh";
		try {
			URL url = new URL(urlString);
			System.out.println(url.getProtocol());
			System.out.println(url.getHost());
			
			try(InputStreamReader ir = new InputStreamReader(url.openStream())){
				BufferedReader br = new BufferedReader(ir);
				while(br.readLine() != null) {
					System.out.println(br.readLine());
				}
			} catch (Exception e) {
				
			}

		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "서버를 찾을 수 없습니다");
		}
		
	}
}
