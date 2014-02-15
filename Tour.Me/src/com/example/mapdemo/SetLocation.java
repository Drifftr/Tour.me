package com.example.mapdemo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.comtools.GPSTracker;
import com.example.comtools.JsonController;
import com.example.jsonentities.POIDetails;
import com.example.mapdemo.EventView.Read;
import com.example.sqlitetools.DbHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

import android.support.v4.app.*;

import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;


import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import android.os.AsyncTask;
import android.os.Bundle;

import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class SetLocation extends FragmentActivity {
	private GoogleMap mMap;	
	GPSTracker gpsTracker;
	private double latitude;
	private double longitude;
	JsonController jc=new JsonController();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_location);
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		LatLng loc=new LatLng(7.2964,80.6350);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 7);
		mMap.animateCamera(cameraUpdate);	
		new Read().execute("all");
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
	    
			@Override
			public void onMapLongClick(LatLng arg0) {
				Intent i=getIntent();
				i.putExtra("latitude", arg0.latitude+"");
				i.putExtra("longitude", arg0.longitude+"");
				i.putExtra("refpoi","0");
				setResult(RESULT_OK,i);     
				finish();
			}
		});
		
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				Intent i=getIntent();
				i.putExtra("latitude", arg0.getPosition().latitude+"");
				i.putExtra("longitude", arg0.getPosition().longitude+"");
				i.putExtra("refpoi",arg0.getSnippet());
				setResult(RESULT_OK,i);     
				finish();
				return false;
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_set_location, menu);
		return true;
	}
	
	
    private void setUpMap(POIDetails poid) {
   
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(poid.getLatitude()),Double.parseDouble(poid.getLongitude()))).title(poid.getName()).snippet(poid.getPid()));
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
