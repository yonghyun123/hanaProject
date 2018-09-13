
public class YongRoom {

	String leaderName, roomName, maxRoomCnt, roomNumber;

	public YongRoom() {

	}

	public YongRoom(String leaderName, String roomName, String maxRoomCnt, String roomNumber) {
		super();
		this.leaderName = leaderName;
		this.roomName = roomName;
		this.maxRoomCnt = maxRoomCnt;
		this.roomNumber = roomNumber;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getMaxRoomCnt() {
		return maxRoomCnt;
	}

	public void setMaxRoomCnt(String maxRoomCnt) {
		this.maxRoomCnt = maxRoomCnt;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public String toString() {
		return "뛟뛠" + String.format("%s%15s%15s%15s", roomNumber, leaderName, roomName, maxRoomCnt);
	}

}