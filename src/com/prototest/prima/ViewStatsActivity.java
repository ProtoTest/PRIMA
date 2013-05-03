package com.prototest.prima;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewStatsActivity extends Activity {
	
public TextView batteryTextView ;
public TextView cpuTextView;
public TextView memTextView;
public TextView procTextView;
public LinearLayout statsLayout;

public DeviceMonitor monitor;
private Handler handler;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_stats);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	    

	    batteryTextView = (TextView) findViewById(R.id.battery_stats_text);
	    procTextView = (TextView) findViewById(R.id.process_stats_text); 
	    cpuTextView = (TextView) findViewById(R.id.cpu_stats_text); 
	    memTextView = (TextView) findViewById(R.id.memory_stats_text);  
	    monitor=new DeviceMonitor(this);
	    handler= new Handler();
	    getStats();
	        
	}
	
	@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			handler.removeCallbacks(runnable);

		}
	
	@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			handler.removeCallbacks(runnable);
		}
	
	public void displayStats()
	{
		procTextView.setText(monitor.getProcessInfo());
    	memTextView.setText(monitor.getMemoryInfo());
    	cpuTextView.setText(monitor.getCpuUsage());	
    	batteryTextView.setText(monitor.getBatteryInfo());
	}
	
	private Runnable runnable = new Runnable() {
		   @Override
		   public void run() {
		      displayStats();
		      handler.postDelayed(this, 1000);
		   }
		};

	public void getStats()
	{
		GlobalData.recordingEnabled = true;
		handler.post(runnable);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_stats, menu);
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

}
