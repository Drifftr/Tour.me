package com.example.mapdemo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.comtools.JSONParser;
import com.example.imagerender.ImageLoader;
import com.example.jsonentities.Comment;
import com.example.jsonentities.POIDetails;
import com.google.android.gms.plus.model.people.Person.Image;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PublicPOI extends Activity {
	TextView heading,description,votes,address,category;
	RatingBar rating;
	Image image;
	Gson gson;
	JSONParser jp;
	String pid,userid;
	Button navigate,addComment;
	POIDetails pois;
	ImageView imageview;
	EditText newComment;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_poi);
		SharedPreferences sp1=this.getSharedPreferences("Login",0);
	    userid = sp1.getString("userid", null);
		heading=(TextView) findViewById(R.id.heading);
		newComment=(EditText)findViewById(R.id.newComment);
		addComment=(Button)findViewById(R.id.addComment);
		description=(TextView) findViewById(R.id.description);
		votes=(TextView) findViewById(R.id.votes);
		address=(TextView) findViewById(R.id.address);
		category=(TextView) findViewById(R.id.category);
		rating= (RatingBar) findViewById(R.id.ratingBar);
		navigate=(Button)findViewById(R.id.navigate);
		imageview=(ImageView)findViewById(R.id.image);
		
		
		//image= (Image) findViewById(R.id.image);
		
		//Toast.makeText(getApplicationContext(),getIntent().getStringExtra("poiId"),Toast.LENGTH_LONG).show();
		pid=getIntent().getStringExtra("poiId");
		new Read().execute(pid);
		rating.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				 if (arg1.getAction()== MotionEvent.ACTION_UP)
				 {
				RatingBar rat=(RatingBar) arg0;
				new Rate().execute(pid,rat.getRating()+"");
				 }
				
				return false;
			}
		});
		
		navigate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),Navigate.class);
				intent.putExtra("DestLat",pois.getLatitude());
				intent.putExtra("DestLng",pois.getLongitude());
    			startActivity(intent);
				
			}
		});
		
		addComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String str=newComment.getText().toString();
				new CommentPOI().execute(pid,userid,str);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_public_poi, menu);
		return true;
	}
	
	
	public class Read extends AsyncTask<String, Integer, String>{
		String res="";
		JSONObject json;
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(PublicPOI.this);
	        progressDialog.setCancelable(true);
	        progressDialog.setMessage("Retrieving information...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("[]")){
				Toast.makeText(getApplicationContext(),"Cannot Retrieve the information",Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
			}else{
				//Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
				JSONArray timeline = null;
				try {
					timeline = new JSONArray(result);
					JSONObject last=timeline.getJSONObject(0);
					
					pois=new POIDetails();
					pois.setPid(last.getString("pid"));
					pois.setLatitude(last.getDouble("latitude")+"");
					pois.setLongitude(last.getDouble("Longitude")+"");
					pois.setName(last.getString("name"));
					pois.setRating(last.getString("rating"));
					pois.setDescription(last.getString("description")+"");
					pois.setVotes(last.getString("votes"));
					pois.setAddress(last.getString("address"));
					pois.setCategory(last.getString("category"));
					pois.setPicture(last.getString("picture"));
					ArrayList<Comment> cmntList =new ArrayList<Comment>();
					JSONArray cmntLine=new JSONArray(last.getString("comments"));
					for(int i=0;i<cmntLine.length();i++){
						JSONObject jsb=cmntLine.getJSONObject(i);
						Comment cmnt=new Comment();
						cmnt.setCommentID(jsb.getString("commentID"));
			            cmnt.setPoiID(jsb.getString("poiID"));
			            cmnt.setUserID(jsb.getString("userID"));
			            cmnt.setDate(jsb.getString("date"));
			            cmnt.setTime(jsb.getString("time"));
			            cmnt.setComment(jsb.getString("comment"));
			            cmntList.add(cmnt);
					}
					pois.setComments(cmntList);
					
					LinearLayout ll=(LinearLayout)findViewById(R.id.comments);
					ll.setOrientation(LinearLayout.VERTICAL);
					
					for(Comment cm:pois.getComments()){
						TextView tv = new TextView(getApplicationContext());
						tv.setTextColor(Color.GRAY);
						tv.setText(cm.getUserID()+" Says "+cm.getComment());
						ll.addView(tv);
						
						
						
					}
					ImageLoader img=new ImageLoader();
					heading.setText(pois.getName());
					description.setText(pois.getDescription());
					rating.setRating((float) Integer.parseInt(pois.getRating()));
					votes.setText("(by "+pois.getVotes()+" votes)");
					address.setText(pois.getAddress());
					category.setText(pois.getCategory());
					if(pois.getPicture().length()>=100){
						Bitmap bm=img.StringToBitMap(pois.getPicture());
						imageview.setImageBitmap(bm);
						
					}
					progressDialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			}
		
	
		@Override
		protected String doInBackground(String... params) {
			try {
				 String jsons="{\"pid\":\""+params[0]+"\"}";
				 json=new JSONObject();
				 json.put("pid", params[0]);
				 String res="NUll";	
				HttpClient client = new DefaultHttpClient();
	    		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
	    																		// Limit
	    		HttpResponse response = null;
	    		//HttpPost post= new HttpPost("http://10.0.2.2:8080/JsonTest/GetPOI");
	    		HttpPost post= new HttpPost("http://192.168.131.1:8080/JsonTest/GetPOI");
	    		StringEntity se = new StringEntity(json.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				if (response != null) {
						InputStream in1 = response.getEntity().getContent();
						res = convertStreamToString(in1);
								
				} 
				return res;
			} catch (Exception e) {
				return "error";
			}
			
			
			
		}
		
	}
		public class Rate extends AsyncTask<String, Integer, String>{
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				
					Intent intent=new Intent(getApplicationContext(),PublicPOI.class);
					intent.putExtra("poiId", pid);
					startActivity(intent);
					finish();
				
			}
			
			@Override
			protected String doInBackground(String... params) {
				JSONObject json2=new JSONObject();
				String res="";
				 try {
					json2.put("pid", params[0]);
					json2.put("rating", params[1]);
					HttpClient client = new DefaultHttpClient();
		    		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
		    																		// Limit
		    		HttpResponse response = null;
		    		//HttpPost post= new HttpPost("http://10.0.2.2:8080/JsonTest/RatePOI");
		    		HttpPost post= new HttpPost("http://192.168.131.1:8080/JsonTest/RatePOI");
		    		StringEntity se = new StringEntity(json2.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					response = client.execute(post);
					if (response != null) {
							InputStream in1 = response.getEntity().getContent();
							res = convertStreamToString(in1);
									
					} 
					return res;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				return null;
			}
			
			
		}
		
		
		
		public class CommentPOI extends AsyncTask<String, Integer, String>{
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				
					Intent intent=new Intent(getApplicationContext(),PublicPOI.class);
					intent.putExtra("poiId", pid);
					startActivity(intent);
					finish();
				
			}
			
			@Override
			protected String doInBackground(String... params) {
				JSONObject json2=new JSONObject();
				String res="";
				 try {
					json2.put("poiID", params[0]);
					json2.put("userID", params[1]);
					json2.put("comment", params[2]);
					HttpClient client = new DefaultHttpClient();
		    		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
		    																		// Limit
		    		HttpResponse response = null;
		    		//HttpPost post= new HttpPost("http://10.0.2.2:8080/JsonTest/CommentPOI");
		    		HttpPost post= new HttpPost("http://192.168.131.1:8080/JsonTest/CommentPOI");
		    		StringEntity se = new StringEntity(json2.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					response = client.execute(post);
					if (response != null) {
							InputStream in1 = response.getEntity().getContent();
							res = convertStreamToString(in1);
									
					} 
					return res;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				return null;
			}
			
			
		}
		  public String convertStreamToString(InputStream is) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return sb.toString();
			}
		
		
		@Override
		protected void onDestroy() {
			
			super.onDestroy();
		}
		
		
	

}
