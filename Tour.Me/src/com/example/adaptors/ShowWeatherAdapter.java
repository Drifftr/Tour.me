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
import com.example.jsonentities.WeatherEntity;
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

public class ShowWeatherAdapter extends ArrayAdapter<WeatherEntity>{

	private  Context context;
	private  ArrayList<WeatherEntity> weather;
	ViewHolder holder;
	Bitmap image;
	HashMap<String,Bitmap> hm=new HashMap<String, Bitmap>();
	public ShowWeatherAdapter(Context context, int textViewResourceId,
			ArrayList<WeatherEntity> weather) {
		super(context,R.layout.adaptor_station,weather);
		this.context = context;
		this.weather = weather;
	}
	

	

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.adaptor_weather, parent,false);
			holder = new ViewHolder();
			holder.description=(TextView)v.findViewById(R.id.description);
			holder.main=(TextView)v.findViewById(R.id.mainwet);
			holder.icon=(ImageView)v.findViewById(R.id.icon);
			holder.humidity=(TextView)v.findViewById(R.id.pressure);
			holder.tempmax=(TextView)v.findViewById(R.id.tempmax);
			holder.tempmin=(TextView)v.findViewById(R.id.tempmin);
			holder.temp=(TextView)v.findViewById(R.id.temp);


			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		WeatherEntity weatherResult = weather.get(position);

		holder.icon.setImageBitmap(weatherResult.getIcon());
		holder.description.setText(weatherResult.getDescription());
		holder.main.setText(weatherResult.getMain());
		holder.humidity.setText("Humidity :"+weatherResult.getHumidity()+" %");
		holder.temp.setText("");
		holder.tempmax.setText("Max temp :"+Math.round(Double.parseDouble(weatherResult.getTemp_max()))+" C");
		holder.tempmin.setText("Min temp :"+Math.round(Double.parseDouble(weatherResult.getTemp_min()))+" C");
		
		
		
		return v;
	}
	
	public class ViewHolder{
		
		public TextView description,main,humidity,pressure,tempmax,tempmin,temp;
		public ImageView icon;
	}
	
	
}