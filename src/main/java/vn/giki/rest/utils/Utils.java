package vn.giki.rest.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

public class Utils {

	public static final String KEY = "$2a$10$fvQirCkgl75zjEqN7XMVyuBTQe";

	public static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
		Date date = new Date();
		return dateFormat.format(date).replaceAll("/", "-");
	}
	
	public static String getLastSevenDay(){
		DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		Date date = calendar.getTime();
		String s = dateFormat.format(date).replaceAll("/", "-");
		return s;
	}

	public static String getDateTime(long longTime) {
		DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
		Date date = new Date(longTime);
		return dateFormat.format(date);
	} 
	
	public static String getDate(long longTime) {
		DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
		Date date = new Date(longTime);
		return dateFormat.format(date).replaceAll("/", "-");
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

	public static String createToken() {
		Date date = new Date();
		String text = (KEY + date.toString());
		return md5(text);

	}

	public static String getValue(List<String> listData) {

		StringBuffer stringBuffer = new StringBuffer();
		for (String listIdPlatform : listData) {
			stringBuffer.append("'");
			stringBuffer.append(listIdPlatform);
			stringBuffer.append("',");
		}
		String value = stringBuffer.substring(0, stringBuffer.lastIndexOf(","));
		return value;

	}
	public static String genCode( ){
        int len = 6;
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
	public static void main(String[] args) throws IllegalArgumentException, UnsupportedEncodingException, MalformedURLException, URISyntaxException {

		System.out.println(getDate(1495472116000l));
		
		
		
		System.out.println(getLastSevenDay());

	}
}
