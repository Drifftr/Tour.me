package com.example.mapdemo;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddEvent extends Activity {
	EditText name,date,time,description;
	Button setLocation,regevent;
	String latitude="",longitude="",nametxt="",datetxt="",timetxt="",descriptiontxt="",refpoi="0";
	int year = 0, month = 0, day = 0, hour = 0, minute = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		name=(EditText)findViewById(R.id.eventname);
		date=(EditText)findViewById(R.id.eventdate);
		time=(EditText)findViewById(R.id.eventtime);
		description=(EditText)findViewById(R.id.description);
		setLocation =(Button) findViewById(R.id.setlocation);
		regevent =(Button) findViewById(R.id.regevent);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		regevent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nametxt=name.getText().toString();
				datetxt=date.getText().toString();
				timetxt=time.getText().toString();
				descriptiontxt=description.getText().toString();
				if(!nametxt.trim().isEmpty() || !datetxt.trim().isEmpty() || !timetxt.trim().isEmpty() || !latitude.trim().isEmpty()){
				Intent i=getIntent();
				i.putExtra("latitude", latitude);
				i.putExtra("longitude",longitude);
				i.putExtra("eventname", nametxt);
				i.putExtra("eventdate",datetxt);
				i.putExtra("eventtime", timetxt);
				i.putExtra("description",descriptiontxt);
				i.putExtra("refpoi", refpoi);
				setResult(RESULT_OK,i);     
				finish();
				}else{
					Toast.makeText(getApplicationContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		setLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mapintent = new Intent(getApplicationContext(),SetLocation.class);
				 startActivityForResult( mapintent, 2);
				
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
	private void handleLocation(Intent intent) {
		try{
	    Bundle extras = intent.getExtras();
	    latitude =(String) extras.get("latitude");
	    longitude =(String) extras.get("longitude");
	    refpoi=(String) extras.get("refpoi");
	   // Toast.makeText(getApplicationContext(), latitude, Toast.LENGTH_LONG).show();
		}catch(Exception e){}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		handleLocation(data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_event, menu);
		return true;
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
