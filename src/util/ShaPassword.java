package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaPassword {

	//비밀번호 암호화
	public static String shaPassword(String pw) {
		
		StringBuffer sb=new StringBuffer();
		
		try {
			MessageDigest md=MessageDigest.getInstance("SHA-256");
			byte[] hash=md.digest(pw.getBytes("UTF-8")); 
			
			
			for (int i = 0; i < hash.length; i++) {
				String hex=Integer.toHexString(0xff&hash[i]);//16진수 문자열로 변환
				if(hex.length()==1)sb.append("0");
				sb.append(hex);
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("알고리즘이 존재하지 않습니다.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		return sb.toString() ;
	}
}
