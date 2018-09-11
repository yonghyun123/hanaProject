package kr.or.kosta.yongchat.gui;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;

public class YongChatPanel extends Panel {

	// owner
	YongMainFrame mainFrame;
	
	//layout
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	
	//property
	public Button exitB, inviteB, sendB;
	public Choice choice;
	public TextArea messageTA;
	public List userList;
	public TextField sendTF;



	Image background;

	public YongChatPanel(YongMainFrame mainFrame) {
		this.mainFrame = mainFrame;
		exitB = new Button("나가기");
		inviteB = new Button("초대하기");
		sendB = new Button("전송");
		choice = new Choice();
		choice.add("전체");
		messageTA = new TextArea();
		userList = new List();
		sendTF = new TextField();

		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();

	}

	public void init() {
		setContents();
	}

	public void setContents() {

		Panel rightP = new Panel();
		rightP.add(userList);
		add(rightP, 3, 1, 3, 3, 0, 0);

		setLayout(gridBagLayout);
		add(inviteB, 0, 0, 1, 1, 0, 0);
		add(new Label(" "), 1, 0, 1, 1, 0, 0);
		add(exitB, 2, 0, 1, 1, 0, 0);

		// add(new Label(" "), 3,0,1,1,1,0);
		// add(new Label(" "), 4,0,1,1,1,0);

		add(messageTA, 0, 1, 3, 3, 0, 0);
		// add(userList,3,1,2,3,0,0);

		add(choice, 0, 4, 1, 1, 0, 0);
		add(sendTF, 1, 4, 1, 1, 1, 0);
		add(sendB, 2, 4, 1, 1, 0, 0);
		add(new Label(" "), 3, 4, 1, 1, 0, 0);
		add(new Label(" "), 4, 4, 1, 1, 0, 0);

	}

	/**
	 * component 추가하는 메소드
	 * 
	 * @param component
	 *            Component종류
	 * @param gridx
	 *            x축위치
	 * @param gridy
	 *            y축위치
	 * @param gridwidth
	 *            차지하는 폭
	 * @param gridheight
	 *            차지하는 높이
	 * @param weightx
	 *            x축 가중치
	 * @param weighty
	 *            y축 가중치
	 */
	private void add(Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty) {
		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.gridwidth = gridwidth;
		gridBagConstraints.gridheight = gridheight;
		gridBagConstraints.weightx = weightx;
		gridBagConstraints.weighty = weighty;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);// 여백조절

		gridBagLayout.setConstraints(component, gridBagConstraints);
		add(component);
	}

	public void eventRegist() {

	}

}
