package com.example.maptools;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class DirectionGrabber {
	public String makeUrl(LatLng source,LatLng dest){
	    StringBuilder urlString = new StringBuilder();
	    urlString.append("http://maps.googleapis.com/maps/api/directions/json");
	    urlString.append("?origin="); //start positie
	    urlString.append(Double.toString(source.latitude));
	    urlString.append(",");
	    urlString.append(Double.toString(source.longitude));
	    urlString.append("&destination="); //end positie
	    urlString.append(Double.toString(dest.latitude));
	    urlString.append(",");
	    urlString.append(Double.toString(dest.longitude));
	    urlString.append("&sensor=false&mode=driving");
	   // String my="http://maps.googleapis.com/maps/api/directions/json?origin=galle&destination=colombo&sensor=false";
	    return urlString.toString();
	  
	}
	
	
	public List<LatLng> decodePoly(String encoded){

	    List<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0;
	    int length = encoded.length();
	    int latitude = 0;
	    int longitude = 0;

	    while(index < length){
	        int b;
	        int shift = 0;
	        int result = 0;

	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);

	        int destLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        latitude += destLat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);

	        int destLong = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        longitude += destLong;

	        poly.add(new LatLng((latitude / 1E5),(longitude / 1E5) ));
	    }
	    return poly;
	}
}
