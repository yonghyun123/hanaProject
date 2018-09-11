package kr.or.kosta.boundary;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JOptionPane;

import kr.or.kosta.entity.Account;
import kr.or.kosta.entity.AccountManager;
import kr.or.kosta.entity.MinusAccount;
import kr.or.kosta.exception.AccountException;
import kr.or.kosta.util.AMSRegExpr;

public class AMSMainFrame extends Frame {
	//Component
	AMSMainPanel mainPanel;
	AccountManager manager;
	
	//Constants
	private static final String TOTAL = "전체";
	private static final String GENERAL_ACCOUNT = "입출금계좌";
	private static final String MINUS_ACCOUNT = "마이너스계좌";
	
	//Data
	private String appendingData;
	private String accountNum; 
	private String accountOwner;
	private int passwd; 
	private int restMoney;
	private int borrowMoney;
	
	//생성자
	public AMSMainFrame(){
		mainPanel = new AMSMainPanel();
	}
	//화면 초기화
	public void init(){
		this.setDisplaySize();
		this.setCenter();
		this.add(mainPanel);
		this.mainPanel.init();
		this.eventRegist();
	}
	
	//가운데 정렬(MacOS 버전)
	public void setCenter() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - getSize().width)/5;
		int y = (dim.height - getSize().height)/5;
		this.setLocation(x, y);
		
	}
	//화면 크기 설정
	public void setDisplaySize(){
		this.setSize(700, 500);
		this.setVisible(true);
	}
	
	//출력 데이터 포맷 (top)
	public void setDataFormatTop(){
		String formatTop = "------------------------------------------------------------------\n"
				+ "계좌종류" + "\t" + "계좌번호"+ "\t\t" + "예금주명"+ "\t"
				+ "현재잔액" + "\t" + "대출금액\n"
				+ "=====================================================\n";
				
		mainPanel.accountListTA.setText(formatTop);
		
	}
	//출력 데이터 포맷(bottom)
	public String getDataFormatBottom(){
		String formatBottom = "------------------------------------------------------------------";
		return formatBottom;
	}
	
	//전체 TextArea Clear
	public void testAreaClear(){
		mainPanel.accountNameTF.setText("");
		mainPanel.accountNumTF.setText("");
		mainPanel.accountPasswdTF.setText("");
		mainPanel.depositTF.setText("");
		mainPanel.loanMoneyTF.setText("");
	}
	
	//알림 메시지 dialog
	public void showDialog(String msg){
		JOptionPane.showMessageDialog(null, msg, "알림", JOptionPane.INFORMATION_MESSAGE);
	}
	//프레임 끄기 기능
	public void finish(){
		this.setVisible(false);
		this.dispose();
		System.exit(0);
	}
	
	//이벤트 등록
	public void eventRegist(){
		/*
		 * 1. 기본기능(검색, 삭제, 조회, 전체조회, 신규등록)
		 * 2. 상세기능(개발시 주의할점) 
		 *  - 최초 GUI 호출시 신규등록버튼, 입금금액, 대출금액 비활성화(콤보박스가 전체로 되어있을시)
		 *  - 정규식을 통한 input 이벤트 예외처리(alert) 
		 *  - 출력시 계좌종류로 정렬
		 *  - MacOS 프레임 가운데 정렬(window처리와 다름)
		 */
		/**
		 * frame WindowListener
		 * 
		 */
		// 프레임 끄기 이벤트
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				finish();
			}
		});
		
		//계좌번호 삭제 이벤트
		mainPanel.deleteAccountB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appendingData = "";
				setDataFormatTop(); // 출력데이터 포맷 메서드 호출
				//data logic
				String deleteAccountNum = mainPanel.accountNumTF.getText();
				if(manager.remove(deleteAccountNum.trim())){
					showDialog(deleteAccountNum+"님의 계좌가 삭제되었습니다.");
				} else {
					showDialog(deleteAccountNum+"님의 계좌가 조회되지 않습니다.");
				}
				//TestArea clear
				testAreaClear();
			}
		});
		
		//예금주명 검색 이벤트
		mainPanel.searchAccountB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appendingData = "";
				setDataFormatTop(); // 출력데이터 포맷 메서드 호출
				//data logic
				String findAccountName = mainPanel.accountNameTF.getText();
				List<Account> findAccountList = manager.search(findAccountName.trim()); //공백문자 제거
				for (Account account : findAccountList) {
					appendingData += account.toString() + "\n";
				}
				//마지막 데이터 포맷 append
				appendingData += getDataFormatBottom();
				mainPanel.accountListTA.append(appendingData);
				
				//TestArea clear
				testAreaClear();
			}
		});
		
		//계좌종류 타입 아이템 이벤트
		mainPanel.accountTypeC.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getItem().toString().equals(TOTAL)){
					mainPanel.addAccountB.setEnabled(false);
					mainPanel.loanMoneyTF.setEnabled(false);
				} else if(e.getItem().toString().equals(GENERAL_ACCOUNT)){
					mainPanel.addAccountB.setEnabled(true);
					mainPanel.loanMoneyTF.setEnabled(false);
				} else {
					mainPanel.addAccountB.setEnabled(true);
					mainPanel.loanMoneyTF.setEnabled(true);
				}
			}
		});
		
		//계좌번호 검색 이벤트
		mainPanel.getAccountB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appendingData = "";
				setDataFormatTop(); // 출력데이터 포맷 메서드 호출
				//data logic
				String findAccountNum = mainPanel.accountNumTF.getText();
				Account findAccount = manager.get(findAccountNum.trim()); //공백문자 제거
				if(findAccount != null){
					appendingData += findAccount.toString() + "\n";
				}
				
				//마지막 데이터 포맷 append
				appendingData += getDataFormatBottom();
				mainPanel.accountListTA.append(appendingData);
				
				//TestArea clear
				testAreaClear();
				
			}
		});
		
		//전체 조회 이벤트
		mainPanel.getAllAccountB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appendingData = "";
				setDataFormatTop(); // 출력데이터 포맷 메서드 호출
				
				//data logic
				for (Account account : manager.list()) {
					appendingData += account.toString() + "\n";
				}
				//마지막 데이터 포맷 append
				appendingData += getDataFormatBottom();
				mainPanel.accountListTA.append(appendingData);
				
				testAreaClear();
		
			}
		});
		
		//신규가입 버튼 이벤트
		mainPanel.addAccountB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mainPanel.accountTypeC.getSelectedItem().equals(GENERAL_ACCOUNT)){
					try {
						addAccountByBtn(GENERAL_ACCOUNT);				
						showDialog("입출금 계좌를 개설하였습니다.");
					} catch (AccountException e1) {
						
					}
				} else if(mainPanel.accountTypeC.getSelectedItem().equals(MINUS_ACCOUNT)){
					try {
						addAccountByBtn(MINUS_ACCOUNT);
						showDialog("마이너스 계좌를 개설하였습니다.");
					} catch (AccountException e1) {

					}
				} 
			}
		});
	}
	
	// 신규 등록 버튼 눌렀을 시, 정규식을 통한 예외처리 후 계좌 등록
	public void addAccountByBtn(String accountType) throws AccountException{
		/**
		 * 정규식을 사용해 예외처리 구문
		 */
		//계좌정보가 xxxx-xxxx-xxxx 형식인지 파악
		if(!AMSRegExpr.isAccountNum(mainPanel.accountNumTF.getText())){
			throw new AccountException("올바른 계좌정보가 아닙니다.", -200);
		}
		//이름정보가 한글인지 파악
		if(!AMSRegExpr.isAccountName(mainPanel.accountNameTF.getText())){
			throw new AccountException("올바른 이름정보가 아닙니다.", -200);
		}
		//비밀번호 정보가 숫자만 들어가있는지 파악
		if(!AMSRegExpr.isAccountPasswd(mainPanel.accountPasswdTF.getText())){
			throw new AccountException("올바른 비밀번호 정보가 아닙니다.", -200);
		}
		// 입금금액이 양수인지 파악
		if(!AMSRegExpr.isDeposit(mainPanel.depositTF.getText())){
			throw new AccountException("올바른 입금금액 정보가 아닙니다.", -200);
		}

		
		//일반 대출계좌라면 입력받은 텍스트를 Account data형식에 맞추어 manager에 등록
		if(accountType.equals(GENERAL_ACCOUNT)){
			accountNum = mainPanel.accountNumTF.getText();
			accountOwner = mainPanel.accountNameTF.getText();
			passwd = Integer.parseInt(mainPanel.accountPasswdTF.getText());
			restMoney = Integer.parseInt((mainPanel.depositTF.getText()));
			manager.add(new Account(accountNum, accountOwner, passwd, restMoney));
		} else  {
			// 대출금액이 양수인지 파악
			if(!AMSRegExpr.isMinusNumber(mainPanel.loanMoneyTF.getText())){
				throw new AccountException("올바른 대출금액 정보가 아닙니다.", -200);
			}
			//마이너스계좌라면 대출금액까지 포함시켜 manager에 등록
			accountNum = mainPanel.accountNumTF.getText();
			accountOwner = mainPanel.accountNameTF.getText();
			passwd = Integer.parseInt(mainPanel.accountPasswdTF.getText());
			restMoney = Integer.parseInt((mainPanel.depositTF.getText()));
			borrowMoney = Integer.parseInt(mainPanel.loanMoneyTF.getText());
			manager.add(new MinusAccount(accountNum, accountOwner, passwd, restMoney, borrowMoney));
		}
	}
	
	//setter getter
	public void setManager(AccountManager manager){
		this.manager = manager;
	}
	public AccountManager getManager(){
		return this.manager;
	}
}
