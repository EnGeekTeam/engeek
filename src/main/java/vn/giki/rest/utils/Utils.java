package vn.giki.rest.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

public class Utils {
	public static final String KEY = "$2a$10$fvQirCkgl75zjEqN7XMVyuBTQe";

	public static String hashTokenClient(String id) {
		Date date = new Date();
		String string = date.toString() + KEY + id;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes());
			byte[] digest = md.digest();
			String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
			
			return myHash;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	
}
