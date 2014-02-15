package com.example.mapdemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adaptors.ShowJourneyAdapter;
import com.example.adaptors.ShowStationAdapter;
import com.example.comtools.JSONParser;
import com.example.jsonentities.TrainDetails;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectStation extends Activity {
	ListView listView;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_station);
		listView=(ListView)findViewById(R.id.listview);
		String url=makeUrl(getIntent().getExtras().getString("fromtime"),getIntent().getExtras().getString("totime"),getIntent().getExtras().getString("fromplace"),getIntent().getExtras().getString("toplace"),getIntent().getExtras().getString("date"));
		//Toast.makeText(getApplicationContext(),url, Toast.LENGTH_SHORT).show();
		new Read().execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_station, menu);
		return true;
	}
	public String makeUrl(String fromTime,String toTime,String fromPlace,String toPlace,String adate){
	    StringBuilder urlString = new StringBuilder();
	    urlString.append("http://m.icta.lk/services/railwayservice/getSchedule.php");
	    urlString.append("?lang=en&startStationCode="); 
	    urlString.append(fromPlace);
	    urlString.append("&endStationCode=");
	    urlString.append(toPlace);
	    urlString.append("&arrivalTime=");
	    urlString.append(fromTime);
	    urlString.append("&depatureTime=");
	    urlString.append(toTime);
	    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    Date fDate=new Date();
	    String currentDate= adate;
	    String currentTime= time.format(fDate);
	    urlString.append("&currentDate="+currentDate+"&currentTime="+currentTime+"");
	    /*String my="http://m.icta.lk/services/railwayservice/getSchedule.php?lang=en&startStationCode=FOT&endStationCode=KDT&arrivalTime=01:00:00&depatureTime=22:00:01&currentDate=2010-12-23&currentTime=06:53:00";
	    return my;*/
	   return urlString.toString();
	  
	}
	public class Read extends AsyncTask<String, Integer, String>{
		String jsonString;
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(SelectStation.this);
	        progressDialog.setCancelable(true);
	        progressDialog.setMessage("Retrieving information...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
		@Override
		protected void onPostExecute(String jstring) {
				//trainList=gson.fromJson(jstring,new TypeToken<ArrayList<TrainDetails>>(){}.getType());
				JSONArray trainString = null;
			//	Toast.makeText(getApplicationContext(),jsonString, Toast.LENGTH_SHORT).show();
		
					try {
						JSONObject lasting=new JSONObject(jstring);
						trainString=lasting.getJSONArray("trains");
						
						//Toast.makeText(getApplicationContext(),"Fucked", Toast.LENGTH_SHORT).show();
				
			ArrayList<TrainDetails> trainList=new ArrayList<TrainDetails>();
				for(int i=0;i<trainString.length();i++){
					JSONObject last=trainString.getJSONObject(i);
					
					TrainDetails td=new TrainDetails();
					td.setName(last.getString("name"));
					td.setArrivalTime(last.getString("arrivalTime"));
					td.setDepatureTime(last.getString("depatureTime"));
					td.setArrivalAtDestinationTime(last.getString("arrivalAtDestinationTime"));
					td.setDelayTime(last.getString("delayTime"));
					td.setComment(last.getString("comment"));
					td.setStartStationName(last.getString("startStationName"));
					td.setEndStationName(last.getString("endStationName"));
					td.setToTrStationName(last.getString("toTrStationName"));
					td.setfDescription(last.getString("fDescription"));
					td.setTyDescription(last.getString("tyDescription"));
					
					
					trainList.add(td);
					
				}
				
				ShowStationAdapter adapter= new ShowStationAdapter(getApplicationContext(), 0, trainList);
				listView.setAdapter(adapter);
				
				progressDialog.dismiss();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
					}
				
				
			//	Toast.makeText(getApplicationContext(),trainList.get(1).getStartStationName(), Toast.LENGTH_SHORT).show();
			}
		
		@Override
		protected String doInBackground(String... arg0) {
			JSONParser jp=new JSONParser();
			jsonString=jp.getJSONFromUrl(arg0[0]);
		    return jsonString;
						
		}
		
		
		
	}
	
}
