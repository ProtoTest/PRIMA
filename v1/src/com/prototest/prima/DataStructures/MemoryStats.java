package com.prototest.prima.DataStructures;

public class MemoryStats {
	public long current;
	public long max;
	public long available;
	public double getPercentage()
	{
		double percent = 100*((double)this.current/(double)this.max);
	return percent;	
	}

}
