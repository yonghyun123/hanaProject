import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * 호스트 네임으로 IP얻어오기
 * @author 김용현
 *
 */
public class InetAddressExample {
	
	public static void main(String[] args) throws UnknownHostException {
		
		InetAddress ia = InetAddress.getLocalHost();
		System.out.println(ia.getHostAddress());
		
		InetAddress ia2 = InetAddress.getByName("www.naver.com");
		System.out.println(ia2.getHostAddress());
		
		InetAddress[] ias = InetAddress.getAllByName("www.naver.com");
		
		for (InetAddress inetAddress : ias) {
			System.out.println(inetAddress);
		}
	}
}
