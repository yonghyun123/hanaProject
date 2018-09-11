package kr.or.kosta.util;

import java.util.regex.Pattern;

public class AMSRegExpr {
	//예제에서 한 내용을 토대로 총 12자리 계좌번호 등록
	public static boolean isAccountNum(String str){
		String pattern = "\\d{4}-\\d{4}-\\d{4}";
		return str.matches(pattern) && !str.equals("");	
	}
	
	//한글만 사용할 수 있는 정규식
	public static boolean isAccountName(String str){
		String pattern = "^[ㄱ-ㅎ가-힣]*$";
		return str.matches(pattern) && !str.equals("");
	}
	
	//비밀번호 양식은 언제든지 바뀔수 있기때문에 정규식을 분리해서 사용
	public static boolean isAccountPasswd(String str){
		String pattern =  "^[0-9]*$";
		return str.matches(pattern) && !str.equals("");
	}
	
	// 금액입력시 양수만을 입력받게 함
	// 입출금계좌시 대출금액이 비활성화 되어있으므로 0으로 초기화
	public static boolean isDeposit(String str){
		String pattern =  "^[0-9]*$";
		return str.matches(pattern) && !str.equals("");
	}
	
	public static boolean isMinusNumber(String str){
		String pattern =  "^[0-9]*$";
		return str.matches(pattern) && !str.equals("");
	}
	
	public static void main(String[] args) {
		System.out.println(isMinusNumber(""));
	}
}
