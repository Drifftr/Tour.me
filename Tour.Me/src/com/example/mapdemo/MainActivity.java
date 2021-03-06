package com.example.mapdemo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.comtools.GPSTracker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;




import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
MapFragment mMapFragment;
private GoogleMap mMap;	
GPSTracker gpsTracker;
private double latitude;
private double longitude;
ArrayList<LatLng> ar=new ArrayList<LatLng>(); 
final Handler handler = new Handler();
Timer timer = new Timer();
TimerTask doAsynchronousTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		setUpMapIfNeeded();
		mMap.setMyLocationEnabled(true);
		/*mMap.setOnMapClickListener(new OnMapClickListener() {
		
		
			@Override
			public void onMapClick(LatLng arg0) {
			
				
			}
		});*/
		
		
	    
	    doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {       
	                    try {
	                    	LatLng ln=getCordinates();
	        				ar.add(ln);
	        				
	        				for(int i=1;i<ar.size();i++){
	        					if(ar.size()>=2){
	        					Polyline line = mMap.addPolyline(new PolylineOptions()
	        					.add(ar.get(i-1), ar.get(i)).width(5)
	        					.color(Color.RED));
	        					}
	        			}
	                    } catch (Exception e) {
	                        
	                    }
	                }
	            });
	        }
	    };
	    timer.schedule(doAsynchronousTask, 0, 5000);
		
	}

	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not have been
     * completely destroyed during this process (it is likely that it would only be stopped or
     * paused), {@link #onCreate(Bundle)} may not be called again so we should call this method in
     * {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(getCordinates()).title("Marker"));
    	
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
				
			} else {
				
				ln=new LatLng(latitude, longitude);
			}

		} else {
			gpsTracker.showSettingsAlert();
		}
	
	
		
		return ln;
	}
	public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString
            .append(Double.toString( sourcelog));
		urlString.append("&destination=");// to
		urlString
            .append(Double.toString( destlat));
		urlString.append(",");
		urlString.append(Double.toString( destlog));
		urlString.append("&sensor=false&mode=driving&alternatives=true");
		return urlString.toString();
	}
}

