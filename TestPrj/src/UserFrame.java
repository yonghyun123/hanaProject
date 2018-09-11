import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class UserFrame extends Frame implements MouseListener, WindowListener{
	
	Button eButton;
	Button wButton;
	Button sButton;
	Button nButton;
	Button cButton;
	
	//생성자
	public UserFrame()  {
		this("이름없음");
	}
	
	public UserFrame(String title)  {
		super(title);
		this.eButton = new Button("East");
		this.wButton = new Button("West");
		this.sButton = new Button("South");
		this.nButton = new Button("North");
		this.cButton = new Button("Center");
		
	}
	
	// 초기화
	public void init() {
		this.setSize(800, 500);
		this.setVisible(true);
	}
	
	// 화면 배치
	public void setContents() {
		//레이아웃매니저 교체
		this.setLayout(new FlowLayout());
		this.add(eButton, BorderLayout.EAST);
		this.add(wButton, BorderLayout.WEST);
		this.add(sButton, BorderLayout.SOUTH);
		this.add(nButton, BorderLayout.NORTH);
		this.add(cButton, BorderLayout.CENTER);
		//이벤트소스에 이벤트리스너 연결 
		
	}
	
	public void eventRegist() {
		eButton.addMouseListener(this);
		wButton.addMouseListener(this);
		sButton.addMouseListener(this);
		nButton.addMouseListener(this);
		cButton.addMouseListener(this);
		
		this.addWindowListener(this);
	}
	
	public static void main(String[] args) {
		UserFrame userFrame = new UserFrame("타이틀입니다.");
		userFrame.init();
		userFrame.setContents();
		userFrame.eventRegist();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
//		System.out.println("마우스 클릭되었습니다.");
		System.out.println(e.getSource());
		Object eventSource = e.getSource();
		Button button = (Button)eventSource;
		int r = (int)(Math.random()*256);
		int g = (int)(Math.random()*256);
		int b = (int)(Math.random()*256);
		button.setBackground(new Color(r, g, b));
		
		if(eventSource == eButton) {
			System.out.println("East clicked!!");
		} else if(eventSource == wButton) {
			System.out.println("West clicked!!");
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("mousePressed Called..");
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("mouseReleased Called..");
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("mouseEntered Called..");
	}
	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("mouseExited Called..");
	}
	
//	창이 처음 열릴 때
	@Override
	public void windowOpened(WindowEvent e) {
		System.out.println("창이열렸습니다.");
	}
//  창 닫을때
	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
		dispose(); //os에 그래픽 리소스 반납
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	
	}
	
//	최소화 버튼 눌렀을때
	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
//	윈도우창 활성화
	public void windowActivated(WindowEvent e) {}

	@Override
//	윈도우창 비활성화
	public void windowDeactivated(WindowEvent e) {}
	
}
