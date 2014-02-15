package com.example.mapdemo;  //  Navigate code

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.comtools.GPSTracker;
import com.example.comtools.JsonController;
import com.example.jsonentities.POIDetails;
import com.example.sqlitetools.DbHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NearMeActivity extends FragmentActivity {
MapFragment mMapFragment;
private GoogleMap mMap;	
GPSTracker gpsTracker;
private double latitude;
private double longitude;
ArrayList<LatLng> ar=new ArrayList<LatLng>(); 
final Handler handler = new Handler();
Timer timer = new Timer();
TimerTask doAsynchronousTask;
String category;
JsonController jc=new JsonController();
Gson gson=new Gson();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_me);
		
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
		mMap.setMyLocationEnabled(true);
		LatLng loc=new LatLng(7.2964,80.6350);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 7);
		mMap.animateCamera(cameraUpdate);
		category="all";
		Spinner spinner=(Spinner) findViewById(R.id.spinner);
		String [] values={"all","Hotel","Attraction","Shopping","Transport"};
		ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, values);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		new Read().execute(category);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mMap.clear();
				TextView cat=(TextView) arg1;
				category=cat.getText().toString();
				
				new Read().execute(category);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				Intent intent2=new Intent(getApplicationContext(), CreatePOI.class);
				intent2.putExtra("latitude", arg0.latitude);
				intent2.putExtra("longitude", arg0.longitude);
				finish();
				startActivity(intent2);
			}
		});
		
		
		
		
		
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				Intent intent = new Intent(getApplicationContext(),PublicPOI.class);
				intent.putExtra("poiId",marker.getSnippet());
    			startActivity(intent);
				return false;
			}
		});
		
		
		
		
		
		/*mMap.setOnMapClickListener(new OnMapClickListener() {
		
		
			@Override
			public void onMapClick(LatLng arg0) {
			
				
			}
		});*/
		
		
	    
	    /*doAsynchronousTask = new TimerTask() {       
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
	    timer.schedule(doAsynchronousTask, 0, 5000);*/
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    /*private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
               // setUpMap(new POIDetails());
            }
        }
    }*/

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(POIDetails poid) {
    /*	HashMap<String, String> hm=new HashMap<String, String>();
    	hm.put("", "");*/
    	///Check Hashmap Implementation
    	String path="drawable/"+poid.getCategory().toLowerCase(Locale.ENGLISH);
    	int it=getResources().getIdentifier(path, "drawable-hdpi", getPackageName());
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(poid.getLatitude()),Double.parseDouble(poid.getLongitude()))).title(poid.getName()).snippet(poid.getPid()).icon(BitmapDescriptorFactory.fromResource(it)));
       
    }
    
    
public LatLng getCordinates() {
		
		
		LatLng ln=null;
		
		gpsTracker = new GPSTracker(getApplicationContext());
		gpsTracker.stopUsingGPS();
		if (gpsTracker.canGetLocation()) {
			latitude =gpsTracker.getLatitude();
			longitude =gpsTracker.getLongitude();
			if (latitude == 0.0 || longitude == 0.0) {
				ln=new LatLng(14.2532, 12.5365);                    ///grave problem
			} else {
				
				ln=new LatLng(latitude, longitude);
			}

		} else {
			
		}
	
	
		
		return ln;
	}
public class Read extends AsyncTask<String, Integer, String>{
	String res="";
	
   
	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(String result) {
		//progressDialog.cancel();
		DbHandler db = new DbHandler(getApplicationContext());
		
		for(int j=0;j<2;j++){
		if(result.length()<=8 || result.trim().equals("")){
			Toast.makeText(getApplicationContext(),"No Connection",Toast.LENGTH_SHORT).show();
			result=db.getData(1);
			
		}else{
			db.addData(1, result);
		//	Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
			JSONArray timeline = null;
			try {
				timeline = new JSONArray(result);
				
				for(int i=0;i<timeline.length();i++){
				JSONObject last=timeline.getJSONObject(i);
				
				POIDetails pois=new POIDetails();
				pois.setPid(last.getString("pid"));
				pois.setLatitude(last.getDouble("latitude")+"");
				pois.setLongitude(last.getDouble("Longitude")+"");
				pois.setName(last.getString("name"));
				pois.setCategory(last.getString("category"));
				pois.setRating(last.getString("rating"));
				pois.setDescription(last.getString("description")+"");
				setUpMap(pois);
				
			}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		}
		
			
		
		
		
	}
	

	@Override
	protected String doInBackground(String... params) {
		try {
			LatLng loc=getCordinates();
			res= jc.getNearMe(loc.latitude, loc.longitude,params[0]);
			return res;
		} catch (Exception e) {
			return e.getMessage();
		}
		
	}
	
	
	
	
	
	
	
}
	
}

