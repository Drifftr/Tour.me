package com.example.mapdemo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.comtools.GPSTracker;
import com.example.comtools.JSONParser;
import com.example.comtools.JsonController;
import com.example.jsonentities.POIDetails;
import com.example.mapdemo.SetLocation.Read;
import com.example.sqlitetools.DbHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

public class Dateselect extends FragmentActivity {
	MapFragment mMapFragment;
	private GoogleMap mMap;	
	GPSTracker gpsTracker;
	private double latitude;
	private double longitude;
	Button savePath;
	ArrayList<LatLng> ar=new ArrayList<LatLng>(); 
	List<LatLng> list;
	private ProgressDialog progressDialog;
	JsonController jc=new JsonController();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dateselect);
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		LatLng start=getCordinates();
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(start,10);
		mMap.animateCamera(cameraUpdate);
		new Read().execute("all");
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				//Toast.makeText(getApplicationContext(),arg0.latitude+"|"+arg0.longitude, Toast.LENGTH_SHORT).show();
				String url=makeUrl(getCordinates(),arg0);
				mMap.clear();
				new Path().execute(url);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_dateselect, menu);
		return true;
	}
	 private void setUpMap(POIDetails poid) {
		   
	        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(poid.getLatitude()),Double.parseDouble(poid.getLongitude()))).title(poid.getName()).snippet(poid.getPid()));
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

		
		public class Path extends AsyncTask<String, Integer, String>{
			String jsonString;
			@Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog = new ProgressDialog(Dateselect.this);
		        progressDialog.setCancelable(true);
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
				JSONParser jp=new JSONParser();
				jsonString=jp.getJSONFromUrl(arg0[0]);
			    return jsonString;
			}
			
			
			
		}
}
