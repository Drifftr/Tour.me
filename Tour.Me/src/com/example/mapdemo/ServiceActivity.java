package com.example.mapdemo;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.comtools.GPSTracker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.widget.Toast;


public class ServiceActivity extends IntentService {
	public ServiceActivity(String name) {
		super(name);
		
	}


	MapFragment mMapFragment;
	private GoogleMap mMap;	
	GPSTracker gpsTracker;
	private double latitude;
	private double longitude;
	ArrayList<LatLng> ar=new ArrayList<LatLng>(); 
	final Handler handler = new Handler();
	Timer timer = new Timer();
	TimerTask doAsynchronousTask;
	Gson gson;
	
	
	

	@Override
	protected void onHandleIntent(Intent arg0) {
	                    	//LatLng ln=getCordinates();
	        			///	ar.add(ln);
	        		        String jstring="my path";
	        		        publishResults(jstring);

		
	}
private void publishResults(String path) {
		    Intent intent = new Intent("com.example.mapdemo.PathTrackerService");
		    intent.putExtra("path", path);
		    sendBroadcast(intent);
		  }
    
    
public LatLng getCordinates() {
		
	LatLng ln=null;
		
		gpsTracker = new GPSTracker(getApplicationContext());
		gpsTracker.stopUsingGPS();
		if (gpsTracker.canGetLocation()) {
			latitude =gpsTracker.getLatitude();
			longitude =gpsTracker.getLongitude();
			if (latitude == 0.0 || longitude == 0.0) {
				ln=new LatLng(14.2532, 12.5365);
				Toast.makeText(getApplicationContext(),
						"Lat " + latitude + " Long " + longitude,
						Toast.LENGTH_LONG).show();
			} else {
				
				ln=new LatLng(latitude, longitude);
			}

		} else {
			gpsTracker.showSettingsAlert();
		}
	
	
		
		return ln;
	}

}
