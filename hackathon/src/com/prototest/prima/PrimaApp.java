package com.prototest.prima;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
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
   static Context context;

   SharedPreferences prefs;

   public static Context getAppContext() {
      return PrimaApp.context;
   }

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
      PrimaApp.context = getApplicationContext();

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
      super.onLowMemory();
   }

   /* Only called on the emulator, not on the real device */
   @Override
   public void onTerminate() {
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

}
