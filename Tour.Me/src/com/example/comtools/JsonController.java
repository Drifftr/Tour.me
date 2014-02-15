package com.example.comtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonController {
	/*private static String LoginURL = "http://10.0.2.2:8080/JsonTest/LoginController";
	private static String RegistrationURL = "http://10.0.2.2:8080/JsonTest/IndexController";
	private static String TempURL = "http://10.0.2.2:8080/JsonTest/temp";
	private static String NearMeURL = "http://10.0.2.2:8080/JsonTest/NearMeController";*/
	private static String LoginURL = "http://192.168.131.1:8080/JsonTest/LoginController";
	private static String RegistrationURL = "http://192.168.131.1:8080/JsonTest/IndexController";
	private static String TempURL = "http://192.168.131.1:8080/JsonTest/temp";
	private static String NearMeURL = "http://192.168.131.1:8080/JsonTest/NearMeController";
	String res;

	@SuppressWarnings("finally")
	public String sendJson(String username, String password, String event) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																				// Limit
		HttpResponse response = null;
		JSONObject json = new JSONObject();

		HttpPost post;
		if (event.contains("login")) {
			post = new HttpPost(LoginURL);
		} else if (event.contains("register")) {
			post = new HttpPost(RegistrationURL);

		} else {
			post = new HttpPost(TempURL);

		}
		try {
			json.put("userName", username);
			json.put("passWord", password);
			StringEntity se = new StringEntity(json.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (response != null) {
			try {
				InputStream in1 = response.getEntity().getContent();
				res = convertStreamToString(in1);

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				return res;
			}

		} else {
			return " ";

		}

	}

	
	
	@SuppressWarnings("finally")
	public String getNearMe(double latitude, double longitude,String category) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																				// Limit
		HttpResponse response = null;		
		JSONObject json = new JSONObject();

		HttpPost post= new HttpPost(NearMeURL);
		
		try {
			json.put("latitude", latitude+"");
			json.put("longitude", longitude+"");
			json.put("category", category);
			StringEntity se = new StringEntity(json.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (response != null) {
			try {
				InputStream in1 = response.getEntity().getContent();
				res = convertStreamToString(in1);

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				return res;
			}

		} else {
			return " ";

		}

	}
	
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
