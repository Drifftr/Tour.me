package com.example.mapdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class PathDetails extends Activity {

	TextView path,distance,time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path_details);
		path=(TextView)findViewById(R.id.pathto);
		time=(TextView)findViewById(R.id.time);
		distance=(TextView)findViewById(R.id.distance);
		path.setText(getIntent().getExtras().getString("path"));
		time.setText(getIntent().getExtras().getString("time"));
		distance.setText(getIntent().getExtras().getString("distance"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_path_details, menu);
		return true;
	}

}
