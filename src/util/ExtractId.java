package util;

public class ExtractId {
	public static String extractId(String userInfo) {
		String id = userInfo.substring(userInfo.indexOf("(")+1, userInfo.indexOf(")"));
		return id;
	}
}
