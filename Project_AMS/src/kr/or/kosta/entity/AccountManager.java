package kr.or.kosta.entity;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import kr.or.kosta.exception.AccountException;

/**
 * 은행계좌 Manager
 * @author 김용현
 *
 */

public class AccountManager {
	private Hashtable<String,Account> accounts;

//	default Constructor
	public AccountManager(){
		this(50); 
	}
//	Constructor
	public AccountManager(int capacity){
		accounts = new Hashtable<String, Account>(capacity);
	}
	
	//getter and setter
	public Hashtable<String,Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(Hashtable<String, Account> accounts) {
		this.accounts = accounts;
	}
	
	/**
	 * add account element
	 * @param account
	 */
	public void add(Account account) throws AccountException{
		if(accounts.containsKey(account.getAccountNum())) {
			throw new AccountException("이미 등록되어 있는 계좌입니다.",-200);
		}
		accounts.put(account.getAccountNum(), account);
	}
	
	//실제 들어있는 계좌만 반환
	/**
	 * get all accounts
	 * @return Account[]
	 */
	
	public List<Account> list(){
		
		//여기에서 바꿔줘야해
		List<Account> tempList = new ArrayList<Account>();
		Enumeration e = accounts.elements();
		while(e.hasMoreElements()) {
			Account temp = (Account) e.nextElement();
			tempList.add(temp);
		}
		return tempList;
	}
	
	/**
	 * find account data only one;
	 * @param accountNum
	 * @return Account
	 */
	
	public Account get(String accountNum){
		return accounts.get(accountNum);
	}
	
	/**
	 * find account data of same user name
	 * @param accountName
	 * @return List
	 */
	
	public List<Account> search(String accountName){
		List<Account> list = new ArrayList<>();
		Enumeration<Account> e = accounts.elements();
		while(e.hasMoreElements()) {
			Account tmpAcc = e.nextElement();
			if(tmpAcc.getAccountOwner().equals(accountName)) {
				list.add(tmpAcc);
			}
		}
		return list;
	}
	/**
	 * remove parameter account data
	 * @param accountNum
	 * @return boolean
	 */
	public boolean remove(String accountNum){
		return accounts.remove(accountNum) != null;
	}
}	