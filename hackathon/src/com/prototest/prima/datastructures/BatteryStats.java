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

   public float percent;
   public int scale = -1;
   public int level = -1;
   public int voltage = -1;
   public int temp = -1;

   public BatteryStats() {
   }

   @Override
   public void GetStats() {
      Log.d("BatteryStats", "GetStats");
      // RegisterBatteryReceiver();
   }

   public void RegisterBatteryReceiver() {
      BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

         @Override
         public void onReceive(Context context, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);

            percent = ((float) level / (float) scale) * 100;
         }
      };
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
