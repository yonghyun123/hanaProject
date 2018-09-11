package kr.or.kosta.entity;

import kr.or.kosta.exception.AccountException;

/**
* 일상생활의 객체를 추상화하기 위한 모델링 클래스 정의
* 은행계좌 객체
*/

public class Account {

	//static 초기화 블록(특수한 목적의 명령어 실행 ex DB연결 jdbc 등..)
	static {
		System.out.println("초기화 블록 실행입니다..1");
		System.out.println("초기화 블록 실행입니다..2");
	}

	// 클래스(static) 변수
	public static final String bankName = "하나은행";

	// instance 변수 선언
	private String accountNum;
	private String accountOwner;
	private int passwd;
	private int restMoney;

	//Constructor
	public Account(){
		this(null,null);
	}
	public Account(String accountNum, String accountOwner, int passwd, int restMoney){
		this.accountNum = accountNum;
		this.accountOwner = accountOwner;
		this.passwd = passwd;
		this.restMoney = restMoney;
	}

	public Account(String accountNum, String accountOwner){
		this(accountNum,accountOwner,0,0);
	}

	//instance method
	public long deposit(long money) throws AccountException{
		if(money <= 0) {
			throw new AccountException("출금하고자 하는 금액은 음수일 수 없습니다.", -1);
		}
		this.restMoney += money;
		return this.restMoney;
	}

	public long withdraw(long money) throws AccountException{
		if(money <= 0) {
			throw new AccountException("출금하고자 하는 금액은 음수일 수 없습니다.", -1);
		}
		if(money > restMoney){
			throw new AccountException("잔액이 부족합니다", -2);
		}
		this.restMoney -= money;
		return this.restMoney;
	}

	public long getRestMoney(){
		return this.restMoney;
	}

	public boolean checkPasswd(int passwd){
		return this.passwd == passwd ? true : false;
	}
	
	@Override
	public boolean equals(Object account) {
		boolean result = false;
		if(account instanceof Account) {
			return this.toString().equals(account.toString());	
		} else {
			return super.equals(account);
		}
	}

	//class(static) method
	public static int sum(int a, int b){
		return a+b;
	}

	public String toString() {
		String objStr = "";
		if(this instanceof MinusAccount){
			objStr += "마이너스\t";
		} else {
			objStr += "입출금\t";
		}
		return objStr + getAccountNum() + "\t" + getAccountOwner() + "\t"
				+ getRestMoney() + "\t";
	}
	
	//getter setter
	public void setAccountNum(String accountNum){
		this.accountNum = accountNum;
	}
	public String getAccountNum(){
		return this.accountNum;
	}

	public void setAccountOwner(String accountOwner){
		this.accountOwner = accountOwner;
	}
	public String getAccountOwner(){
		return this.accountOwner;
	}

	public void setPasswd(int passwd){
		this.passwd = passwd;
	}
	public int getPasswd(){
		return this.passwd;
	}

	public void setRestMoney(int restMoney){
		this.restMoney = restMoney;
	}

}
