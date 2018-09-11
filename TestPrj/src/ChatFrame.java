import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

public class ChatFrame extends Frame {
	Label serverL; 
	TextField serverTF, inputTF;
	Button connectB, sendB;
	TextArea messageTA;
	List userList;
	
	//생성자
	public ChatFrame()  {
		this("이름없음");
	}
	
	public ChatFrame(String title)  {
		super(title);
			
		serverL = new Label("서버");
		serverTF = new TextField();
		inputTF = new TextField();
		//button customizing
		connectB = new Button("connect.."){
			@Override
			public void paint(Graphics g) {
				g.drawLine(50, 50, 200, 200);
			}
		};
		sendB = new Button("send!!");
		messageTA = new TextArea();
		userList = new List();
		userList.add("말미잘");
		userList.add("꼴뚝히");
		userList.add("멀절이");
	}
	
	// 초기화
	public void init() {
		this.setSize(400, 500);
		this.setVisible(true);
	}
	
	// 화면 배치
	public void setContents() {
		//색상설정
//		connectB.setEnabled(false);
//		connectB.setBackground(new Color(100, 50, 120));
//		connectB.setBackground(Color.red);
		
		connectB.setFont(new Font("궁서", Font.BOLD, 20));
		
		//레이아웃매니저 교체
		Panel northP = new Panel();
		northP.setLayout(new BorderLayout());
		northP.add(serverL, BorderLayout.WEST);
		northP.add(serverTF, BorderLayout.CENTER);
		northP.add(connectB, BorderLayout.EAST);
		
		Panel southP = new Panel();
		southP.setLayout(new BorderLayout());
		southP.add(inputTF, BorderLayout.CENTER);
		southP.add(sendB, BorderLayout.EAST);
		
		this.add(northP, BorderLayout.NORTH);
		this.add(messageTA, BorderLayout.CENTER);
		this.add(userList, BorderLayout.EAST);
		this.add(southP, BorderLayout.SOUTH);
		
//		this.setColorAll(Color.blue);
		this.eventRegist();
	}
	
	//frame 가운데에 맞추기
	public void setCenter() {
//		Runtime.getRuntime().
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		int x = (dim.width - getSize().width)/2;
		int y = (dim.height - getSize().height)/2;
		this.setLocation(x, y);
		
	}
	
	public void finish(){
		this.setVisible(false);
		dispose();
		System.exit(0);
	}
	
	public void appendMessage(){
		String message = inputTF.getText();
//		messageTA.setText(message);
		messageTA.append(message+"\n");
		inputTF.setText("");
	}
	
	public void eventRegist(){
		/** 이름있는 지역내부 클래스 */
		class Exiter extends WindowAdapter{
			public void windowClosing(WindowEvent e){
				finish();
			}	
		}

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				finish();
			}
		});
		
		inputTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appendMessage();
			}
		});
		
		inputTF.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				System.out.println(inputTF.getText());
			}
		});
		
		sendB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appendMessage();
			}
		});
		
		serverTF.addKeyListener(new KeyListener(){
			
			@Override
			public void keyTyped(KeyEvent e) {
//				System.out.println(e.getKeyCode());
				System.out.println(e.getKeyChar());
				System.out.println(KeyEvent.VK_ENTER);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		userList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange() == ItemEvent.SELECTED){
					String name = userList.getSelectedItem();
//					JOptionPane.showMessageDialog(null, name+"님 선택이요", "알림", JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog(null, name+"님 선택이요", "알림", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
	}
	
	//전체 컴포넌트 색상 설정
	private void setColorAll(Color bg) {
		Component[] components = this.getComponents();
		
		for (Component component : components) {
			if(component instanceof Panel) {
				Component[] cs = ((Panel) component).getComponents();
				for (Component component2 : cs) {
					component2.setBackground(bg);
				}
			}
			
			component.setBackground(bg);
		}
	}
	
	public static void main(String[] args) {
		ChatFrame userFrame = new ChatFrame("Kotalk");
		userFrame.init();
		userFrame.setCenter();
		userFrame.setContents();
	}
	
	/**
	 * 멤버 내부 클래스를 이용한 이벤트 처리
	 * @author yonghyun
	 *
	 */


}
	


