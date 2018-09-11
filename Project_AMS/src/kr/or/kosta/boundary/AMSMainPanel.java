package kr.or.kosta.boundary;

import java.awt.Button;
import java.awt.Choice;
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


public class AMSMainPanel extends Panel{
	//Properties
	//Labels
	Label accountTypeL;
	Label accountNumL;
	Label accountNameL;
	Label accountPasswdL;
	Label loanMoneyL;
	Label accountListL;
	Label depositL;
	Label unitWonL;
	
	//ComboBox
	Choice accountTypeC;
	
	//TextFields
	TextField accountNumTF;
	TextField accountNameTF;
	TextField accountPasswdTF;
	TextField loanMoneyTF;
	TextField depositTF;
	
	//Buttons
	Button getAccountB;
	Button searchAccountB;
	Button deleteAccountB;
	Button addAccountB;
	Button getAllAccountB;
	
	//TextArea
	TextArea accountListTA;
	
	//layout property
	GridBagLayout gridBagLayout;
	GridBagConstraints gridBagConstraints;
	
	public AMSMainPanel(){
		accountTypeL = new Label("계좌종류");
		accountNumL = new Label("계좌번호");
		accountNameL = new Label("예금주명");
		accountPasswdL = new Label("비밀번호");
		loanMoneyL = new Label("대출금액");
		accountListL = new Label("계좌목록");
		depositL = new Label("입금금액", Label.CENTER);
		unitWonL = new Label("(단위: 원)", Label.RIGHT);
		
		//ComboBox
		accountTypeC = new Choice();
		accountTypeC.add("전체");
		accountTypeC.add("입출금계좌");
		accountTypeC.add("마이너스계좌");
		
		//TextFields
		accountNumTF = new TextField();
		accountNameTF = new TextField();;
		accountPasswdTF = new TextField();
		loanMoneyTF = new TextField();
		depositTF = new TextField();
		
		//Buttons
		getAccountB = new Button("조회");
		searchAccountB = new Button("검색");
		deleteAccountB = new Button("삭제");
		addAccountB = new Button("신규등록");
		getAllAccountB = new Button("전체조회");
		
		//TextArea
		accountListTA = new TextArea();
		
		//layout property
		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
	}
	
	public void init(){
		this.setContents();
		this.loanMoneyTF.setEnabled(false);
		this.addAccountB.setEnabled(false);
	}
	
	public void setContents(){
		setLayout(gridBagLayout);
		//row 1
		this.setComponentProp(accountTypeL,     0, 0, 1, 1, 0, 0);
		this.setComponentProp(accountTypeC,     1, 0, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	2, 0, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	3, 0, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	4, 0, 1, 1, 0, 0);
		
		//row 2
		this.setComponentProp(accountNumL,      0, 1, 1, 1, 0, 0);
		this.setComponentProp(accountNumTF,     1, 1, 1, 1, 0, 0);
		this.setComponentProp(getAccountB,      2, 1, 1, 1, 0, 0);
		this.setComponentProp(deleteAccountB,   3, 1, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	4, 1, 1, 1, 0, 0);
		
		//row3
		this.setComponentProp(accountNameL,     0, 2, 1, 1, 0, 0);
		this.setComponentProp(accountNameTF,    1, 2, 1, 1, 0, 0);
		this.setComponentProp(searchAccountB,   2, 2, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	3, 2, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	4, 2, 1, 1, 0, 0);
		
		//row4
		this.setComponentProp(accountPasswdL,   0, 3, 1, 1, 0, 0);
		this.setComponentProp(accountPasswdTF,  1, 3, 1, 1, 0, 0);
		this.setComponentProp(depositL,         2, 3, 1, 1, 0, 0);
		this.setComponentProp(depositTF,		3, 3, 2, 1, 0, 0);
		this.setComponentProp(new Label(""), 	4, 3, 1, 1, 0, 0);
		//row5
		this.setComponentProp(loanMoneyL, 		0, 4, 1, 1, 0, 0);
		this.setComponentProp(loanMoneyTF, 		1, 4, 1, 1, 0, 0);
		this.setComponentProp(addAccountB, 		2, 4, 1, 1, 0, 0);
		this.setComponentProp(getAllAccountB, 	3, 4, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	4, 4, 1, 1, 0, 0);
		//row6
		this.setComponentProp(accountListL, 	0, 5, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	1, 5, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	2, 5, 1, 1, 0, 0);
		this.setComponentProp(new Label(""), 	3, 5, 1, 1, 0, 0);
		this.setComponentProp(unitWonL,     	4, 5, 1, 1, 0, 0);
		//row7
		this.setComponentProp(accountListTA, 	0, 6, 5, 1, 0, 0);
		

	}
	
	//컴포넌트 속성 설정하기
	private void setComponentProp(Component component, int gridx, int gridy, 
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
}
