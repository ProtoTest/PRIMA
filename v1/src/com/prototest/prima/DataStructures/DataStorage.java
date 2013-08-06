package com.prototest.prima.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {
	public long tickMs;
	public long maxTime;
	public boolean trackBattery;
	public boolean trackProcessor;
	public boolean trackMemory;
	
	public List<BatteryStats> battStats;
	public List<MemoryStats> memStats;
	public List<ProcessorStats> procStats;
	
	public DataStorage()
	{
		this.battStats = new ArrayList<BatteryStats>();
		this.memStats = new ArrayList<MemoryStats>();
		this.procStats = new ArrayList<ProcessorStats>();		
	}
	
}
