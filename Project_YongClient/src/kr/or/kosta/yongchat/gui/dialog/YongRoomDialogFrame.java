package kr.or.kosta.yongchat.gui.dialog;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class YongRoomDialogFrame extends Frame{
	public YongRoomDialog roomDialog;
	
	
	public YongRoomDialogFrame() {
		roomDialog = new YongRoomDialog();
	}
	
	public void init(){
		this.setSize(300, 200);
		this.setVisible(true);
		this.setCenter();
		this.add(roomDialog);

		// event listener
		eventRegist();
	}
	
	public void setCenter() {
		// Runtime.getRuntime().
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		int x = (dim.width - getSize().width) / 2;
		int y = (dim.height - getSize().height) / 2;
		this.setLocation(x, y);
	}
	
	public void finish() {
		setVisible(false);
		dispose();
//		System.exit(0);
	}

	public void eventRegist() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				finish();
			}
		});
	}
}
