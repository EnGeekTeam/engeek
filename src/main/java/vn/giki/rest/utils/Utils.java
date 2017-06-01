package vn.giki.rest.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Utils {

	public static final String KEY = "$2a$10$fvQirCkgl75zjEqN7XMVyuBTQe";


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
	
	public static void main(String[] args) throws IllegalArgumentException, UnsupportedEncodingException {
		System.out.println(encodeJWT("1"));
		System.out.println(decodeJWT("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIxMTIifQ.zyoxU7cA1Bq-HK6d7fhvqa0ZuqKu0w1IaFpGQTGTwJo"));
	}
}
