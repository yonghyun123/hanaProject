package kr.or.kosta.exception;

import javax.swing.JOptionPane;

public class AccountException extends Exception {
	//String message;
	private int errorCode;
	
	//생성자
	public AccountException() {
		this("계좌처리중 예기치 않은 에러가 발생하였습니다.", -9);
	}

	public AccountException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
		JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.ERROR_MESSAGE);
	}
	
	//getter setter
	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		return "AccountException [errorCode=" + errorCode + ", getErrorCode()=" + getErrorCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
