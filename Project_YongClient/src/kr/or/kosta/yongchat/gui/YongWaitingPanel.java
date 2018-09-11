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

import kr.or.kosta.yongchat.gui.dialog.YongRoomDialog;
import kr.or.kosta.yongchat.gui.dialog.YongRoomDialogFrame;

public class YongWaitingPanel extends Panel {

	// owner
	YongMainFrame mainFrame;

	// layout
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;

	// properties
	public Button roomSearchB, enterB, makeB;
	public Label searchL, roomSearchL, waitingL, participateL;
	public Label roomNumL, leaderL, roomTitleL, MaxNumL;
	public TextField roomSearchTF;
	public List roomList;
	public List waitingList;
	public List participateList;

	// Component
	YongRoomDialogFrame dialogFrame;

	public YongWaitingPanel(YongMainFrame mainFrame) {
		this.mainFrame = mainFrame;

		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		roomSearchB = new Button("검색");
		enterB = new Button("입장");
		makeB = new Button("방만들기");

		searchL = new Label("방제목");
		waitingL = new Label("대기인원");
		waitingL.setAlignment(Label.RIGHT);
		participateL = new Label("참여인원");
		participateL.setAlignment(Label.RIGHT);

		roomNumL = new Label("방번호");
		leaderL = new Label("방장");
		roomTitleL = new Label("방제목");
		MaxNumL = new Label("최대인원");

		roomSearchTF = new TextField(3);
		roomList = new List();
		waitingList = new List();
		participateList = new List();

		dialogFrame = new YongRoomDialogFrame();

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

		add(searchL, 0, 0, 1, 1, 0, 0);
		add(roomSearchTF, 1, 0, 3, 1, 2, 0);

		add(roomSearchB, 4, 0, 1, 1, 0, 0);

		add(roomNumL, 0, 1, 1, 1, 1, 0);
		add(leaderL, 1, 1, 1, 1, 1, 0);
		add(roomTitleL, 2, 1, 1, 1, 1, 0);
		add(MaxNumL, 3, 1, 1, 1, 1, 0);

		add(waitingL, 4, 1, 2, 1, 0, 0);
		add(waitingList, 4, 2, 2, 1, 1, 1);

		add(roomList, 0, 2, 4, 3, 2, 2);

		add(participateL, 4, 3, 2, 1, 0, 0);
		add(participateList, 4, 4, 2, 1, 1, 1);

		add(southP, 0, 5, 8, 1, 0, 0);

	}

	private void add(Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty) {

		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		gridBagConstraints.gridheight = gridheight;
		gridBagConstraints.gridwidth = gridwidth;
		gridBagConstraints.weightx = weightx;
		gridBagConstraints.weighty = weighty;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
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