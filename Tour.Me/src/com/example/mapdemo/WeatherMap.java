package com.example.mapdemo;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class WeatherMap extends FragmentActivity {
	private GoogleMap mMap;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_map);
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		LatLng loc=new LatLng(7.2964,80.6350);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 7);
		mMap.animateCamera(cameraUpdate);	
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
		
			@Override
			public void onMapLongClick(LatLng arg0) {
				Intent i=new Intent(getApplicationContext(),WeatherResults.class);
				i.putExtra("latitude", arg0.latitude+"");
				i.putExtra("longitude", arg0.longitude+"");
				startActivity(i);    
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_weather_map, menu);
		return true;
	}

}
