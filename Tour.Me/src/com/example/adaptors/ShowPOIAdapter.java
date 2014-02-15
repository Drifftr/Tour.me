package com.example.adaptors;





import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.imagerender.ImageLoader;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowPOIAdapter extends ArrayAdapter<POIDetails> implements Filterable{
	ImageLoader img=new ImageLoader();
	private  Context context;
	private  ArrayList<POIDetails> POIs,out;
	public ViewHolder holder;
	Bitmap image;
	HashMap<String,Bitmap> hm=new HashMap<String, Bitmap>();
	public ShowPOIAdapter(Context context, int textViewResourceId,
			ArrayList<POIDetails> POIs) {
		super(context,R.layout.adaptor_poi_item,POIs);
		this.context = context;
		this.POIs = POIs;
	}
	@Override
    public int getCount() {
        return POIs.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
	
	@Override
	public Filter getFilter() {
		 Filter filter = new Filter() {

             @SuppressWarnings("unchecked")
             @Override
             protected void publishResults(CharSequence constraint,FilterResults results) {

                 POIs = (ArrayList<POIDetails>) results.values; // has the filtered values
                 notifyDataSetChanged();  // notifies the data with new filtered values
             }

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<POIDetails> FilteredArrList = new ArrayList<POIDetails>();

                if (out == null) {
                	out = new ArrayList<POIDetails>(POIs); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = out.size();
                    results.values = out;
                } else {
                	Log.w("****************************************************", "printed");
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < out.size(); i++) {
                        POIDetails data = out.get(i);
                        if (data.getName().toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
				}
			};

		return filter;
}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.adaptor_poi_item, parent,false);

			holder = new ViewHolder();
			holder.poiName = (TextView) v.findViewById(R.id.firstline);
			holder.poiDesc = (TextView) v.findViewById(R.id.secondLine);
			holder.poiImage=(ImageView) v.findViewById(R.id.icon);


			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		POIDetails POI = POIs.get(position);
		
		holder.poiName.setText(POI.getName());
		holder.poiDesc.setText(POI.getDescription());
		
		
		
		return v;
	}
	
	public class ViewHolder{
		
		public TextView poiName;
		public TextView poiDesc;
		public ImageView poiImage;
	}
	
}
