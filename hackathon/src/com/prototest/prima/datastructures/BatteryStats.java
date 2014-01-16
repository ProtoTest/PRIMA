package com.prototest.prima.datastructures;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.prototest.prima.PrimaApp;
import com.prototest.prima.contentprovider.PrimaContentProvider;
import com.prototest.prima.database.BATTStatsTable;

public class BatteryStats implements SystemStats {

   private static final String TAG = "BatteryStats";
   private boolean recording = false;
   public float percent = -1;
   public int scale = -1;
   public int level = -1;
   public int voltage = -1;
   public int temp = -1;
   
   public float tempPercent = -1;
   public int tempScale = -1;
   public int tempLevel = -1;
   public int tempVoltage = -1;
   public int tempTemp = -1;
   
   private BroadcastReceiver  batteryReceiver = new BroadcastReceiver() {
       @Override
       public void onReceive(Context context, Intent intent) {
    	  tempLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    	  tempScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
          tempTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
          tempVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
          tempPercent = ((float) level / (float) scale) * 100;
       }
    };
   public BatteryStats() {
	  StartListening(); 
   }

   @Override
   public void GetStats() {  
      percent = tempPercent;
      scale = tempScale;
      voltage = tempVoltage;
      level = tempLevel;
      temp = tempTemp;
      
   }
   
   private void StopListening()
   {
	   batteryReceiver.abortBroadcast();
	   recording = false;
   }
   
   private void StartListening()
   {
	   if(!recording){
		   RegisterBatteryReceiver();
		   recording=true;
	   }
   }

   private void RegisterBatteryReceiver() {
	   Log.d(TAG,"RegisterReceiver");
      IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
      PrimaApp.getAppContext().registerReceiver(batteryReceiver, filter);
   }

   @Override
   public void ProcessStats() {
      Log.d(TAG, "ProcessStats");

      ContentValues values = new ContentValues();
      values.put(BATTStatsTable.COLUMN_LEVEL, this.level);
      values.put(BATTStatsTable.COLUMN_SCALE, this.scale);
      values.put(BATTStatsTable.COLUMN_TEMP, this.temp);
      values.put(BATTStatsTable.COLUMN_VOLTAGE, this.voltage);

      PrimaApp.getAppContext().getContentResolver()
            .insert(PrimaContentProvider.CONTENT_URI_BATT, values);
   }
}
