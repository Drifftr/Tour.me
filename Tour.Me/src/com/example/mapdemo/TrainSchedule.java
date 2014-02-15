package com.example.mapdemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adaptors.ShowPOIAdapter;
import com.example.comtools.JSONParser;
import com.example.jsonentities.POIDetails;
import com.example.jsonentities.TrainDetails;
import com.example.mapdemo.CreateJourney.Send;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class TrainSchedule extends Activity {
	Button submit;
	EditText fromTime;
	EditText toTime, date;
	Spinner fromPlace, toPlace;
	ArrayList<TrainDetails> trainList;
	Gson gson;
	HashMap<String, String> hm;
	private ProgressDialog progressDialog;
	int year = 0, month = 0, day = 0, hour = 0, minute = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_schedule);
		submit = (Button) findViewById(R.id.check);
		fromTime = (EditText) findViewById(R.id.fromtime);
		toTime = (EditText) findViewById(R.id.totime);
		date = (EditText) findViewById(R.id.date);
		fromPlace = (Spinner) findViewById(R.id.imat);
		toPlace = (Spinner) findViewById(R.id.goingto);
		trainList = new ArrayList<TrainDetails>();
		hm = new HashMap<String, String>();
		gson = new Gson();
		new FillList().execute();
	//	Toast.makeText(getApplicationContext(),"Aawa",Toast.LENGTH_LONG).show();
	//	final Date todate = new Date();
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		/*SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");
		String today = smd.format(todate);*/
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				if(!fromTime.getText().toString().trim().isEmpty() && !toTime.getText().toString().trim().isEmpty() && !date.getText().toString().trim().isEmpty()){
					Intent intent = new Intent(getApplicationContext(),
							SelectStation.class);
					intent.putExtra("fromtime", fromTime.getText() + ":00");
					intent.putExtra("totime", toTime.getText() + ":00");
					intent.putExtra("fromplace",
							hm.get(fromPlace.getSelectedItem()));
					intent.putExtra("toplace", hm.get(toPlace.getSelectedItem()));
					intent.putExtra("date", date.getText().toString());
					startActivity(intent);
					}else{
						Toast.makeText(getApplicationContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
					}
				

			}
		});

		date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(1111);

			}
		});
		fromTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(1112);

			}
		});
		toTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(1113);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_train_schedule, menu);
		return true;
	}

	public class FillList extends AsyncTask<String, Integer, String> {
		String jsonString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(TrainSchedule.this);
			progressDialog.setCancelable(true);
			progressDialog.setMessage("Retrieving information...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setProgress(0);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(String jstring) {
			// trainList=gson.fromJson(jstring,new
			// TypeToken<ArrayList<TrainDetails>>(){}.getType());
			JSONArray trainString = null;
			ArrayList<String> stationList = new ArrayList<String>();

			try {
				JSONObject lasting = new JSONObject(jstring);
				trainString = lasting.getJSONArray("stations");

				//Toast.makeText(getApplicationContext(), jstring,
				//		Toast.LENGTH_SHORT).show();

				for (int i = 4; i < trainString.length(); i++) {
					JSONObject last = trainString.getJSONObject(i);
					String stationName = last.getString("stationName");
					String stationCode = last.getString("stationCode");
					hm.put(stationName, stationCode);
					stationList.add(stationName);

				}
				final StableArrayAdapter adapter = new StableArrayAdapter(
						getApplicationContext(),
						android.R.layout.simple_list_item_1, stationList);

				fromPlace.setAdapter(adapter);
				toPlace.setAdapter(adapter);
				progressDialog.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			//	Toast.makeText(getApplicationContext(), e.toString(),
				//		Toast.LENGTH_SHORT).show();
			}

			// Toast.makeText(getApplicationContext(),trainList.get(0).getStartStationName(),
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			JSONParser jp = new JSONParser();
			jsonString = jp
					.getJSONFromUrl("http://m.icta.lk/services/railwayservice/getAllStations.php?lang=en");
			return jsonString;

		}

	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1111:
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		case 1112:
			return new TimePickerDialog(this, fromtimePickerListener, hour,
					minute, false);
		case 1113:
			return new TimePickerDialog(this, totimePickerListener, hour,
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
			if(day<10){
				date.setText(year + "-" + (month + 1) + "-0" + day);
			}else{
			date.setText(year + "-" + (month + 1) + "-" + day);
			}
		}
	};

	private TimePickerDialog.OnTimeSetListener fromtimePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			fromTime.setText(hourOfDay + ":" + minute);
		}
	};
	private TimePickerDialog.OnTimeSetListener totimePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			toTime.setText(hourOfDay + ":" + minute);
		}
	};
}
