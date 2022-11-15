package vo;

public class VO {
	final private static String ADMIN_ID = "admin";
	private static String myID;
	
	static public boolean isAdmin() {
		return ADMIN_ID.equals(myID);
	}
	
	static public void setMyID(String id) {
		myID = id;
	}
	
	static public String getMyID() {
		return myID;
	}
}
