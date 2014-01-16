package com.prototest.prima;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.prototest.prima.contentprovider.PrimaContentProvider;
import com.prototest.prima.datastructures.BatteryStats;
import com.prototest.prima.datastructures.MemoryStats;
import com.prototest.prima.datastructures.ProcessorStats;
import com.prototest.prima.monitor.SystemMonitor;

public class MainActivity extends Activity {

   static final String TAG = "MainActivity";
   private static SystemMonitor batteryMonitor;
   private static SystemMonitor memoryMonitor;
   private static SystemMonitor processorMonitor;
   private static boolean recording = false;
   private TextView recordingLabel;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d(TAG, "onCreate()");
      setContentView(R.layout.activity_main);
      batteryMonitor = new SystemMonitor(new BatteryStats());
      memoryMonitor = new SystemMonitor(new MemoryStats());
      try {
         processorMonitor = new SystemMonitor(new ProcessorStats());
      } catch (IOException e) {
      }

      recordingLabel = (TextView) findViewById(R.id.recording_status);
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

   public void toggleRecording(View view) throws IOException {
      if (!recording) {
         PrimaContentProvider.dropAllTables();
         PrimaContentProvider.createAllTables();

         startRecording();
         recording = true;
      } else {
         stopRecording(view);
         recording = false;
      }
   }

   private void startRecording() {
      Log.d(TAG, "startRecording");
      batteryMonitor.StartMonitoring();
      memoryMonitor.StartMonitoring();
      processorMonitor.StartMonitoring();
      recordingLabel.setText(R.string.recording);
   }

   private void stopRecording(View view) {
      Log.d(TAG, "stopRecording");
      batteryMonitor.StopMonitoring();
      memoryMonitor.StopMonitoring();
      processorMonitor.StopMonitoring();
      recordingLabel.setText(R.string.not_recording);
      switchToGraph(view);
   }
}
