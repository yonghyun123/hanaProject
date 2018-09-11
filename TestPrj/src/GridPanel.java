import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;

public class GridPanel extends Panel {
	Button[] buttons;
	
	public GridPanel(){
		setLayout(new GridLayout(1, 1));
		buttons = new Button[100];
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button(i+1+"btn");
//			this.add(buttons[i]);
		}
		this.add(buttons[0]);
	}
	
	public static void main(String[] args) {
		Frame frame = new Frame("GridLayout Example");
		GridPanel gridPanel = new GridPanel();
		frame.add(gridPanel);
		
		frame.setSize(400,400);
		frame.setVisible(true);
		
	}
}
