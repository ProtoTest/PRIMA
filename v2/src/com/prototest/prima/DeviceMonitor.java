package com.prototest.prima;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Debug;
import android.os.Handler;
import android.widget.TextView;

import com.prototest.PRIMA;
import com.prototest.Queries.TopQuery;
import com.prototest.prima.DataStructures.BatteryStats;
import com.prototest.prima.DataStructures.MemoryStats;
import com.prototest.prima.DataStructures.ProcessList;
import com.prototest.prima.DataStructures.ProcessStats;
import com.prototest.prima.DataStructures.ProcessorStats;

//singleton. get with getDeviceMonitor();
@SuppressLint("NewApi")
public class DeviceMonitor {
	
	
	
	private class GetDataTask extends AsyncTask<Void, Void, Void> {
 	    @Override
	    protected Void doInBackground(Void... arg0) {
	        try {
				getDeviceStats();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	    }
 	    
 	    @Override
 	    protected void onPostExecute(Void result) {
 	    	// TODO Auto-generated method stub
 	    	super.onPostExecute(result);
 	    	loaded=true;
 	    }
	}
	AsyncTask<Void, Void, Void> task;
	BufferedReader reader;
	public static boolean loaded = false;
	public int max_duration_ms = 30000;
	public int record_frequency_ms = 5000;
	public int num_ticks = 0;
	private static Boolean locked = false;
	public static Boolean monitorEnabled = false;
	public static Boolean recordingEnabled = false;
	private static DeviceMonitor _monitor;
	
	private Handler handler;

	public Context context;
	public BatteryStats battery;
	public MemoryStats memory;
	public ProcessorStats processor;
	public ProcessStats[] processes;
	
	public BatteryStats[] batteryStats;
	public MemoryStats[] memoryStats;
	public ProcessorStats[] processorStats;
	public ProcessList[] processListStats;
	private Runnable recordingRunnable;
	private Thread recordingThread;
	
	public TopQuery topQuery;
	
	private TextView textview;
	
	private DeviceMonitor() throws IOException
	{

		this.context= PRIMA.getAppContext();
		this.battery = new BatteryStats();
		this.memory = new MemoryStats();
		this.processor = new ProcessorStats();
		this.processes = new ProcessStats[100];
		this.batteryStats = new BatteryStats[max_duration_ms/record_frequency_ms];
		this.memoryStats = new MemoryStats[max_duration_ms/record_frequency_ms];
		this.processorStats = new ProcessorStats[max_duration_ms/record_frequency_ms];
		this.processListStats = new ProcessList[max_duration_ms/record_frequency_ms];
		this.handler = new Handler();
		task = new GetDataTask();
	}
	
	public static DeviceMonitor getDeviceMonitor()
	{
		try {
			if(locked==false)
			{
				locked=true;
				_monitor = new DeviceMonitor();
			}
			return _monitor;
		} catch (Exception e) {
			return null;
		}

	}
	
	private Runnable monitor = new Runnable() {
		   @Override
		   public void run() {
			   task = new GetDataTask();
			   task.execute();
     		   if(monitorEnabled)
     		   {
     			   handler.postDelayed(this, record_frequency_ms);
     		   }
		   }
		};
		
	private Runnable recording = new Runnable() {
		   @Override
		   public void run() {
			   recordDeviceStats();
        	   num_ticks++;
        	   if((num_ticks*record_frequency_ms)<(max_duration_ms))
        	   {
        		   if(recordingEnabled)
        		   {
        			   handler.postDelayed(this, record_frequency_ms);
        		   }
        	   }
		   }
		};
		

			
	public void WaitForData()
	{
		try {
			task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void ResetRecording()
	{
		num_ticks=0;
		this.batteryStats = new BatteryStats[max_duration_ms/record_frequency_ms];
		this.memoryStats = new MemoryStats[max_duration_ms/record_frequency_ms];
		this.processorStats = new ProcessorStats[max_duration_ms/record_frequency_ms];
		this.processListStats = new ProcessList[max_duration_ms/record_frequency_ms];
		
	}
	
	public void StartMonitor()
	{
		monitorEnabled = true;
		task = new GetDataTask();
	    task.execute();
	    try {
			task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.post(monitor);
		
	}
	
	public void StartRecording()
	{
		ResetRecording();
		recordDeviceStats();
		recordingEnabled = true;
		handler.post(recording);
	}
	
	public void StopRecording()
	{
		recordingEnabled = false;
		handler.removeCallbacks(recording);
	}
	public void StopMonitor()
	{
		monitorEnabled = false;
		handler.removeCallbacks(monitor);
	}
	public void Kill()
	{
		StopRecording();
		StopMonitor();
		handler.removeCallbacks(null);
	}
	
	private void getDeviceStats() throws IOException, InterruptedException
	{		
		this.topQuery = new TopQuery();
		this.processor = new ProcessorStats();
		this.memory = new MemoryStats();
		this.processes = new ProcessList().getArray();
			

	}
	
	public void recordDeviceStats()
	{
		
		this.batteryStats[num_ticks] = this.battery;
		this.memoryStats[num_ticks] = this.memory;
		this.processorStats[num_ticks] = this.processor;
		//this.processListStats[num_ticks] = this.;
	}

	
	
	
	



	
//	private ProcessStats[] getIconsAndMemoryUsage()
//	{
//		for (ProcessStats stats : processes) {
//			stats.icon = getAppIcon(stats.pid);
//			stats.memUsedKb = getMemUsedByPid(stats.pid);
//		}
//		return processes;
//	}
	

//	public ProcessStats[] getProcessStats()
//	{
//		ProcessStats[] procStats = new ProcessStats[20];
//	
//		try {
//
//			Process p = Runtime.getRuntime().exec("top -m 50 -d 1 -n 1");
//			p.waitFor();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			
//			String line = reader.readLine();
//			while(!line.contains("PID"))
//			{
//				line=reader.readLine();
//			}
//			line = reader.readLine();
//			int i=0;
//			while(line!=null)
//			{
//
//				
//				String[] toks = new String[20];
//				line=line.replaceAll("^[ \t]+|[ \t]+$", "");
//				
//				toks=line.split(" +");
//				ProcessStats stats = new ProcessStats();
//				try {
//					
//					stats.pid = Integer.parseInt(toks[0]);
//					stats.cpuUsage = (Integer.parseInt(toks[2].replace("%","")));
//					stats.numThreads = Integer.parseInt(toks[4]);
//					stats.memUsedKb = Integer.parseInt(toks[6].replace("K",""));
//					if(toks.length==10)
//					{
//						stats.user = toks[8];
//						stats.name = toks[9];
//					}
//					else
//					{
//						stats.user=toks[7];
//						stats.name=toks[8];
//					}
//					procStats[i]=stats;
//					line = reader.readLine();
//					i++;
//				} catch (Exception e) {
//
//					line= reader.readLine();
//				}
//				
//				
//				
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//		}
//		return procStats;
//	
//	}
//	
//	
	
	


}
