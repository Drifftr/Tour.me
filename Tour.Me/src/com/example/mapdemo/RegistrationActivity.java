package com.example.mapdemo;

import com.example.comtools.JsonController;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	EditText userNameTx;
	EditText passwordTx;
	EditText conpasswordTx;
	Button reginbtn;
	String userName,password,conpassword;
	JsonController jc=new JsonController();
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		reginbtn=(Button) findViewById(R.id.registerbtn);
		userNameTx=(EditText) findViewById(R.id.txtusername);
		passwordTx=(EditText) findViewById(R.id.txtpassword);
		conpasswordTx=(EditText) findViewById(R.id.txtconpassword);
		reginbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(userNameTx.getText().length()<=1 || passwordTx.getText().length()<=1 || conpasswordTx.getText().length()<=1){
					Toast.makeText(getApplicationContext(),"Registration Failed,Please fill all the fields",Toast.LENGTH_SHORT).show();
				}else{
					if(passwordTx.getText().toString().equals(conpasswordTx.getText().toString())){
				new Read().execute();
				}else{
					Toast.makeText(getApplicationContext(),"password and confirm password doesnt match",Toast.LENGTH_SHORT).show();
					
				}
				}
				
			}
		});
		
		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
		 
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
				
		
		
		
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_registration, menu);
		return true;
	}
	public class Read extends AsyncTask<String, Integer, String>{
		String res="";
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(RegistrationActivity.this);
	        progressDialog.setCancelable(false);
	        progressDialog.setMessage("Registering on server...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
		
		
		@Override
		protected void onPostExecute(String result) {
			if(result.contains(":")){
				//Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
				SharedPreferences sp=getSharedPreferences("Login", 0);
			     SharedPreferences.Editor Ed=sp.edit();
			     String [] split=result.split(":");
			     Ed.putString("userid",split[0]);   
			     Ed.putString("username",split[1]);   
			     Ed.commit();
				progressDialog.dismiss();
				Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
				finish();
				startActivity(i);
				
			}else if(result.contains("false")){
				Toast.makeText(getApplicationContext(),"Registration Failed,Please check the values",Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
				
			}else{
				Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
				
			}
			
			
		}
		

		@Override
		protected String doInBackground(String... params) {
			try {
				userName=userNameTx.getText().toString();
				password=passwordTx.getText().toString();
				conpassword=conpasswordTx.getText().toString();
				if(password.equals(conpassword)){
				res=jc.sendJson(userName,password,"register");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return res;
			
		}
		
		
		
		
		
		
		
	}
}
