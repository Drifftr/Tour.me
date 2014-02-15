package com.example.mapdemo;

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

import com.example.comtools.JsonController;
import com.example.sqlitetools.DbHandler;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText userNameTx;
	EditText passwordTx;
	Button signinbtn;
	TextView register;
	String userName,password;
	
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		signinbtn=(Button) findViewById(R.id.signinbtn);
		userNameTx=(EditText) findViewById(R.id.txtusername);
		passwordTx=(EditText) findViewById(R.id.txtpassword);
		register =(TextView) findViewById(R.id.textView2);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
				startActivity(i);
				
			}
		});
		signinbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				new Read().execute();
				
				
			}
		});
		
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	public class Read extends AsyncTask<String, Integer, String>{
		String res;
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(LoginActivity.this);
	        progressDialog.setCancelable(false);
	        progressDialog.setMessage("Retrieving information...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
		@Override
		protected void onPostExecute(String result) {
			DbHandler db = new DbHandler(getApplicationContext());
			if(result.contains("false")){
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(),"Please check the combination",Toast.LENGTH_SHORT).show();
								
			}else if(result.contains(":") ){
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_SHORT).show();
				 SharedPreferences sp=getSharedPreferences("Login", 0);
			     SharedPreferences.Editor Ed=sp.edit();
			     String [] split=result.split(":");
			     db.addData(2,userName+":"+password+":"+split[0]);
			     Ed.putString("userid",split[0]);   
			     Ed.putString("username",split[1]);   
			     Ed.commit();
				Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
				startActivity(i);
				
			}else{
				progressDialog.dismiss();
				String kos=db.getData(2);
				String [] split=kos.split(":");
				if(userName.equals(split[0]) && password.equals(split[1])){
					Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_SHORT).show();
					SharedPreferences sp=getSharedPreferences("Login", 0);
				     SharedPreferences.Editor Ed=sp.edit();
				     Ed.putString("userid",split[2]);   
				     Ed.putString("username",split[0]);   
				     Ed.commit();
					Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
					startActivity(i);
				}else{
					Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
					
				}
				
			
			}
		}
		

		@Override
		protected String doInBackground(String... params) {
			try {
				userName=userNameTx.getText().toString();
				password=passwordTx.getText().toString();
				res=sendJson(userName,password);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return res;
			
		}
		
		
		
		public String sendJson(String username, String password) {
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																					// Limit
			HttpResponse response = null;
			JSONObject json = new JSONObject();

			//HttpPost post = new HttpPost("http://10.0.2.2:8080/JsonTest/LoginController");
			HttpPost post = new HttpPost("http://192.168.131.1:8080/JsonTest/LoginController");
			
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
