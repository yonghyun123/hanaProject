package kr.or.kosta.yongchat.gui;
import java.awt.Button;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class YongJoinPanel extends Panel{
	//Owner
	YongMainFrame mainFrame;
	
	//layout
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	
	//properties
	public Label nickNameL;
	public TextField nickNameTF;
	public Button joinB;
	JPanel panel;
	Image yongLeft, yongChat, yongRight;
	
	public YongJoinPanel(YongMainFrame frame){
		this.mainFrame = frame;
		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		nickNameL = new Label("대화명을 입력하세요");
		nickNameTF = new TextField();
		joinB = new Button("입장하기");
		
		yongLeft = Toolkit.getDefaultToolkit().getImage("bin/resources/yong_left.png");
	    yongChat = Toolkit.getDefaultToolkit().getImage("bin/resources/yong_logo.png");
	    yongRight = Toolkit.getDefaultToolkit().getImage("bin/resources/yong_right.png");

	}
	
	public void init(){
		setContents();
		eventRegist();
	}
	
	public void setContents() {
		setLayout(gridBagLayout);
	    add(new Label(""), 0, 1, 1, 1, 0, 0);
	    add(new Label(" "), 0, 2, 1, 1, 0, 0);
	    add(nickNameL,   0, 3, 2, 1, 0, 0);

	    add(nickNameTF,  0, 4, 2, 1, 0, 0);
	    add(joinB,  2, 4, 1, 1, 0, 0);
		
	}
	
	private void add(Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty) {
		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.gridwidth = gridwidth;
		gridBagConstraints.gridheight = gridheight;
		gridBagConstraints.weightx = weightx;
		gridBagConstraints.weighty = weighty;
		gridBagConstraints.fill = gridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(4, 1, 4, 1);
		
		gridBagLayout.setConstraints(component, gridBagConstraints);		
		add(component);
	}
	
	@Override
    public void paint(Graphics g) {
       g.drawImage(yongLeft,10 , 150,110,110, this);
       g.drawImage(yongChat,50 , 0,380,170, this);
       g.drawImage(yongRight,370 , 150,110,110, this);
    }
	
	public void eventRegist(){
		joinB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.connect();
				
			}
		});
		nickNameTF.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.connect();
			}
		});
	}
}
