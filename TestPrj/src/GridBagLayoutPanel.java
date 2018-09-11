import java.awt.Button;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;

public class GridBagLayoutPanel extends Panel {
	
	Button button1, button2, button3;
	
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	
	public GridBagLayoutPanel(){
		button1 = new Button("btn1");
		button2 = new Button("btn2");
		button3 = new Button("btn3");
		
		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		
	}
	
	public void setContents() {
		setLayout(gridBagLayout);
		
	/*	//grid 배치는 gridBagConstrains에다가 해야함
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		//격자를 붙일때마다 동적으로 생성
		gridBagConstraints.gridwidth = 1; //1개
		gridBagConstraints.gridheight = 1; //1개
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		gridBagLayout.setConstraints(button1, gridBagConstraints);
		this.add(button1);
	*/
		this.add(button1, 0, 0, 10, 1, 1, 0);
		this.add(button2, 0, 1, 1, 1, 1, 0);
		this.add(button3, 1, 1, 1, 1, 0, 0);

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
//		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagLayout.setConstraints(component, gridBagConstraints);
		this.add(component);
	}
	
	public static void main(String[] args) {
		Frame frame = new Frame("GridLayout Example");
		GridBagLayoutPanel gridPanel = new GridBagLayoutPanel();
		gridPanel.setContents();
		frame.add(gridPanel);
		
		frame.setSize(400,400);
//		component에 맞게 frame을 맞추는 기능
//		frame.pack();
		frame.setVisible(true);
		
	}
}
