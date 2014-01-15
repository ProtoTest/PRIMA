package com.prototest.prima;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import com.prototest.prima.contentprovider.PrimaContentProvider;
import com.prototest.prima.database.BATTStatsTable;
import com.prototest.prima.database.CPUStatsTable;
import com.prototest.prima.database.MEMStatsTable;

public class PrimaApp extends Application implements OnSharedPreferenceChangeListener {

   static final String TAG = "PrimaApp";

   SharedPreferences prefs;

   /* Screen rotation, etc */
   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      // TODO Auto-generated method stub
      super.onConfigurationChanged(newConfig);
   }

   /* Some initialization stuff */
   @Override
   public void onCreate() {
      Boolean db_seeded = false;

      super.onCreate();

      /* Login details, or whatever */
      Log.d(TAG, "onCreate");

      // Prefs stuff
      prefs = PreferenceManager.getDefaultSharedPreferences(this);
      prefs.registerOnSharedPreferenceChangeListener(this);

      db_seeded = prefs.getBoolean("databaseSeeded", false);

      Log.d(TAG, String.format("Database %s seeded", (db_seeded) ? "is" : "is NOT"));

      if (!db_seeded) {
         new Thread(new Runnable() {
            public void run() {
               seedDatabase();
            }
         }).start();

      }
      /*
       * You could get the prefs variable from any other class via:
       * ((PrimaApp)getApplication()).prefs.getInt("blah blah", defaultDELAY);
       */
   }

   @Override
   public void onLowMemory() {
      // TODO Auto-generated method stub
      super.onLowMemory();
   }

   /* Only called on the emulator, not on the real device */
   @Override
   public void onTerminate() {
      // TODO Auto-generated method stub
      super.onTerminate();
   }

   /*
    * When any preferences change, this callback is called: For example, we can set a
    * class instance object reference back to null, like for changing of a server name or
    * username or password
    */
   @Override
   public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
      // someBSObj = null;
      Log.d(TAG, "onSharedPreferenceChanged for key: " + key);

      /* Preferences changed, so update our instance variable */
      this.prefs = prefs;
   }

   private void seedDatabase() {
      Log.d(TAG, "seedDatabase()");
      ContentValues batt_values = new ContentValues();
      ContentValues mem_values = new ContentValues();
      ContentValues cpu_values = new ContentValues();

      int level = 100;
      int scale = 50;
      int temp = 120;
      int voltage = 20;

      int free = 95;
      int used = 5;
      double percent_used = 50.0;

      for (int i = 1; i <= 50; i++) {
         // battery stats
         batt_values.put(BATTStatsTable.COLUMN_LEVEL, (level % i) * Math.random());
         batt_values.put(BATTStatsTable.COLUMN_SCALE, scale % i);
         batt_values.put(BATTStatsTable.COLUMN_TEMP, temp += 1);
         batt_values.put(BATTStatsTable.COLUMN_VOLTAGE, (voltage % i) * Math.random());
         getContentResolver().insert(PrimaContentProvider.CONTENT_URI_BATT, batt_values);

         // cpu status
         if (i % 2 == 0) {
            cpu_values.put(CPUStatsTable.COLUMN_FREE, free -= 1);
            cpu_values.put(CPUStatsTable.COLUMN_PERCENT_USED, percent_used += 1);
            cpu_values.put(CPUStatsTable.COLUMN_USED, used += 1);
         } else {
            cpu_values.put(CPUStatsTable.COLUMN_FREE, free += 1);
            cpu_values.put(CPUStatsTable.COLUMN_PERCENT_USED, percent_used -= 1);
            cpu_values.put(CPUStatsTable.COLUMN_USED, used -= 1);
         }

         getContentResolver().insert(PrimaContentProvider.CONTENT_URI_CPU, cpu_values);

         // mem status
         mem_values.put(MEMStatsTable.COLUMN_AVAILABLE, 7898);
         mem_values.put(MEMStatsTable.COLUMN_CURRENT, 3333);
         mem_values.put(MEMStatsTable.COLUMN_MAX, 8192);
         getContentResolver().insert(PrimaContentProvider.CONTENT_URI_MEM, mem_values);
      }

      prefs.edit().putBoolean("databaseSeeded", true).commit();
      Log.d(TAG, "Database has been successfully seeded!!!");
   }
}
