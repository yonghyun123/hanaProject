package kr.or.kosta.entity;
/**
 * Account를 확장한 Minus계좌
 * @author 김용현
 *
 */
public class MinusAccount extends Account {
	//대출금
	private long borrowMoney;
	
	public MinusAccount() {
		this(null,null,0,0,0);
	}

	public MinusAccount(String accountNum, String accountOwner, int passwd, int restMoney, long borrowMoney) {
		super(accountNum, accountOwner, passwd, restMoney);
		this.borrowMoney = borrowMoney;
	}
	
	//methods
	@Override
	public long getRestMoney(){
		return super.getRestMoney() - this.getBorrowMoney();
	}
	
	@Override
	public String toString() {
		return super.toString() + getBorrowMoney() + "\t";
	}

	
	//getter and setter
	public long getBorrowMoney() {
		return borrowMoney;
	}
	
	public void setBorrowMoney(long borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	
	public static void main(String[] args) {
		MinusAccount minusAccount = new MinusAccount();
		System.out.println(minusAccount.getBorrowMoney());
		
		MinusAccount minusAccount2 = new MinusAccount("9999-1111-2222", "이대출", 1111, 0, 1000000);
//		minusAccount2.deposit(10000);
		System.out.println(minusAccount2);
		
		System.out.println(minusAccount2.getRestMoney());// 대출금액을 고려한 출력을 해야해
	}
	
	
}
