package vn.giki.rest.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Utils {

	public static final String KEY = "$2a$10$fvQirCkgl75zjEqN7XMVyuBTQe";

	public static String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static String getDate(long longTime){
		DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
		Date date = new Date(longTime);
		return dateFormat.format(date);
	}
	
	public static String md5(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte[] digest = md.digest();
			String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
			return myHash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String encodeJWT(String id) throws IllegalArgumentException, UnsupportedEncodingException {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			String token = JWT.create().withIssuer(id)
					.sign(algorithm);
			return token;
	
	}

	public static Integer decodeJWT(String token) {
		DecodedJWT jwt = JWT.decode(token);
		return Integer.parseInt(jwt.getIssuer());

	}
	
	public static String getValue(List<String> listData){

		StringBuffer stringBuffer = new StringBuffer();
		for (String listIdPlatform : listData) {
			stringBuffer.append("'");
			stringBuffer.append(listIdPlatform);
			stringBuffer.append("',");
		}
		String value = stringBuffer.substring(0, stringBuffer.lastIndexOf(","));
		return value;
		
	}
	
	public static void main(String[] args) throws IllegalArgumentException, UnsupportedEncodingException {
		long a = System.currentTimeMillis();
		
		DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
		Date date = new Date(a);
		System.out.println(dateFormat.format(date));
		
		int list = 3;
		int size = 1;
	
		int total = (list%size!=0)?(list/size+1):(list/size);
				
		System.out.println(total);
		
	}
}
