package com.example.mapdemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.comtools.JSONParser;
import com.example.jsonentities.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EventView extends FragmentActivity {
	EditText date,time,description;
	TextView name;
	private GoogleMap mMap;	
	Button navigate,edit,viewpoi;
	Event evnt;
	String eventid;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		name=(TextView)findViewById(R.id.textView1);
		date=(EditText)findViewById(R.id.date);
		time=(EditText)findViewById(R.id.time);
		navigate=(Button) findViewById(R.id.navigate);
		edit=(Button) findViewById(R.id.edit);
		description=(EditText)findViewById(R.id.description);
		viewpoi=(Button) findViewById(R.id.viewpoi);
		eventid=getIntent().getStringExtra("eventId");
		new Read().execute(eventid);
		
		navigate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),Navigate.class);
				intent.putExtra("DestLat",evnt.getLatitude());
				intent.putExtra("DestLng",evnt.getLongitude());
    			startActivity(intent);
				
			}
		});
		edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!date.getText().toString().trim().isEmpty() || !time.getText().toString().trim().isEmpty() || !description.getText().toString().trim().isEmpty()){
				new Update().execute(eventid,date.getText().toString(),time.getText().toString(),description.getText().toString());
				}else{
					Toast.makeText(getApplicationContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		viewpoi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),PublicPOI.class);
				intent.putExtra("poiId",evnt.getRefpoi());
				startActivity(intent);
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_event_view, menu);
		return true;
	}
	
	private void setUpMap(Event ev) {
	  	Marker marker= mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ev.getLatitude()),Double.parseDouble(ev.getLongitude()))).title(ev.getName()).snippet(ev.getDate()+" | "+ev.getTime()));
	  	LatLng loc=new LatLng(Double.parseDouble(ev.getLatitude()),Double.parseDouble(ev.getLongitude()));
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc,10);
		mMap.animateCamera(cameraUpdate);
}
	 public class Read extends AsyncTask<String, Integer, String>{
		 String jsonString;
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog = new ProgressDialog(EventView.this);
		        progressDialog.setCancelable(false);
		        progressDialog.setMessage("Retrieving information...");
		        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		        progressDialog.setProgress(0);
		        progressDialog.show();
		    }
		 @Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			JSONArray timeline = null;
			try {
				timeline = new JSONArray(result);
				JSONObject jsb=timeline.getJSONObject(0);
				evnt=new Event();
				evnt.setEventid(jsb.getString("eventid"));
				evnt.setLatitude(jsb.getString("latitude"));
				evnt.setLongitude(jsb.getString("longitude"));
				evnt.setName(jsb.getString("name"));
				evnt.setDate(jsb.getString("date"));
				evnt.setTime(jsb.getString("time"));
				evnt.setDescription(jsb.getString("description"));
				evnt.setRefpoi(jsb.getString("refpoi"));
				name.setText(evnt.getName().toString());
				date.setText(evnt.getDate());
				time.setText(evnt.getTime());
				description.setText(evnt.getDescription());
				if(evnt.getRefpoi().equals("0")){
					viewpoi.setVisibility(View.GONE);
					
				}
				setUpMap(evnt);
				progressDialog.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		 
		 
		@Override
		protected String doInBackground(String... arg0) {
			JSONObject json=new JSONObject();
			JSONParser jp=new JSONParser();
			try {
				json.put("eventid", arg0[0]);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//jsonString=jp.sendJSONToUrl("http://10.0.2.2:8080/JsonTest/GetEvent",json.toString());
			jsonString=jp.sendJSONToUrl("http://192.168.131.1:8080/JsonTest/GetEvent",json.toString());
			return jsonString;
		}
	 
	 }
	 
	 
	 public class Update extends AsyncTask<String, Integer, String>{
		 String jsonString;
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog = new ProgressDialog(EventView.this);
		        progressDialog.setCancelable(true);
		        progressDialog.setMessage("Updating information...");
		        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		        progressDialog.setProgress(0);
		        progressDialog.show();
		    }
		 
		 @Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
			progressDialog.dismiss();
		}
		 
		 
		@Override
		protected String doInBackground(String... arg0) {
			JSONObject json=new JSONObject();
			JSONParser jp=new JSONParser();
			try {
				json.put("eventid", arg0[0]);
				json.put("date", arg0[1]);
				json.put("time", arg0[2]);
				json.put("description", arg0[3]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//jsonString=jp.sendJSONToUrl("http://10.0.2.2:8080/JsonTest/UpdateEvent",json.toString());
			jsonString=jp.sendJSONToUrl("http://192.168.131.1:8080/JsonTest/UpdateEvent",json.toString());
			return jsonString;
		}
	 
	 }
}
