package vn.giki.rest.utils.pourchase;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.common.io.Files;

import vn.giki.rest.utils.Constant;

public class PurchaseVerifierGoogle {

	

	/** Global configuration of Google Cloud Storage OAuth 2.0 scope. */
	private static final String STORAGE_SCOPE = "https://www.googleapis.com/auth/androidpublisher";

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static String getToken() throws GeneralSecurityException, IOException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		// Check for valid setup.
		String p12Content = Files.readFirstLine(new File("key.p12"), Charset.defaultCharset());

		Preconditions.checkArgument(!p12Content.startsWith("Please"), p12Content);

		// [START snippet]
		// Build a service account credential.
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY).setServiceAccountId(Constant.PURCHASE_GOOGLE.SERVICE_ACCOUNT)
				.setServiceAccountScopes(Collections.singleton(STORAGE_SCOPE))
				.setServiceAccountPrivateKeyFromP12File(new File("key.p12")).build();

		credential.refreshToken();

		return credential.getAccessToken();
	}

	public static String getData(String packageName, String productId, String token) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet receiptInformationRequest;

		try {
			receiptInformationRequest = new HttpGet("https://www.googleapis.com/androidpublisher/v2/applications/"
					+ packageName + "/purchases/subscriptions/" + productId + "/tokens/" + token + "?access_token="
					+ getToken());

			HttpResponse response = httpClient.execute(receiptInformationRequest);
			// int responseCode = response.getStatusLine().getStatusCode();
			String responseBody = EntityUtils.toString(response.getEntity());
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return "";
	}

	public static void main(String[] args) {
		
		//test
		String packageName = "vn.magik.englishpartner";
		String productId = "vip_monthly_discount";
		String token = "ilkoocfjamkeidnmgaoaecai.AO-J1Oz7Q9IuJF4FFaMs0tcoIv-RWau4w56Y51kFHpowujKETh8hjCX_m2jgREN36QlSfZG5JeX5fR9ocvXyF-16TadRXBs860ph247ErJI_65yFNZBU30Mw5pTkMn3XsjMOaEkQlDRS";

		String body = getData(packageName, productId, token);
		System.out.println(body);
	}

}
