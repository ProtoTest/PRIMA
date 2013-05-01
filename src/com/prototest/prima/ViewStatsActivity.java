package com.prototest.prima;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewStatsActivity extends Activity {
	
public TextView batteryTextView ;
public TextView cpuTextView;
public TextView memTextView;
public TextView procTextView;
public LinearLayout statsLayout;

public DeviceMonitor monitor;

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
	    
	    displayStats();
	        
		    Runnable myRunnable = new Runnable() {
		         @Override
		         public void run() {
		              while (true) {
		                   try {
		                	   procTextView.post(new Runnable() { 
			                        @Override
			                        public void run() {
			                        	displayStats();			                        	
			                        }});
			             
		                	   Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 
	              }   
		         }
		    };
		    
		    Thread displayThread = new Thread(myRunnable);
		    displayThread.start();
		    
	}
	
	public void displayStats()
	{
		procTextView.setText(monitor.getProcessInfo());
    	memTextView.setText(monitor.getMemoryInfo());
    	cpuTextView.setText(monitor.getCpuUsage());	
    	batteryTextView.setText(monitor.getBatteryInfo());
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
