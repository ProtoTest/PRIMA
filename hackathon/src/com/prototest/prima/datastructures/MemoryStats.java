package com.prototest.prima.datastructures;

import android.content.ContentValues;
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

   @Override
   public void GetStats() {
      Log.d("MemoryStats", "GetStats");
      // ProcessBuilder cmd;
      // StringBuffer strMemory = new StringBuffer();
      // ActivityManager actvityManager = (ActivityManager)
      // PrimaApp.getAppContext().getSystemService(
      // android.content.Context.ACTIVITY_SERVICE);
      // ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo();
      // actvityManager.getMemoryInfo(mInfo);
      // this.available = mInfo.availMem / 1048576L;
      // Log.d("MemoryStats",String.valueOf(this.available));
      // this.max = mInfo.totalMem/1048576L;
      // this.current = this.max - this.available;
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
