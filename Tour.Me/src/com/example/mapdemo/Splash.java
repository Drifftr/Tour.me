package com.example.mapdemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Splash extends Activity {


	@Override
	protected void onCreate(Bundle TravisLoveKani) {
		// TODO Auto-generated method stub
		super.onCreate(TravisLoveKani);
		setContentView(R.layout.activity_splash);
		
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(1000);
				} catch (InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openMainActivity = new Intent(Splash.this, LoginActivity.class);
					startActivity(openMainActivity);       
				}
			}
		};
		timer.start();
	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
