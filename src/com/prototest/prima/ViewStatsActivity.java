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
	    
	    
	    displayStats();
	    
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
		            
		            batteryTextView.setText("level is "+level+"/"+scale+", temp is "+temp+", voltage is "+voltage);
		        }
		    };
		    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		    registerReceiver(batteryReceiver, filter);
		    


		    
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
		procTextView.setText(getProcessInfo());
    	memTextView.setText(getMemoryInfo());
    	cpuTextView.setText(getCpuUsage());		
	}
	
	@SuppressLint("NewApi")
	private String getMemoryInfo()
	{
	ProcessBuilder cmd;
	StringBuffer strMemory = new StringBuffer();
	ActivityManager actvityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE );
	ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo ();
	actvityManager.getMemoryInfo( mInfo );
	long avail = mInfo.availMem/1048576L;
	long total = mInfo.totalMem/1048576L;
	long used = total - avail;

	
	strMemory.append("Available Memory : ");
	strMemory.append(avail + " mb");
	strMemory.append("\n");
	strMemory.append("Total Memory : ");
	strMemory.append(total + " mb");
	strMemory.append("\n");
	strMemory.append("Used :");
	strMemory.append(used + " mb");
	strMemory.append("\n");
	strMemory.append("Percent Used:");
	strMemory.append(((float)used/(float)total)*100 + " %");
	strMemory.append("\n");
	
	return strMemory.toString();
	}

	public String getProcessInfo()
	{
		String message = "";
		try {
			// -m 10, how many entries you want, -d 1, delay by how much, -n 1,
			// number of iterations
			Process p = Runtime.getRuntime().exec("top -m 10 -d 1 -n 1");

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			int i = 0;
			String line = reader.readLine();
			while(!line.contains("PID"))
			{
				line = reader.readLine();
			}
			reader.readLine();
			reader.readLine();
			while (line != null) {
				message += line.substring(0, 50) +"\n";
				line = reader.readLine();
				i++;
			}

			p.waitFor();


		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "Caught", Toast.LENGTH_SHORT)
					.show();
		}
		return message;
	}	
	
	private int getNumCores() {
	    //Private Class to display only CPU devices in the directory listing
	    class CpuFilter implements FileFilter {
	        @Override
	        public boolean accept(File pathname) {
	            //Check if filename is "cpu", followed by a single digit number
	            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
	                return true;
	            }
	            return false;
	        }      
	    }

	    try {
	        //Get directory containing CPU info
	        File dir = new File("/sys/devices/system/cpu/");
	        //Filter to only list the devices we care about
	        File[] files = dir.listFiles(new CpuFilter());
	        //Return the number of cores (virtual CPU devices)
	        return files.length;
	    } catch(Exception e) {
	        //Default to return 1 core
	        return 1;
	    }
	}
	
	private String getCpuUsage() {
	    try {
	        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
	        String load = "";
       
	        String[] toks = new String[10];
	        long idle;
	        long cpu;
	        int numCores = getNumCores();
	        String message = "Number of Cores: " + String.valueOf(getNumCores()) + "\n";
	        for(int i=0;i<=getNumCores()+1;i++)
	        {
	        	load = reader.readLine();
	        	toks = load.split(" ");
	        	if(toks[0].contains("cpu"))
	        	{
	        		try
	        		{
	        			
	        		idle = Long.parseLong(toks[4]);
		        	cpu = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) + Long.parseLong(toks[3])
		  	              + Long.parseLong(toks[5]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]);  
		        	float percent = ((float)cpu/(float)idle)*100;
		        	message+= toks[0] + ": " + String.valueOf(cpu) + "/" + String.valueOf(idle) + " " + String.valueOf(percent) + "%\n";
	        		}
	        		catch(Exception e)
	        		{
	        		
	        		}
	        	}
	        	
	        }
	        reader.close();
        	return message;
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return "";
	    }
	} 
	
	private float getUsage() {
	    try {
	        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
	        String load = reader.readLine();

	        String[] toks = load.split(" ");

	        long idle1 = Long.parseLong(toks[5]);
	        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	        try {
	            Thread.sleep(360);
	        } catch (Exception e) {}

	        reader.seek(0);
	        load = reader.readLine();
	        reader.close();

	        toks = load.split(" ");

	        long idle2 = Long.parseLong(toks[5]);
	        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
	            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	        return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }

	    return 0;
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
