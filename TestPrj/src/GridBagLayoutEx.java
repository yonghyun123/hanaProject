import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridBagLayoutEx extends Panel  {

	Button button1, button2, button3;
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	
	Label receiveUserL, fileL, titleL, blankL, blankL2;
	Button findB, sendB, cancelB;
	TextField receiveUserTF, fileTF, titleTF;
	TextArea contentTA;
	
	Panel centerP, bottomP;
	
	public GridBagLayoutEx(){
	
		button1 = new Button("btn1");
		button2 = new Button("btn2");
		button3 = new Button("btn3");
		
		//label
		receiveUserL = new Label("receiver", Label.RIGHT);
		fileL = new Label("attached file", Label.RIGHT);
		titleL = new Label("title",Label.RIGHT);
		blankL = new Label();
		blankL2 = new Label();
		
		//button
		findB = new Button("find");
		sendB = new Button("send");
		cancelB = new Button("cancel");
		
		//TextField
		receiveUserTF = new TextField();
		fileTF = new TextField();
		titleTF = new TextField();
		
		//TextArea
		contentTA = new TextArea();
		
		//panel
		centerP = new Panel();
		bottomP = new Panel();
		
		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		
	}
	
	public void setCenterPanel() {
		centerP.add(contentTA);
	}
	
	public void setBottomPanel() {
		bottomP.add(sendB);
		bottomP.add(cancelB);
	}
	
	public void setContents() {
		setLayout(gridBagLayout);
		

		this.add(receiveUserL,      0, 0, 1, 1, 0, 0);
		this.add(receiveUserTF,     1, 0, 1, 1, 0, 0);
		this.add(new Label(""), 2, 0, 1, 1, 0, 0);
		this.add(new Label(""), 3, 0, 1, 1, 0, 0);

		this.add(fileL,            0, 1, 1, 1, 0, 0);
		this.add(fileTF,           1, 1, 1, 1, 2, 0);
		this.add(findB,            2, 1, 1, 1, 1, 0);
		this.add(new Label(""),3, 1, 1, 1, 3, 0);

		this.add(titleL,           0, 2, 1, 1, 0, 0);
		this.add(titleTF,          1, 2, 3, 1, 0, 0);
		this.add(new Label("빈라벨"),2, 2, 1, 1, 0, 0);
		this.add(new Label("빈라벨"),3, 2, 1, 1, 0, 0);
		this.add(new Label(" "), 4, 2, 1, 1, 0, 0);
	}
	
	private void add(Component component, int gridx, int gridy, 
			int gridwidth, int gridheight, double weightx, double weighty) {
		//grid 배치는 gridBagConstrains에다가 해야함
		gridBagConstraints.gridx = gridx; //왼쪽으로 붙일때 1을 넘겨줌
		gridBagConstraints.gridy = gridy;
		//격자를 붙일때마다 동적으로 생성
		gridBagConstraints.gridwidth = gridwidth; //1개
		gridBagConstraints.gridheight = gridheight; //1개
		gridBagConstraints.weightx = weightx;
		gridBagConstraints.weighty = weighty;
		//나머지 여백 다채우는 기능
		gridBagConstraints.fill = gridBagConstraints.HORIZONTAL;
		//마진을 넣는 기능
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagLayout.setConstraints(component, gridBagConstraints);
		this.add(component);
	}
	
	public static void main(String[] args) {
		Frame frame = new Frame("GridLayout Example");
		GridBagLayoutEx gridPanel = new GridBagLayoutEx();
		gridPanel.setContents();
		gridPanel.setCenterPanel();
		gridPanel.setBottomPanel();
	
		frame.setLayout(new BorderLayout());
		frame.add(gridPanel, BorderLayout.NORTH);
		frame.add(new TextArea(), BorderLayout.CENTER);
		frame.add(gridPanel.bottomP,BorderLayout.SOUTH);
		frame.add(new Panel(), BorderLayout.EAST);
		frame.add(new Panel(), BorderLayout.WEST);

		frame.setSize(800,400);
//		component에 맞게 frame을 맞추는 기능
		frame.pack();
		frame.setVisible(true);
		
	}

}
