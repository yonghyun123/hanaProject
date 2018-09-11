package kr.or.kosta.yongchat.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class YongWaitingPanel extends Panel {

	// owner
	YongMainFrame mainFrame;
	
	//layout
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	
	// properties
	public Button roomSearchB, enterB, makeB;
	public Label searchL, roomSearchL, waitingL, participateL;
	public TextField roomSearchTF;
	public List roomList;
	public List waitingList;
	public List participateList;
	
	public YongWaitingPanel(YongMainFrame mainFrame) {
		this.mainFrame = mainFrame;

		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		roomSearchB = new Button("검색");
		enterB = new Button("입장");
		makeB = new Button("방만들기");

		searchL = new Label("방제목");
		waitingL = new Label("대기인원");
		participateL = new Label("참여인원");

		roomSearchTF = new TextField(1);
		roomList = new List();
		waitingList = new List();
		participateList = new List();

	}

	public void init() {
		setContents();
		eventRegist();
	}

	public void setContents() {

		Panel southP = new Panel();
		southP.add(enterB);
		southP.add(makeB);

		setLayout(gridBagLayout);
		add(new Label(" "), 0, 0, 1, 1, 0, 0);
		add(searchL, 1, 0, 2, 1, 0, 0);
		add(new Label(" "), 3, 0, 1, 1, 0, 0);
		add(roomSearchTF, 4, 0, 5, 1, 0, 0);
		add(new Label(" "), 9, 0, 1, 1, 0, 0);
		add(roomSearchB, 10, 0, 2, 1, 0, 0);
		add(new Label(" "), 12, 0, 1, 1, 0, 0);
		add(new Label(" "), 13, 0, 1, 1, 0, 0);

		add(waitingL, 14, 0, 5, 1, 0, 0);
		add(roomList, 0, 1, 14, 11, 0, 0);

		add(waitingList, 14, 1, 5, 5, 0, 0);

		add(participateL, 14, 6, 5, 1, 0, 0);
		add(participateList, 14, 7, 5, 5, 0, 0);

		add(southP, 0, 12, 19, 1, 0, 0);

	}

	private void add(Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty) {

		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.gridheight = gridheight;
		gridBagConstraints.gridwidth = gridwidth;
		gridBagConstraints.weightx = weightx;
		gridBagConstraints.weighty = weighty;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);

		gridBagLayout.setConstraints(component, gridBagConstraints);
		add(component);
	}

	public void eventRegist() {
		// 입장 버튼 눌렸을때 이벤트
		enterB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeCard("CHAT");
			}
		});
	}

}