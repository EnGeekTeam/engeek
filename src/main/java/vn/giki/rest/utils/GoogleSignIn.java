package vn.giki.rest.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleSignIn {
	
	public static final String CLIENT_ID="666918469015-bkrvoemjm7gngb9fmptsido9aan6e69l.apps.googleusercontent.com";
	public static boolean test(String idTokenString, String googleId) {
		final JacksonFactory jacksonFactory = new JacksonFactory();
		final NetHttpTransport transport = new NetHttpTransport();
		
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
				.setAudience(Collections.singletonList(CLIENT_ID))
				// Or, if multiple clients access the backend:
				// .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2,
				// CLIENT_ID_3))
				.build();
		

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken;
		try {
			idToken = verifier.verify(idTokenString);
			if (idToken != null) {
				Payload payload = idToken.getPayload();
				
				// Print user identifier
				String userId = payload.getSubject();
				System.out.println("GoogleID: " + googleId + " : " + googleId.length());
				System.out.println("User ID: " + userId + " : " + userId.length());
				if (userId.trim().equals(googleId.trim())){
					System.out.println("true");
					return true;
				}
				
				
			} else {
				System.out.println("Invalid ID token.");
			}
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
}
