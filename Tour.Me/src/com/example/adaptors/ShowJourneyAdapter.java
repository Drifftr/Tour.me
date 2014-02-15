package com.example.adaptors;





import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.imagerender.ImageLoader;
import com.example.jsonentities.Journey;
import com.example.jsonentities.POIDetails;
import com.example.mapdemo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowJourneyAdapter extends ArrayAdapter<Journey>{

	private  Context context;
	private  ArrayList<Journey> journeys;
	ViewHolder holder;
	Bitmap image;
	HashMap<String,Bitmap> hm=new HashMap<String, Bitmap>();
	public ShowJourneyAdapter(Context context, int textViewResourceId,
			ArrayList<Journey> journeys) {
		super(context,R.layout.adaptor_journey_item,journeys);
		this.context = context;
		this.journeys = journeys;
	}
	

	

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.adaptor_journey_item, parent,false);

			holder = new ViewHolder();
			holder.poiName = (TextView) v.findViewById(R.id.firstline);
			holder.poiDesc = (TextView) v.findViewById(R.id.secondLine);
			holder.poiImage=(ImageView) v.findViewById(R.id.icon);


			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Journey journey = journeys.get(position);

		holder.poiName.setText(journey.getName());
		holder.poiDesc.setText(journey.getDate()+" | "+journey.getTime());
		
		
		
		return v;
	}
	
	public class ViewHolder{
		
		public TextView poiName;
		public TextView poiDesc;
		public ImageView poiImage;
	}
	
	
}
