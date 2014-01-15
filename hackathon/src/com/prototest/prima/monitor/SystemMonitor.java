package com.prototest.prima.monitor;
import java.util.concurrent.*;

import com.prototest.prima.datastructures.SystemStats;

import static java.util.concurrent.TimeUnit.*;
import android.text.format.Time;

public class SystemMonitor {
	protected SystemStats stats;
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> handle;
	
	public SystemMonitor(SystemStats stats){
		this.stats = stats;
	}
	
	protected Runnable monitor = new Runnable() {
		@Override
		public void run() {
			stats.GetStats();
		}
	};
	
	public void StartMonitoring()	{
		handle = scheduler.scheduleAtFixedRate(monitor, 0, 1, SECONDS);
	}
	
	public void StopMonitoring(){
		handle.cancel(true);
	}
}
