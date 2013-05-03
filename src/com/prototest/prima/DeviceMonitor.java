package com.prototest.prima;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView.FindListener;
import android.widget.TextView;
import android.widget.Toast;

import com.prototest.prima.DataStructures.BatteryStats;
import com.prototest.prima.DataStructures.MemoryStats;
import com.prototest.prima.DataStructures.ProcessorStats;

public class DeviceMonitor {
	
	public int max_duration_ms = 30000;
	public int record_frequency_ms = 1000;
	public int num_ticks = 0;
	
	private Handler handler;

	public Context context;
	private BatteryStats battery;
	private MemoryStats memory;
	private ProcessorStats processor;
	//private processStats process;
	
	public BatteryStats[] batteryStats;
	public MemoryStats[] memoryStats;
	public ProcessorStats[] processorStats;
	
	private Runnable recordingRunnable;
	private Thread recordingThread;
	
	private TextView textview;
	
	public DeviceMonitor(Context context)
	{
		this.context= context;
		this.battery = new BatteryStats();
		this.memory = new MemoryStats();
		this.processor = new ProcessorStats();
		this.batteryStats = new BatteryStats[max_duration_ms/record_frequency_ms];
		this.memoryStats = new MemoryStats[max_duration_ms/record_frequency_ms];
		this.processorStats = new ProcessorStats[max_duration_ms/record_frequency_ms];
		
		this.handler = new Handler();
		RegisterBatteryReceiver();
	}
	
	private Runnable recording = new Runnable() {
		   @Override
		   public void run() {
			   recordDeviceStats();
        	   num_ticks++;
        	   if((num_ticks*record_frequency_ms)<(max_duration_ms))
        	   {
        		   if(GlobalData.recordingEnabled)
        		   {
        			   handler.postDelayed(this, record_frequency_ms);
        		   }
        	   }
		   }
		};
		
	public void ResetRecording()
	{
		num_ticks=0;
		this.batteryStats = new BatteryStats[max_duration_ms/record_frequency_ms];
		this.memoryStats = new MemoryStats[max_duration_ms/record_frequency_ms];
		this.processorStats = new ProcessorStats[max_duration_ms/record_frequency_ms];
	
	}
	
	public void StartRecording()
	{
		ResetRecording();
		GlobalData.recordingEnabled = true;
		handler.post(recording);
	}
	
	public void StopRecording()
	{
		GlobalData.recordingEnabled = false;
		handler.removeCallbacks(null);
	}
	
	public void recordDeviceStats()
	{
			Toast.makeText(this.context, "Recording : " + num_ticks, Toast.LENGTH_SHORT).show();
			this.processor = getCpuStats();
			this.battery = getBatteryStats();
			this.memory = getMemoryStats();
			this.batteryStats[num_ticks] = this.battery;
			this.memoryStats[num_ticks] = this.memory;
			this.processorStats[num_ticks] = this.processor;
			
	}
	
	public void RegisterBatteryReceiver()
	{
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
	            
	            battery.percent = ((float)level/(float)scale)*100;
	            battery.temp = temp;
	            battery.voltage = voltage;
	        }
	    };
	    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    context.registerReceiver(batteryReceiver, filter);	
	
	}
	
	public String getBatteryInfo()
	{
		String message = "Charge: "+this.battery.percent+"% \n";
		message+= "Temp: " + this.battery.temp + "\n";
		message+= "Voltage: " + this.battery.voltage + "\n";   
		    return message;
	}
	public BatteryStats getBatteryStats()
	{
		return this.battery;
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
			
		}
		return message;
	}
	
	@SuppressLint("NewApi")
	public MemoryStats getMemoryStats()
	{
	MemoryStats stats = new MemoryStats();
	ProcessBuilder cmd;
	StringBuffer strMemory = new StringBuffer();
	ActivityManager actvityManager = (ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE );
	ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo ();
	actvityManager.getMemoryInfo( mInfo );
	stats.available = mInfo.availMem/1048576L;
	stats.max = mInfo.totalMem/1048576L;
	stats.current = stats.max - stats.available;
	return stats;

	} 

	@SuppressLint("NewApi")
	public String getMemoryInfo()
	{
	ProcessBuilder cmd;
	StringBuffer strMemory = new StringBuffer();
	ActivityManager actvityManager = (ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE );
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

	public int getNumCores() {
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
	
	public ProcessorStats getCpuStats() {
	    try {
	        
	    	ProcessorStats stats = new ProcessorStats();
	    	
	    	RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
	        String load = "";
       
	        String[] toks = new String[10];
	        long idle;
	        long cpu;
	        int numCores = getNumCores();
	        stats.numProcs = numCores;
	        
	        
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
		        	stats.free = idle;
		        	stats.used = cpu;
		        	stats.percentUsed = percent;
		        	return stats;
		        			
	        		}
	        		catch(Exception e)
	        		{
	        		
	        		}
	        	}
	        	
	        }
	        reader.close();
        	return stats;
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	} 
	
	public String getCpuUsage() {
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
	
	public float getUsage() {

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
		
}
