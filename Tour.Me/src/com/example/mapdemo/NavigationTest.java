package com.example.mapdemo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.comtools.GPSTracker;
import com.example.comtools.JSONParser;
import com.example.mapdemo.LoginActivity.Read;
import com.example.maptools.DirectionGrabber;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NavigationTest extends FragmentActivity {
	DirectionGrabber dg;
	MapFragment mMapFragment;
	private GoogleMap mMap;	
	GPSTracker gpsTracker;
	private double latitude;
	private double longitude;
	Button savePath;
	ArrayList<LatLng> ar=new ArrayList<LatLng>(); 
	List<LatLng> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation_test);
		savePath=(Button) findViewById(R.id.savepath);
		String url=makeUrl(new LatLng(6.9344,79.8428),new LatLng(6.0350,80.2158));
		new Read().execute(url);
		setUpMapIfNeeded();
		mMap.setMyLocationEnabled(true);
		savePath.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new Save().execute();
			}
		});
		
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				//Toast.makeText(getApplicationContext(),arg0.latitude+"|"+arg0.longitude, Toast.LENGTH_SHORT).show();
				String url=makeUrl(getCordinates(),arg0);
				mMap.clear();
				new Read().execute(url);
			}
		});
		
	}
	
	//Map Configurations
	private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }
  private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(getCordinates()).title("Marker"));
    	
    }
  
  
  //Direction Tools
  public String makeUrl(LatLng source,LatLng dest){
	    StringBuilder urlString = new StringBuilder();
	    urlString.append("http://maps.googleapis.com/maps/api/directions/json");
	    urlString.append("?origin="); 
	    urlString.append(Double.toString(source.latitude));
	    urlString.append(",");
	    urlString.append(Double.toString(source.longitude));
	    urlString.append("&destination=");
	    urlString.append(Double.toString(dest.latitude));
	    urlString.append(",");
	    urlString.append(Double.toString(dest.longitude));
	    urlString.append("&sensor=false&mode=driving");
	   // String my="http://maps.googleapis.com/maps/api/directions/json?origin=galle&destination=colombo&sensor=false";
	    return urlString.toString();
	  
	}
  
    public void drawPath(String result){
    	
        try{
        	
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            list = decodePoly(encodedString);
            LatLng last = null;
            for (int i = 0; i < list.size()-1; i++) {
                LatLng src = list.get(i);
                LatLng dest = list.get(i+1);
                last = dest;
               Polyline line = mMap.addPolyline(new PolylineOptions().add( 
                        new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                        .width(2)
                        .color(Color.BLUE));
            }
            
        }catch (JSONException e){
        	e.printStackTrace();
        	
        }
		
    }
    
    public List<LatLng> decodePoly(String encoded){

	    List<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0;
	    int length = encoded.length();
	    int latitude = 0;
	    int longitude = 0;

	    while(index < length){
	        int b;
	        int shift = 0;
	        int result = 0;

	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);

	        int destLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        latitude += destLat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);

	        int destLong = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        longitude += destLong;

	        poly.add(new LatLng((latitude / 1E5),(longitude / 1E5) ));
	    }
	    return poly;
	}
    
   
  //GPS Tools
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_navigation_test, menu);
		return true;
	}
	
	
	public class Read extends AsyncTask<String, Integer, String>{
		String jsonString;
		
		@Override
		protected void onPostExecute(String jstring) {
				drawPath(jstring);
			}
		
		@Override
		protected String doInBackground(String... arg0) {
			JSONParser jp=new JSONParser();
			jsonString=jp.getJSONFromUrl(arg0[0]);
		    return jsonString;
		}
		
		
		
	}
	
	public class Save extends AsyncTask<String, Integer, String>{
		String jsonString;
		
		@Override
		protected void onPostExecute(String jstring) {
				Toast.makeText(getApplicationContext(), jstring, Toast.LENGTH_SHORT);
			}
		
		@Override
		protected String doInBackground(String... arg0) {
			Gson gson=new Gson();
			String geopoints=gson.toJson(list);
			JSONObject json = new JSONObject();
			JSONParser jp=new JSONParser();
			try {
				json.put("userID", "hetti");
				json.put("geoPoints", geopoints);
				json.put("journeyID", "2");                   ///////////////////////////////////////////////////Journey ID
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//jsonString=jp.sendJSONToUrl("http://10.0.2.2:8080/JsonTest/SavePath",json.toString());
			jsonString=jp.sendJSONToUrl("http://192.168.131.1:8080/JsonTest/SavePath",json.toString());
			return jsonString;
		}
		
		
		
	}

}
