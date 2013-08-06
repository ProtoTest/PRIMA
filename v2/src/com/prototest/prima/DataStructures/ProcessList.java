package com.prototest.prima.DataStructures;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.prototest.PRIMA;
import com.prototest.prima.DeviceMonitor;

public class ProcessList 
{
	private List<ProcessStats> processes;
	private int numProcesses = 0;
	
	public ProcessList()
	{
		this.processes = new ArrayList<ProcessStats>();
		Init();
	}
	
	public void Init()
	{
		ActivityManager manager = (ActivityManager)PRIMA.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pManager = PRIMA.getAppContext().getPackageManager();
		List<RunningAppProcessInfo> service=  manager.getRunningAppProcesses();
		 for (RunningAppProcessInfo app : service) {
			int[] pids = new int[1];
			pids[0] = app.pid;
			Debug.MemoryInfo[] info = manager.getProcessMemoryInfo(pids);
			ProcessStats stats = new ProcessStats();
			stats.packageName = app.processName;
			stats.name = stats.getAppName(stats.packageName);
			stats.memUsedKb = info[0].getTotalPss();
			stats.pid = app.pid;
			stats.uid = app.uid;
			stats.user = pManager.getNameForUid(stats.uid);
            stats.icon = stats.getAppIcon(stats.packageName);
            
            ProcessStats stat = DeviceMonitor.getDeviceMonitor().topQuery.getProcessStatsByPid(stats.pid);
            stats.cpuUsage = stat.cpuUsage;
            stats.numThreads = stat.numThreads;
			processes.add(stats);
		 }
	}

	
	public ProcessStats get(int i)
	{
		return this.processes.get(i);
	}
	
	public void add(ProcessStats process)
	{
		processes.add(process);
		numProcesses++;
	}
	public List<ProcessStats> getList()
	{
		return processes;
	}
	public int getNumProcesses()
	{
		return numProcesses;
	}
	
	public ProcessStats[] getArray()
	{
		return processes.toArray(new ProcessStats[processes.size()]);
	}
	
	public ProcessStats getProcessWithPid(int pid)
	{
		for (ProcessStats stat : processes) {
			if(stat.pid==pid)
				return stat;	
		}
		return null;
	}

}

