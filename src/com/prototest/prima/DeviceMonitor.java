//package com.prototest.prima;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.RandomAccessFile;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.app.ActivityManager;
//import android.app.ActivityManager.MemoryInfo;
//import android.app.ActivityManager.RunningAppProcessInfo;
//
//import com.prototest.prima.DataStructures.MemoryStats;
//import com.prototest.prima.DataStructures.ProcessorStats;
//
//public class DeviceMonitor {
//	public static MemoryStats GetMemInfo()		
//	{
//		MemoryInfo mi = new MemoryInfo();
//		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//		activityManager.getMemoryInfo(mi);
//		long availableMegs = mi.availMem / 1048576L;
//		long totalMegs = mi.totalMem / 1048576L;
//		long usedMegs = getUsedMemorySize() / 1048576L;
//		
//	    long freeSize = 0L;
//	    long totalSize = 0L;
//	    long usedSize = -1L;
//	    try {
//	        Runtime info = Runtime.getRuntime();
//	        freeSize = info.freeMemory() / 1048576L;
//	        totalSize = info.totalMemory() / 1048576L;
//	        usedSize = totalSize - freeSize;
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//		
//		//String message = "Free: " + String.valueOf(freeSize) + ". Total: " + String.valueOf(totalSize) + ". Used: " + String.valueOf(usedSize);
//		
//		MemoryStats memStats = new MemoryStats();
//		memStats.available = freeSize;
//		memStats.current = usedSize;
//		memStats.max = totalSize;
//
//		return memStats;
//		
//	}
//	
//	@SuppressLint("NewApi")
//	private String GetCpuInfo()
//	{
//	ProcessBuilder cmd;
//	StringBuffer strMemory = new StringBuffer();
//	//final ActivityManager activityManager =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//	ActivityManager actvityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE );
//	ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo ();
//	actvityManager.getMemoryInfo( mInfo );
//	strMemory.append("Available Memory : ");
//	strMemory.append(mInfo.availMem/1048576L + " mb");
//	strMemory.append("\n");
//	strMemory.append("Total Memory : ");
//	strMemory.append(mInfo.totalMem/1048576L + " mb");
//	strMemory.append("\n");
//	strMemory.append("\n");
//	String result=strMemory.toString();
//	try{
//	String[] args = {"/system/bin/cat", "/proc/meminfo"};
//	cmd = new ProcessBuilder(args);
//	Process process = cmd.start();
//	InputStream in = process.getInputStream();
//	byte[] re = new byte[1024];
//	while(in.read(re) != -1){
//	System.out.println("itthhe   ====  ---   >>>>    "+new String(re));
//	result = result + new String(re);
//	}
//	in.close();
//	} catch(IOException ex){
//	ex.printStackTrace();
//	}
//	return result;
//	}
//
//	public static void GetRunningProcesses()
//	{
//		ActivityManager activityManager = (ActivityManager)
//                getSystemService(ACTIVITY_SERVICE);
//
//		List<RunningAppProcessInfo> listOfRunningProcess = activityManager
//              .getRunningAppProcesses();
//	
//	}
//	
//	public static ProcessorStats GetProcInfo()
//	{
//	    try {
//	        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
//	        String load = reader.readLine();
//
//	        String[] toks = load.split(" ");
//
//	        long idle1 = Long.parseLong(toks[5]);
//	        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
//	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
//
//	        try {
//	            Thread.sleep(360);
//	        } catch (Exception e) {}
//
//	        reader.seek(0);
//	        load = reader.readLine();
//	        reader.close();
//
//	        toks = load.split(" ");
//
//	        long idle2 = Long.parseLong(toks[5]);
//	        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
//	            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
//
//	        
//	        String message = "CPU1: " + String.valueOf(cpu1) + "/" + String.valueOf(idle1) + "\n";
//	        	message	+= "CPU2:" + String.valueOf(cpu2) + "/" + String.valueOf(idle2) +"\n";
//	        
//	        	
//	        	
//	        return message;
//	    } catch (IOException ex) {
//	        ex.printStackTrace();
//	    }
//
//	    return new ProcessorStats();
//
//	}
//
//	public static String GetBatteryInfo()
//	{
//		
//	}
//
//	public static long getUsedMemorySize() {
//
//	    long freeSize = 0L;
//	    long totalSize = 0L;
//	    long usedSize = -1L;
//	    try {
//	        Runtime info = Runtime.getRuntime();
//	        freeSize = info.freeMemory();
//	        totalSize = info.totalMemory();
//	        usedSize = totalSize - freeSize;
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return usedSize;
//
//	}
//	
//}
