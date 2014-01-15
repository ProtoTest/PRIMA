package com.prototest.prima;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

   static final String TAG = "MainActivity";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d(TAG, "onCreate()");
      setContentView(R.layout.activity_main);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      Log.d(TAG, "onCreateOptionsMenu()");
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

   public void switchToGraph(View view) {
      Log.d(TAG, "switchToGraph()");
      Intent intent = new Intent(MainActivity.this, SimpleXYPlotActivity.class);
      startActivity(intent);
   }

}
