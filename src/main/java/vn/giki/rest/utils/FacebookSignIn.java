package vn.giki.rest.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookSignIn {

	public static String getFBGraph(String token) {
		String graph = null;
		try {

			String g = "https://graph.facebook.com/me?access_token=" + token
					+ "&fields=name,gender,email,picture{url},first_name";
			URL u = new URL(g);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine;
			StringBuffer b = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				b.append(inputLine + "\n");
			in.close();
			graph = b.toString();
			System.out.println(graph);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR in getting FB graph data. " + e);
		}
		return graph;
	}

	public static HashMap<String, String> getGraphData(String token) {
		HashMap<String, String> fbProfile = new HashMap<>();
		try {
			JSONObject json = new JSONObject(getFBGraph(token));
			fbProfile.put("id", json.getString("id"));
			fbProfile.put("name", json.getString("first_name"));
			if (json.has("email"))
				fbProfile.put("email", json.getString("email"));
			if (json.has("gender"))
				fbProfile.put("gender", json.getString("gender"));
			if (json.has("picture")) {
				JSONObject pic = json.getJSONObject("picture");
				JSONObject data = pic.getJSONObject("data");
				fbProfile.put("url", data.getString("url"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR in parsing FB graph data. " + e);
		}
		return fbProfile;
	}

	public static Map<String, Object> checkToken(String token, String facebookId) throws IllegalArgumentException, UnsupportedEncodingException {

		HashMap<String, String> info = getGraphData(token);
		String id = info.get("id");
		if (id.trim().equals(facebookId.trim())) {
			Map<String, Object> o = new HashMap<String, Object>();
			o.put("email", info.get("email"));
			o.put("facebookId", id);
			o.put("googleId", "");
			o.put("token", token);
			o.put("tokenClient", Utils.encodeJWT(facebookId));
			o.put("created", Utils.getDate());
			o.put("name", info.get("name"));
			o.put("gender", info.get("gender"));
			o.put("avatarUrl", info.get("url"));
			o.put("hint", Constant.USER.HINT_DEFAULT);
			return o;
		}
		return null;

	}
}
