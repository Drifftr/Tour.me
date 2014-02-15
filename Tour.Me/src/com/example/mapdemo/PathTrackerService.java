package com.example.mapdemo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.comtools.GPSTracker;
import com.example.comtools.JSONParser;
import com.example.jsonentities.POIDetails;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PathTrackerService extends FragmentActivity {
	MapFragment mMapFragment;
	private GoogleMap mMap;	
	GPSTracker gpsTracker;
	private double latitude;
	private double longitude;
	ArrayList<LatLng> ar=new ArrayList<LatLng>(); 
	final Handler handler = new Handler();
	Timer timer = new Timer();
	Timer timer2 = new Timer();
	TimerTask doAsynchronousTask;
	TimerTask doAsynchronousTask2;
	String string;
	Read read;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path_tracker_service);
		 mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapservice))
                 .getMap();
		//setUpMapIfNeeded();
		mMap.setMyLocationEnabled(true);
		
		Button startBtn=(Button)findViewById(R.id.startbtn);
		startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				read=new Read();
				read.execute();
			}
		});
		Button stopbtn=(Button)findViewById(R.id.stopbtn);
		stopbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				read.cancel(true);
			}
		});
		
	    doAsynchronousTask2 = new TimerTask() {      
	    	ArrayList<LatLng> locar=new ArrayList<LatLng>(); 
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {       
	                    try {
	                    	
	                    	SharedPreferences sp1=getApplicationContext().getSharedPreferences("geopoints",0);
	                	    String jsonString = sp1.getString("points", null);
	                	    Toast.makeText(getApplicationContext(),
	        						jsonString,
	        						Toast.LENGTH_LONG).show();
	                	    JSONArray timeline = null;
	            			
	            				timeline = new JSONArray(jsonString);
	            				for(int i=0;i<timeline.length();i++){
	            					JSONObject last=timeline.getJSONObject(i);
	            					
	            					LatLng obj=new LatLng(Double.parseDouble(last.getString("latitude")),Double.parseDouble(last.getString("longitude")));
	            					locar.add(obj);
	            					
	            				}
	                	    
	            			mMap.clear();	
	            			for(int i=1;i<locar.size();i++){
	        					if(locar.size()>=2){
	        					Polyline line = mMap.addPolyline(new PolylineOptions()
	        					.add(locar.get(i-1), locar.get(i)).width(5)
	        					.color(Color.RED));
	        					}
	        			}
	                    } catch (Exception e) {
	                        
	                    }
	                }
	            });
	        }
	    };
	    timer2.schedule(doAsynchronousTask2, 0, 5000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_path_tracker_service, menu);
		return true;
	}
		
	private void setUpMapIfNeeded() {
        if (mMap == null) {
        	
            if (mMap != null) {
                setUpMap();
            }
        }
    }

 
	
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
    				ln=new LatLng(22.2532, 12.5365);
    				
    			} else {
    				
    				ln=new LatLng(latitude, longitude);
    			}

    		} else {
    			gpsTracker.showSettingsAlert();
    		}
    	
    	
    		
    		return ln;
    	}

	public class Read extends AsyncTask<String, Integer, String>{
		String jsonString;
		Gson gson;
		SharedPreferences sp;
		SharedPreferences.Editor Ed;
		
		@Override
		protected void onPostExecute(String jstring) {
				
			}
		
		@Override
		protected String doInBackground(String... arg0) {
			gson=new Gson();
			sp=getSharedPreferences("geopoints", 0);
		    Ed=sp.edit();
			
		    doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() {       
		                    try {
		                    	LatLng ln=getCordinates();
			        			ar.add(ln);
			        			String geopoints=gson.toJson(ar,ArrayList.class);
			        		    Ed.putString("points",geopoints);   
			        		    Ed.commit();
		        			   } catch (Exception e) {
		                        
		                    }
		                }
		            });
		        }
		    };
		    timer.schedule(doAsynchronousTask, 0, 5000);
		    return jsonString;
		}
		
		
		
	}
}
