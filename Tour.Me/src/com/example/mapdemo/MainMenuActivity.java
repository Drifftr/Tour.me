package com.example.mapdemo;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {

	Button nearMeBtn;
	Button myJourney;
	Button browsePlace;
	Button navigation;
	Button weather;
	Button transport;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		nearMeBtn  = (Button)findViewById(R.id.near);
		myJourney  =  (Button)findViewById(R.id.my_j);
		browsePlace  =  (Button)findViewById(R.id.bus_pls);
		navigation  =  (Button)findViewById(R.id.bus_poi);	
		weather = (Button)findViewById(R.id.weather);
		transport =(Button)findViewById(R.id.transport);
		nearMeBtn.getBackground().setAlpha(155);
		myJourney.getBackground().setAlpha(155);
		browsePlace .getBackground().setAlpha(155);
		navigation.getBackground().setAlpha(155);
		weather.getBackground().setAlpha(155);
		transport.getBackground().setAlpha(155);
		nearMeBtn.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent i1 = new Intent(getApplicationContext(), NearMeActivity.class);
				startActivity(i1);
				
			}
		});
		
		
		myJourney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i2 = new Intent(getApplicationContext(), JourneyList.class);
				startActivity(i2);
				
			}
		});
		
		browsePlace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i2 = new Intent(getApplicationContext(), BrowsePOI.class);
				startActivity(i2);
				
			}
		});
		
		weather.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i2 = new Intent(getApplicationContext(), WeatherMap.class);
				startActivity(i2);
				
			}
		});
		
		transport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i2 = new Intent(getApplicationContext(), TrainSchedule.class);
				startActivity(i2);
				
			}
		});
		
		
		navigation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i2 = new Intent(getApplicationContext(), Navigate.class);
				startActivity(i2);
				
			}
		});
	}

	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

}
