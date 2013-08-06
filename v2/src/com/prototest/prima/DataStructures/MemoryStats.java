package com.prototest.prima.DataStructures;

import com.prototest.PRIMA;

import android.annotation.SuppressLint;
import android.app.ActivityManager;

public class MemoryStats {
	public long current;
	public long max;
	public long available;
	
	@SuppressLint("NewApi") public MemoryStats()
	{
		ProcessBuilder cmd;
		StringBuffer strMemory = new StringBuffer();
		ActivityManager actvityManager = (ActivityManager) PRIMA.getAppContext().getSystemService(android.content.Context.ACTIVITY_SERVICE );
		ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo ();
		actvityManager.getMemoryInfo( mInfo );
		this.available = mInfo.availMem/1048576L;
		this.max = mInfo.totalMem/1048576L;
		this.current = this.max - this.available;
	}
	
	public double getPercentage()
	{
		double percent = 100*((double)this.current/(double)this.max);
	return percent;	
	}

}
