package com.example.mapdemo;



import com.example.comtools.JSONParser;
import com.example.imagerender.ImageLoader;
import com.example.jsonentities.POIDetails;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CreatePOI extends Activity {
Button takePhoto;
Button createPOI;
EditText name;
EditText address;
EditText description;
ImageView mimage;
Bitmap mImageBitmap;
String latitude="",longitude="",category="",picture="";
JSONParser jp;
private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_poi);
		takePhoto=(Button) findViewById(R.id.photo);
		createPOI=(Button) findViewById(R.id.createpoibtn);
		name=(EditText)findViewById(R.id.poiname);
		address=(EditText)findViewById(R.id.poiaddress);
		description=(EditText)findViewById(R.id.poidescription);
		mimage=(ImageView) findViewById(R.id.imageView1);
		latitude=getIntent().getDoubleExtra("latitude",0)+"";
		longitude=getIntent().getDoubleExtra("longitude",0)+"";
		Spinner spinner=(Spinner) findViewById(R.id.spinner1);
		String [] values={"Hotel","Attraction","Shopping","Transport"};
		ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, values);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			
				TextView cat=(TextView) arg1;
				category=cat.getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				category="";
				
			}
		});
		
		
		takePhoto.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				 startActivityForResult( takePictureIntent, 2);
			    
			}
		});
		
		
		createPOI.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!name.getText().toString().trim().isEmpty() || !address.getText().toString().trim().isEmpty() || !description.getText().toString().trim().isEmpty()){
				new Send().execute(latitude,longitude,category,picture);
				}else{
					Toast.makeText(getApplicationContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void handleSmallCameraPhoto(Intent intent) {
	    Bundle extras = intent.getExtras();
	    ImageLoader imgRend=new ImageLoader();
	    mImageBitmap = (Bitmap) extras.get("data");
	    mimage.setImageBitmap(mImageBitmap);
	    picture=imgRend.BitMapToString(mImageBitmap);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		handleSmallCameraPhoto(data);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_poi, menu);
		return true;
	}
	
	public class Send extends AsyncTask<String, Integer, String>{
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(CreatePOI.this);
	        progressDialog.setCancelable(false);
	        progressDialog.setMessage("Retrieving information...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	    }
		
		@Override
		protected void onPostExecute(String result) {
			
		if(result.contains("success")){
			Toast.makeText(getApplicationContext(), "Added Successfully",Toast.LENGTH_LONG).show();
			progressDialog.dismiss();
			finish();
			
		}else{
			//Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_LONG).show();
			progressDialog.dismiss();
		}
		}
		

		@Override
		protected String doInBackground(String... params) {
			try {
				jp=new JSONParser();
				Gson gson=new Gson();
				POIDetails pois=new POIDetails();
				pois.setLatitude(params[0]);
				pois.setLongitude(params[1]);
				pois.setName(name.getText().toString());
				pois.setPicture(params[3]);
				pois.setAddress(address.getText().toString());
				pois.setCategory(params[2]);
				pois.setDescription(description.getText().toString());
				String tres=gson.toJson(pois,POIDetails.class);
				//String ress=jp.sendJSONToUrl("http://10.0.2.2:8080/JsonTest/CreatePOI", tres);
				String ress=jp.sendJSONToUrl("http://192.168.131.1:8080/JsonTest/CreatePOI", tres);
				return ress;
			} catch (Exception e) {
				return e.getMessage();
			}
			
		}
	}
	

}
