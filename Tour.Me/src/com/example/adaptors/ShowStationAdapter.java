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
import com.example.jsonentities.TrainDetails;
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

public class ShowStationAdapter extends ArrayAdapter<TrainDetails>{

	private  Context context;
	private  ArrayList<TrainDetails> trains;
	ViewHolder holder;
	Bitmap image;
	HashMap<String,Bitmap> hm=new HashMap<String, Bitmap>();
	public ShowStationAdapter(Context context, int textViewResourceId,
			ArrayList<TrainDetails> trains) {
		super(context,R.layout.adaptor_station,trains);
		this.context = context;
		this.trains = trains;
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

		TrainDetails trainsde = trains.get(position);

		holder.poiName.setText("No:"+trainsde.getName()+" at "+trainsde.getArrivalTime());
		holder.poiDesc.setText("Arrives to "+trainsde.getEndStationName()+" at "+trainsde.getArrivalAtDestinationTime());
		
		
		
		return v;
	}
	
	public class ViewHolder{
		
		public TextView poiName;
		public TextView poiDesc;
		public ImageView poiImage;
	}
	
	
}
