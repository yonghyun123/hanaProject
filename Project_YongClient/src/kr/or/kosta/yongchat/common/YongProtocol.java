package kr.or.kosta.yongchat.common;

public interface YongProtocol {
	
	public static final String DELEMETER = "뛟뛠";

	public static final int CREATE = 0; // 방 생성시
	public static final int ROOMIN = 50; // 방 입장 시

	public static final int CONNECT = 1000; // 로그인 시
	public static final int CONNECT_RESULT = 1001; // 로그인 반응
	public static final int MULTI_CHAT = 2000; // 대화할 때
	public static final int SECRET_CHAT = 2001; // 귓속말 할 때
	public static final int DISCONNECT = 3000; // 종료할 때
	public static final int UPDATELIST = 4000; // 대기방 리스트 업데이트
	public static final int ROOMLIST = 5000; // 방 리스트
	public static final int ROOMNUMBER = 6000; //
	public static final int INVITE = 7000; // 초대할 때
	public static final int INVITE_REJECT = 8000; // 초대를 거절할 때
	public static final int BACK = 9000; // 다시 대기방으로 돌아갈 때

	public static final int UPDATE_INLIST = 10000; // 대화방에서 참여자리스트 업데이트할 때

	public static final int OUT = 11000; // 방장퇴장시 대화방 폭파
	public static final int SELECT_ROOM = 12000; // 방클릭시 방 사용자들 보내기
	public static final int SEARCH_ROOM = 13000; // 방 검색 시 방 정보(전체) 보내기

}
