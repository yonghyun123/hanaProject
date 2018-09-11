package kr.or.kosta.yongchat.gui.dialog;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;

public class YongRoomDialog extends Panel {
	
	public Label nameL, numberL;
	public TextField roomNameTF;
	public Choice numberC;
	public Button createB;
	
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;


	public YongRoomDialog() {
		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();

		nameL = new Label("방 제목을 입력해주세요.");
		numberL = new Label("최대 채팅 인원을 설정해주세요");
		roomNameTF = new TextField(1);
		numberC = new Choice();

		for (int num = 2; num < 11; num++) {
			numberC.add(String.valueOf(num));
		}

		createB = new Button("만들기");

		setContests();
		setCenter();

	}

	public void setContests() {
		setLayout(gridBagLayout);
		add(nameL, 0, 0, 1, 1, 0, 0);
		add(roomNameTF, 0, 1, 10, 1, 0, 0);
		add(numberL, 0, 2, 10, 1, 0, 0);
		add(numberC, 0, 3, 2, 1, 0, 0);
		add(createB,0, 4, 1, 1, 0, 0);
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
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);

		gridBagLayout.setConstraints(component, gridBagConstraints);
		add(component);
	}

	public void setCenter() {
		Toolkit.getDefaultToolkit().beep();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		int x = (dim.width - getSize().width) / 2;
		int y = (dim.height - getSize().height) / 2;
		setLocation(x, y);

	}

}