package com.example.mapdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.comtools.JSONParser;
import com.example.imagerender.ImageLoader;
import com.example.jsonentities.Comment;
import com.example.jsonentities.Event;
import com.example.jsonentities.Journey;
import com.example.jsonentities.POIDetails;
import com.example.mapdemo.Navigate.Read;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class JourneyMap extends FragmentActivity {
	private GoogleMap mMap;	
	List<LatLng> list;
	JSONParser jp=new JSONParser();
	HashMap<String, String> hm=new HashMap<String, String>();
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journey_map);
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		LatLng loc=new LatLng(7.2964,80.6350);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 7);
		mMap.animateCamera(cameraUpdate);
		
		new Read().execute(getIntent().getExtras().getString("journeyid"));
		
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				if(arg0.getTitle().contains("Start") || arg0.getTitle().contains("End")){
				}else{
				Intent i1 = new Intent(getApplicationContext(), EventView.class);
				i1.putExtra("eventId", hm.get(arg0.getId()));
				startActivity(i1);
				
				}
				return false;
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_journey_map, menu);
		return true;
	}
	
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
	  private void setUpMap(Event ev) {
		  	  Marker marker= mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ev.getLatitude()),Double.parseDouble(ev.getLongitude()))).title(ev.getName()).snippet(ev.getDate()+" | "+ev.getTime()));
		      hm.put(marker.getId(),ev.getEventid());
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
	    
	    public class Read extends AsyncTask<String, Integer, String>{
			String jsonString;

@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(JourneyMap.this);
	        progressDialog.setCancelable(true);
	        progressDialog.setMessage("Retrieving information...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
			@Override
			protected void onPostExecute(String result) {
				if(result.equals("[]")){
					Toast.makeText(getApplicationContext(),"Cannot Retrieve the Journey",Toast.LENGTH_SHORT).show();
				}else{
					//Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
					try{
					
						JSONObject last=new JSONObject(result);
						
						Journey journey=new Journey();
						journey.setJourneyid(last.getString("journeyid"));
						journey.setName(last.getString("name"));
						journey.setDate(last.getString("date"));
						journey.setTime(last.getString("time"));
						journey.setStartlat(last.getString("startlat"));
						journey.setStartlng(last.getString("startlng"));
						journey.setEndlat(last.getString("endlat"));
						journey.setEndlng(last.getString("endlng"));
						journey.setTravelstate(last.getString("travelstate"));
						
						ArrayList<Event> eventList =new ArrayList<Event>();
						JSONArray eventLine=new JSONArray(last.getString("events"));
						for(int i=0;i<eventLine.length();i++){
							JSONObject jsb=eventLine.getJSONObject(i);
							Event evnt=new Event();
							evnt.setEventid(jsb.getString("eventid"));
							evnt.setLatitude(jsb.getString("latitude"));
							evnt.setLongitude(jsb.getString("longitude"));
							evnt.setName(jsb.getString("name"));
							evnt.setDate(jsb.getString("date"));
							evnt.setTime(jsb.getString("time"));
							setUpMap(evnt);
				            eventList.add(evnt);
						}
						journey.setEvents(eventList);
						//Toast.makeText(getApplicationContext(),journey.getStartlat()+","+journey.getStartlng()+":"+journey.getEndlat()+","+journey.getEndlng(),Toast.LENGTH_SHORT).show();
						
						String url=makeUrl(new LatLng(Double.parseDouble(journey.getStartlat()),Double.parseDouble(journey.getStartlng())),new LatLng(Double.parseDouble(journey.getEndlat()),Double.parseDouble(journey.getEndlng())));
						Marker marker1= mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(journey.getStartlat()),Double.parseDouble(journey.getStartlng()))).title("Start Location").snippet(journey.getDate()+" | "+journey.getTime()));
						Marker marker2= mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(journey.getEndlat()),Double.parseDouble(journey.getEndlng()))).title("End Location"));
						progressDialog.dismiss();
						new Path().execute(url);
						
						}catch(JSONException e){
						e.printStackTrace();
					}
					}
				}
			
			@Override
			protected String doInBackground(String... arg0) {
				
				JSONObject json=new JSONObject();
				try {
					json.put("journeyid", arg0[0]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//jsonString=jp.sendJSONToUrl("http://10.0.2.2:8080/JsonTest/GetJourney",json.toString());
				jsonString=jp.sendJSONToUrl("http://192.168.131.1:8080/JsonTest/GetJourney",json.toString());
				return jsonString;
			}
			
			
			
		}
	    
	    public class Path extends AsyncTask<String, Integer, String>{
			String jsonString="";
			@Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog = new ProgressDialog(JourneyMap.this);
		        progressDialog.setCancelable(false);
		        progressDialog.setMessage("Resolving Path...");
		        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		        progressDialog.setProgress(0);
		        progressDialog.show();
		    }
			@Override
			protected void onPostExecute(String jstring) {
					drawPath(jstring);
					progressDialog.dismiss();
				}
			
			@Override
			protected String doInBackground(String... arg0) {
				jsonString=jp.getJSONFromUrl(arg0[0]);
			    return jsonString;
			}
			
			
			
		}
}
