package kr.or.kosta.yongchat.gui;
import java.awt.Button;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	
	public YongJoinPanel(YongMainFrame frame){
		this.mainFrame = frame;
		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		nickNameL = new Label("대화명을 입력하세요");
		nickNameTF = new TextField();
		joinB = new Button("입장하기");
		
	}
	
	public void init(){
		setContents();
		eventRegist();
	}
	
	public void setContents() {
		setLayout(gridBagLayout);
		add(new Label(" "),   0, 0, 1, 1, 0, 0);
		add(nickNameL,   1, 0, 1, 1, 0, 0);
		
		
		add(nickNameTF,  0, 1, 2, 1, 0, 0);
		add(new Label(" "), 1, 1, 1, 1, 0, 0);
		
		
		Panel buttonPanel = new Panel();
		buttonPanel.add(joinB);
		add(buttonPanel, 0, 2, 3, 1, 0, 0);
		
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
	
	public void eventRegist(){
		joinB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ddd");
				mainFrame.connect();
				
				
			}
		});
	}
}
