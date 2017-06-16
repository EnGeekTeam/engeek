
package vn.giki.rest.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleSignIn {


	public static Map<String, Object> checkToken(String idTokenString, String googleId) {
		final JacksonFactory jacksonFactory = new JacksonFactory();
		final NetHttpTransport transport = new NetHttpTransport();

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
//				.setAudience(Collections.singletonList(Constant.PURCHASE_GOOGLE.CLIENT_ID))
				.setAudience(Arrays.asList(Constant.PURCHASE_GOOGLE.CLIENT_ID_ANDROID, Constant.PURCHASE_GOOGLE.CLIENT_ID_IOS))
				.build();

		GoogleIdToken idToken;
		try {
			idToken = verifier.verify(idTokenString);
			if (idToken != null) {
				Payload payload = idToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();
				
				String email = payload.getEmail();
				// boolean emailVerified =
				// Boolean.valueOf(payload.getEmailVerified());
				String name = (String) payload.get("name");
				String pictureUrl = (String) payload.get("picture");
				// String locale = (String) payload.get("locale");
				String gender = (String) payload.get("gender");
				

				System.out.println("GoogleID: " + googleId + " : " + googleId.length());
				System.out.println("User ID: " + userId + " : " + userId.length());
				if (userId.trim().equals(googleId.trim())) {
					System.out.println("true");
					Map<String, Object> o = new HashMap<String, Object>();
					o.put("email", email);
					o.put("facebookId", "");
					o.put("googleId", googleId);
					o.put("token", idTokenString);
					o.put("tokenClient", Utils.encodeJWT(googleId));
					o.put("created", Utils.getDate());
					o.put("name", name);
					o.put("gender", gender);
					o.put("avatarUrl", pictureUrl);
					o.put("hint", Constant.USER.HINT_DEFAULT);
					return o;
				}
			} else {
				System.out.println("Invalid ID token.");
			}
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return new HashMap<String, Object>();

	}
}
