package com.prototest.prima.datastructures;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.os.Build;
import android.util.Log;

import com.prototest.prima.PrimaApp;
import com.prototest.prima.contentprovider.PrimaContentProvider;
import com.prototest.prima.database.MEMStatsTable;

public class MemoryStats implements SystemStats {
   private static final String TAG = "MemoryStats";

   public long current;
   public long max;
   public long available;

   public MemoryStats() {

   }

   public double getPercentage() {
      double percent = 100 * ((double) this.current / (double) this.max);
      return percent;
   }

   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@Override
   public void GetStats() {
      Log.d("MemoryStats", "GetStats");
      try {
    	  ProcessBuilder cmd;
          StringBuffer strMemory = new StringBuffer();
          ActivityManager actvityManager = (ActivityManager)
          PrimaApp.getAppContext().getSystemService(
          android.content.Context.ACTIVITY_SERVICE);
          ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo();
          actvityManager.getMemoryInfo(mInfo);
          this.available = mInfo.availMem / 1048576L;
          this.max = mInfo.totalMem/1048576L;
          this.current = this.max - this.available;
          //Log.d("MemoryStats",String.valueOf(this.available) + "+" + String.valueOf(this.current) +"=" + String.valueOf(this.max));
	} catch (Exception e) {
		Log.d("MemoryStats","GetStats : Exception caught : " + e.getMessage());
	}
      
   }

   @Override
   public void ProcessStats() {
      Log.d(TAG, "ProcessStats()");
      ContentValues values = new ContentValues();

      values.put(MEMStatsTable.COLUMN_AVAILABLE, this.available);
      values.put(MEMStatsTable.COLUMN_CURRENT, this.current);

      PrimaApp.getAppContext().getContentResolver()
            .insert(PrimaContentProvider.CONTENT_URI_MEM, values);
   }

}
