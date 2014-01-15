package com.prototest.prima.datastructures;

import android.app.ActivityManager;
import android.util.Log;

import com.prototest.prima.PrimaApp;

public class MemoryStats implements SystemStats {
   public long current;
   public long max;
   public long available;

   public MemoryStats() {

   }

   public double getPercentage() {
      double percent = 100 * ((double) this.current / (double) this.max);
      return percent;
   }

   public void GetStats() {
	   Log.d("MemoryStats","GetStats");
//      ProcessBuilder cmd;
//      StringBuffer strMemory = new StringBuffer();
//      ActivityManager actvityManager = (ActivityManager) PrimaApp.getAppContext().getSystemService(
//            android.content.Context.ACTIVITY_SERVICE);
//      ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo();
//      actvityManager.getMemoryInfo(mInfo);
//      this.available = mInfo.availMem / 1048576L;
//      Log.d("MemoryStats",String.valueOf(this.available));
      // this.max = mInfo.totalMem/1048576L;
      //this.current = this.max - this.available;
   }

}
