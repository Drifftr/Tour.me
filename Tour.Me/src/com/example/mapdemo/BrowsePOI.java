package com.example.mapdemo;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.adaptors.ShowPOIAdapter;
import com.example.comtools.JSONParser;
import com.example.jsonentities.POIDetails;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BrowsePOI extends Activity {

	ArrayList<POIDetails> poiList;
	ListView listview;
	JSONParser jp;
	EditText inputSearch;
	ShowPOIAdapter adapter;
	private ProgressDialog progressDialog;
	HashMap<String,String> hm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_poi);
		listview = (ListView) findViewById(R.id.listview);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		hm=new HashMap<String, String>();
		poiList = new ArrayList<POIDetails>();
		
		new Read().execute();
		inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                 BrowsePOI.this.adapter.getFilter().filter(cs); 
             
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
               
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
               
            }
        });
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name=((TextView)view.findViewById(R.id.firstline)).getText()+"";
				
				Intent intent = new Intent(getApplicationContext(),
						PublicPOI.class);
				intent.putExtra("poiId", hm.get(name));
				startActivity(intent);
			}

		});
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_browse_poi, menu);
		return true;
	}


	public class Read extends AsyncTask<String, Integer, String> {
		String res = "";
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(BrowsePOI.this);
	        progressDialog.setCancelable(true);
	        progressDialog.setMessage("Connecting to networks...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
		@Override
		protected void onPostExecute(String result) {
			
			if (result.equals("[]")) {
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(),
						"Cannot Retrieve the locations", Toast.LENGTH_SHORT)
						.show();
			} else {
				JSONArray timeline = new JSONArray();
				try {
					timeline = new JSONArray(result);

					for (int i = 0; i < timeline.length(); i++) {
						JSONObject last = timeline.getJSONObject(i);

						POIDetails pois = new POIDetails();
						pois.setPid(last.getString("pid"));
						pois.setLatitude(last.getDouble("latitude") + "");
						pois.setLongitude(last.getDouble("Longitude") + "");
						pois.setName(last.getString("name"));
						pois.setRating(last.getString("rating"));
						pois.setDescription(last.getString("description") + "");
						poiList.add(pois);
						hm.put(pois.getName(), pois.getPid());
					}
					
					adapter= new ShowPOIAdapter(getApplicationContext(), 0, poiList);
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
				//res = jp.getPOSTJSONFromUrl("http://10.0.2.2:8080/JsonTest/BrowsePOI");
				res = jp.getPOSTJSONFromUrl("http://192.168.131.1:8080/JsonTest/BrowsePOI");
				return res;
			} catch (Exception e) {
				return "error";
			}
 
		}

	}

}



