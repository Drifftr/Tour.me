package com.example.mapdemo;

import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adaptors.ShowWeatherAdapter;
import com.example.comtools.JSONParser;
import com.example.imagerender.ImageLoader;
import com.example.jsonentities.POIDetails;
import com.example.jsonentities.WeatherEntity;
import com.example.sqlitetools.DbHandler;
import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherResults extends Activity {
	ListView listView;
	TextView city;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_results);
		String latitude=getIntent().getExtras().getString("latitude");
		String longitude=getIntent().getExtras().getString("longitude");
		listView=(ListView)findViewById(R.id.listview);
		city=(TextView)findViewById(R.id.city);
		new Read().execute(latitude,longitude);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_weather_results, menu);
		return true;
	}
	
	public class Read extends AsyncTask<String, Integer, ArrayList<WeatherEntity>>{
		String res="";
		ImageLoader img=new ImageLoader();
		ArrayList<WeatherEntity> weatherlist=new ArrayList<WeatherEntity>();
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(WeatherResults.this);
	        progressDialog.setCancelable(true);
	        progressDialog.setMessage("Retrieving information...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ArrayList<WeatherEntity> weatherResults) {
			city.setText(weatherResults.get(0).getCity());
			ShowWeatherAdapter sw=new ShowWeatherAdapter(getApplicationContext(), 0, weatherResults);	
			listView.setAdapter(sw);
			progressDialog.dismiss();
			
			//- See more at: http://www.survivingwithandroid.com/2013/05/build-weather-app-json-http-android.html#sthash.Et6bI7JY.dpuf
			//DbHandler db = new DbHandler(getApplicationContext());
			/*for(int j=0;j<2;j++){
			if(result.length()<=8 || result.trim().equals("")){
				Toast.makeText(getApplicationContext(),"No Connection",Toast.LENGTH_SHORT).show();
				result=db.getData(1);
				
			}else{
				db.addData(1, result);
				Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
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
			
			}*/
				
			
			
			
		}
		

		@Override
		protected ArrayList<WeatherEntity> doInBackground(String... params) {
			try {
				JSONParser jp=new JSONParser();
				String result=jp.getJSONFromUrl(makeUrl(params[0],params[1]));
				JSONObject jObj;
				jObj = new JSONObject(result);
				JSONArray jArra = jObj.getJSONArray("list");
				String city=jObj.getJSONObject("city").getString("name");
				
				for(int i =0;i<jArra.length();i++){
				JSONObject JSONWeatherup = jArra.getJSONObject(i);
				JSONArray jArr = JSONWeatherup.getJSONArray("weather");
				JSONObject temps = JSONWeatherup.getJSONObject("temp");
				String thedate=JSONWeatherup.getString("dt");
				Date date=new Date((long)Integer.parseInt(thedate)*1000);
				SimpleDateFormat smd=new SimpleDateFormat("EEEE ,yyyy-MM-dd ");
				String newDate=smd.format(date);
				WeatherEntity weather=new WeatherEntity();
				JSONObject JSONWeather = jArr.getJSONObject(0);
				weather.setCity(city);
				weather.setMain(JSONWeather.getString("description"));
				//weather.setMain(JSONWeather.getString("main"));
				weather.setDescription(newDate);
			 
				//JSONObject mainObj = JSONWeatherup.getJSONObject("main");
				weather.setHumidity(JSONWeatherup .getString("humidity"));
			
					
				weather.setTemp_max((Double.parseDouble(temps.getString("max"))-273)+"");
				weather.setTemp_min((Double.parseDouble(temps.getString("min"))-273)+"");
				weather.setTemp((Double.parseDouble(temps.getString("day"))-273)+"");
				
				Bitmap bmp=downloadBitmap("http://openweathermap.org/img/w/"+JSONWeather.getString("icon"));
				weather.setIcon(bmp);
				weatherlist.add(weather);
				}
				return weatherlist;
			} catch (Exception e) {
				 e.printStackTrace();
				 return null;
			}
			
		}
		
		
		
		  public String makeUrl(String latitude,String longitude){
			    StringBuilder urlString = new StringBuilder();
			    urlString.append("http://api.openweathermap.org/data/2.5/forecast/daily?cnt=5");
			    urlString.append("&lat="); 
			    urlString.append((latitude));
			    urlString.append("&lon=");
			    urlString.append((longitude));
			    // String my="http://maps.googleapis.com/maps/api/directions/json?origin=galle&destination=colombo&sensor=false";
			    return urlString.toString();
			  
			}
		
		  private Bitmap downloadBitmap(String url) {
			 Bitmap image = null;
			     final DefaultHttpClient client = new DefaultHttpClient();

			     final HttpGet getRequest = new HttpGet(url);
			     try {

			         HttpResponse response = client.execute(getRequest);

			        
			         final int statusCode = response.getStatusLine().getStatusCode();

			         if (statusCode != HttpStatus.SC_OK) {
			             Log.w("ImageDownloader", "Error " + statusCode + 
			                     " while retrieving bitmap from " + url);
			             return null;

			         }

			         final HttpEntity entity = response.getEntity();
			         if (entity != null) {
			             InputStream inputStream = null;
			             try {
			                 // getting contents from the stream 
			                 inputStream = entity.getContent();

			                 // decoding stream data back into image Bitmap that android understands
			                 image = BitmapFactory.decodeStream(inputStream);


			             } finally {
			                 if (inputStream != null) {
			                     inputStream.close();
			                 }
			                 entity.consumeContent();
			             }
			         }
			     } catch (Exception e) {
			         // You Could provide a more explicit error message for IOException
			         getRequest.abort();
			         Log.e("ImageDownloader", "Something went wrong while" +
			                 " retrieving bitmap from " + url + e.toString());
			     } 

			     return image;
			 }
		
		
		
		
	}

}
