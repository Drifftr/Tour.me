package com.example.mapdemo;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.comtools.JSONParser;
import com.example.imagerender.ImageLoader;
import com.example.jsonentities.Event;
import com.example.jsonentities.Journey;
import com.example.jsonentities.POIDetails;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateJourney extends Activity {
	Button setstart,setEnd,addEvent,createjourney;
	EditText name,date,time;
	String eventlatitude="0",startlatitude="0",endlatitude="0",refpoi="0";
	String eventlongitude="0",startlongitude="0",endlongitude="0";
	String nametxt="",datetxt="",timetxt="";
	int year = 0, month = 0, day = 0, hour = 0, minute = 0;
	String userid="1";
	ArrayList<Event> events=new ArrayList<Event>();
	LinearLayout journeyDisplay;
	JSONParser jp;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_journey);
		setstart=(Button)findViewById(R.id.setstart);
		setEnd=(Button)findViewById(R.id.setend);
		addEvent=(Button)findViewById(R.id.addevent);
		createjourney=(Button)findViewById(R.id.createjourney);
		journeyDisplay=(LinearLayout)findViewById(R.id.journeydisplay);
		name=(EditText)findViewById(R.id.journeyname);
		date=(EditText)findViewById(R.id.startdate);
		time=(EditText)findViewById(R.id.starttime);
		SharedPreferences sp1=this.getSharedPreferences("Login",0);
	    userid = sp1.getString("userid", null);
	    final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		createjourney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nametxt=name.getText().toString();
				datetxt=date.getText().toString();
				timetxt=time.getText().toString();
				if(!nametxt.trim().isEmpty() || !datetxt.trim().isEmpty() || !timetxt.trim().isEmpty() || !startlatitude.trim().equals("0") || !endlatitude.trim().equals("0")){
				new Send().execute();
				}else{
					Toast.makeText(getApplicationContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
				}
			}
		});
		setstart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mapintent = new Intent(getApplicationContext(),SetLocation.class);
				 startActivityForResult( mapintent, 1);
				
			}
		});
		setEnd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mapintent = new Intent(getApplicationContext(),SetLocation.class);
				 startActivityForResult( mapintent, 2);
				
			}
		});
		
		addEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent eventintent = new Intent(getApplicationContext(),AddEvent.class);
				 startActivityForResult( eventintent, 3);
				
			}
		});
		date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(1111);

			}
		});
		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(1112);

			}
		});
		
	}
	
	private void handleStartLocation(Intent intent) {
	    
	    try{
	    Bundle extras = intent.getExtras();
	    startlatitude =(String) extras.get("latitude");
	    startlongitude =(String) extras.get("longitude");
	    refpoi=(String) extras.get("refpoi");
	    Toast.makeText(getApplicationContext(), startlatitude, Toast.LENGTH_LONG).show();
}catch(Exception ex){
	    	
	    }
	}
	
	private void handleEndLocation(Intent intent) {
	    
	    try{
	    Bundle extras = intent.getExtras();
	    endlatitude =(String) extras.get("latitude");
	    endlongitude =(String) extras.get("longitude");
	    refpoi=(String) extras.get("refpoi");
	    Toast.makeText(getApplicationContext(), endlatitude, Toast.LENGTH_LONG).show();
	    }catch(Exception ex){
	    	
	    }
	}
	
	
	private void handleEvent(Intent intent) {
	    
	    try{
	    Bundle extras = intent.getExtras();
	    String eventname="",eventdate="",eventtime="",eventdescription="",eventRefPoi="";
	    eventlatitude =(String) extras.get("latitude");
	    eventlongitude =(String) extras.get("longitude");
	    eventname=(String) extras.get("eventname");
	    eventdate=(String) extras.get("eventdate");
	    eventtime=(String) extras.get("eventtime");
	    eventdescription=(String) extras.get("description");
	    eventRefPoi=(String) extras.get("refpoi");
	    Event singleEvent=new Event();
	    singleEvent.setName(eventname);
	    singleEvent.setDate(eventdate);
	    singleEvent.setTime(eventtime);
	    singleEvent.setDescription(eventdescription);
	    singleEvent.setLatitude(eventlatitude);
	    singleEvent.setLongitude(eventlongitude);
	    singleEvent.setRefpoi(eventRefPoi);
	    events.add(singleEvent);
	    TextView tv = new TextView(getApplicationContext());
		tv.setText(singleEvent.getName()+" due in "+singleEvent.getDate());
		journeyDisplay.addView(tv);
	    }catch(Exception ex){
	    	
	    }
		}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1){
			handleStartLocation(data);
		}else if(requestCode==2){
			handleEndLocation(data);
		}else if(requestCode==3){
			handleEvent(data);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.activity_create_journey, menu);
		return true;
	}
	
	
	public class Send extends AsyncTask<String, Integer, String>{
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(CreateJourney.this);
	        progressDialog.setCancelable(false);
	        progressDialog.setMessage("Sending information...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
		
		@Override
		protected void onPostExecute(String result) {
			
		if(result.contains("Success")){
			progressDialog.dismiss();
			Toast.makeText(getApplicationContext(), "Added Successfully",Toast.LENGTH_LONG).show();
			finish();
		}else{
			progressDialog.dismiss();
			Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_LONG).show();
			
		}
		}
		

		@Override
		protected String doInBackground(String... params) {
			try {
				jp=new JSONParser();
				Gson gson=new Gson();
				Journey journ=new Journey();
				journ.setDate(datetxt);
				journ.setTime(timetxt);
				journ.setStartlat(startlatitude);
				journ.setStartlng(startlongitude);
				journ.setEndlat(endlatitude);
				journ.setEndlng(endlongitude);
				journ.setName(nametxt);
				journ.setUserid(userid);
				journ.setEvents(events);
				String tres=gson.toJson(journ,Journey.class);
				//String ress=jp.sendJSONToUrl("http://10.0.2.2:8080/JsonTest/CreateJourney", tres);
				String ress=jp.sendJSONToUrl("http://192.168.131.1:8080/JsonTest/CreateJourney", tres);
				return ress;
			} catch (Exception e) {
				return e.getMessage();
			}
			
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1111:
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		case 1112:
			return new TimePickerDialog(this, timePickerListener, hour,
					minute, false);
		
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			date.setText(year + "-" + (month + 1) + "-" + day);

		}
	};

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			time.setText(hourOfDay + ":" + minute);
		}
	};
	
}
