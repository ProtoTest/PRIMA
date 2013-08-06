package com.prototest.prima.DataStructures;

import com.prototest.PRIMA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryStats {
	public float percent;
	public int scale = -1;
	public int level = -1;
	public int voltage = -1;
	public int temp = -1;
	public BatteryStats()
	{
		RegisterBatteryReceiver();
		
	}
	
	public void RegisterBatteryReceiver()
	{
		BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
	            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
	            
	            percent = ((float)level/(float)scale)*100;
	        }
	    };
	    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    PRIMA.getAppContext().registerReceiver(batteryReceiver, filter);	
	
	}
}
