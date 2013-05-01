package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class RecordingActivity extends Activity {


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recording);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		   BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		        int scale = -1;
		        int level = -1;
		        int voltage = -1;
		        int temp = -1;
		        @Override
		        public void onReceive(Context context, Intent intent) {
		            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
		            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
		            Log.e("BatteryManager", "level is "+level+"/"+scale+", temp is "+temp+", voltage is "+voltage);
		        }
		    };
		    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		    registerReceiver(batteryReceiver, filter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_recording, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void stopRecording(View view) {
		Intent intent = new Intent(this, StoppedActivity.class);
		//EditText editText = (EditText) findViewById(R.id.max_duration_time);
		//String message = editText.getText().toString();
		//intent.putExtra(EXTRA_MESSAGE, message);
		
		startActivity(intent);
	}

}
