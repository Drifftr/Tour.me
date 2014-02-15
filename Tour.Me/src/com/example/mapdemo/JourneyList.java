package com.example.mapdemo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adaptors.ShowJourneyAdapter;
import com.example.adaptors.ShowPOIAdapter;
import com.example.comtools.JSONParser;
import com.example.jsonentities.Journey;
import com.example.jsonentities.POIDetails;
import com.example.mapdemo.BrowsePOI.Read;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class JourneyList extends Activity {
	ArrayList<POIDetails> poiList;
	ListView listview;
	JSONParser jp;
	String userid="1";
	EditText inputSearch;
	ShowJourneyAdapter adapter;
	Button createJourney;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journey_list);
		listview = (ListView) findViewById(R.id.listview);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		createJourney=(Button)findViewById(R.id.createJourney);
		SharedPreferences sp1=this.getSharedPreferences("Login",0);
	    userid = sp1.getString("userid", null);
		new Read().execute(userid);
		 
		 createJourney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent(getApplicationContext(),
						CreateJourney.class);
				startActivity(intent);
			}
		});
		inputSearch.addTextChangedListener(new TextWatcher() {
	    
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
               JourneyList.this.adapter.getFilter().filter(cs); 
             
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub   
            }
        });
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Journey item = (Journey) parent
						.getItemAtPosition(position);
				Intent intent = new Intent(getApplicationContext(),
						JourneyMap.class);
				intent.putExtra("journeyid", item.getJourneyid());
				startActivity(intent);

			}

		});
		
		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

		@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id3) {
				final Journey item = (Journey) parent
						.getItemAtPosition(position);
			
				new Delete().execute(item.getJourneyid());
				
				return false;
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_journey_list, menu);
		return true;
	}
	
	public class Read extends AsyncTask<String, Integer, String> {
		String res = "";
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog = new ProgressDialog(JourneyList.this);
		        progressDialog.setCancelable(true);
		        progressDialog.setMessage("Retrieving information...");
		        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		        progressDialog.setProgress(0);
		        progressDialog.show();
		    }
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("[]")) {
				Toast.makeText(getApplicationContext(),
						"Cannot Retrieve the information", Toast.LENGTH_SHORT)
						.show();
				progressDialog.dismiss();
			} else {
				ArrayList<Journey> journeyList=new ArrayList<Journey>();
				JSONArray timeline = new JSONArray();
				try {
					timeline = new JSONArray(result);

					for (int i = 0; i < timeline.length(); i++) {
						JSONObject last = timeline.getJSONObject(i);

						Journey pois = new Journey();
						pois.setJourneyid(last.getString("journeyid"));
						pois.setName(last.getString("name") + "");
						pois.setDate(last.getString("date") + "");
						pois.setTime(last.getString("time"));
						pois.setUserid(last.getString("userid"));
						pois.setTravelstate(last.getString("travelstate"));
						journeyList.add(pois);

					}
					
					adapter= new ShowJourneyAdapter(getApplicationContext(), 0, journeyList);
					listview.setAdapter(adapter);
					progressDialog.dismiss();
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
			}

		}

		@Override
		protected String doInBackground(String... params) {
			try {
				jp = new JSONParser();
				JSONObject json=new JSONObject();
				json.put("userid", params[0]);
				//res = jp.sendJSONToUrl("http://10.0.2.2:8080/JsonTest/GetJourneyList",json.toString());
				res = jp.sendJSONToUrl("http://192.168.131.1:8080/JsonTest/GetJourneyList",json.toString());
				return res;
			} catch (Exception e) {
				return "error";
			}

		}

	}
	
	public class Delete extends AsyncTask<String, Integer, String> {
		String res = "";
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        progressDialog = new ProgressDialog(JourneyList.this);
		        progressDialog.setCancelable(true);
		        progressDialog.setMessage("Retrieving information...");
		        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		        progressDialog.setProgress(0);
		        progressDialog.show();
		    }
		@Override
		protected void onPostExecute(String result) {
			finish();
			Intent i2 = new Intent(getApplicationContext(), JourneyList.class);
			startActivity(i2);

		}

		@Override
		protected String doInBackground(String... params) {
			try {
				jp = new JSONParser();
				JSONObject json=new JSONObject();
				json.put("journeyid", params[0]);
				//res = jp.sendJSONToUrl("http://10.0.2.2:8080/JsonTest/RemoveJourney",json.toString());
				res = jp.sendJSONToUrl("http://192.168.131.1:8080/JsonTest/RemoveJourney",json.toString());
				return res;
			} catch (Exception e) {
				return "error";
			}

		}

	}

}