package com.prototest.prima;

import java.util.Arrays;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.prototest.prima.contentprovider.PrimaContentProvider;
import com.prototest.prima.database.BATTStatsTable;
import com.prototest.prima.database.CPUStatsTable;
import com.prototest.prima.database.MEMStatsTable;

/**
 * A straightforward example of using AndroidPlot to plot some data.
 */
public class SimpleXYPlotActivity extends Activity {

   private XYPlot plot;

   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
   @Override
   public void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
         getActionBar().setDisplayHomeAsUpEnabled(true);
      }

      Double[] memStats = getMemStats();
      Double[] battStats = getBattStats();
      Double[] cpuStats = getCPUStats();

      // fun little snippet that prevents users from taking screenshots
      // on ICS+ :-)
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);

      setContentView(R.layout.simple_xy_plot_example);

      // initialize our XYPlot reference:
      plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
      plot.getBackgroundPaint().setColor(Color.WHITE);
      plot.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
      plot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
      plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
      plot.getLegendWidget().getTextPaint().setColor(Color.GRAY);
      plot.setDomainLabel("Seconds");
      plot.setRangeLabel("Percent");

      // Turn the above arrays into XYSeries':
      XYSeries series1 = new SimpleXYSeries(Arrays.asList(memStats),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Memory");
      XYSeries series2 = new SimpleXYSeries(Arrays.asList(cpuStats),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "CPU");
      XYSeries series3 = new SimpleXYSeries(Arrays.asList(battStats),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Battery");
      // Create a formatter to use for drawing a series using LineAndPointRenderer
      // and configure it from xml:
      LineAndPointFormatter series1Format = new LineAndPointFormatter();
      series1Format.setPointLabelFormatter(new PointLabelFormatter());
      series1Format.configure(getApplicationContext(), R.layout.line_point_formatter_with_plf1);

      LineAndPointFormatter series2Format = new LineAndPointFormatter();
      series2Format.setPointLabelFormatter(new PointLabelFormatter());
      series2Format.configure(getApplicationContext(), R.layout.line_point_formatter_with_plf2);

      LineAndPointFormatter series3Format = new LineAndPointFormatter();
      series3Format.setPointLabelFormatter(new PointLabelFormatter());
      series3Format.configure(getApplicationContext(), R.layout.line_point_formatter_with_plf3);
      // add a new series' to the xyplot:
      plot.addSeries(series1, series1Format);
      plot.addSeries(series2, series2Format);
      plot.addSeries(series3, series3Format);

      // reduce the number of range labels
      plot.setRangeBoundaries(0.0, 100.0, BoundaryMode.FIXED);
      plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 10);
      plot.getGraphWidget().setDomainLabelOrientation(-45);

   }

   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {

      case android.R.id.home:
         // app icon in action bar clicked; go home
         Intent intentHome = new Intent(this, MainActivity.class);
         intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intentHome);
         return true;

      default:
         return super.onOptionsItemSelected(item);
      }
   }

   private Double[] getBattStats() {
      String[] projection = new String[] { BATTStatsTable.COLUMN_ID, BATTStatsTable.COLUMN_LEVEL };
      Cursor cursor = getContentResolver().query(PrimaContentProvider.CONTENT_URI_BATT, projection,
            null, null, null);
      Double[] results = new Double[cursor.getCount()];
      cursor.moveToFirst();
      for (int i = 0; i < cursor.getCount(); i++) {
         double curr = cursor.getInt(1);
         results[i] = curr;
         cursor.moveToNext();
      }
      return results;
   }

   private Double[] getMemStats() {
      String[] projection = new String[] { MEMStatsTable.COLUMN_ID, MEMStatsTable.COLUMN_CURRENT,
            MEMStatsTable.COLUMN_AVAILABLE };
      Cursor cursor = getContentResolver().query(PrimaContentProvider.CONTENT_URI_MEM, projection,
            null, null, null);
      Double[] results = new Double[cursor.getCount()];
      cursor.moveToFirst();
      for (int i = 0; i < cursor.getCount(); i++) {
         double curr = cursor.getInt(1);
         double avail = cursor.getInt(2);
         Log.d("SCREW YOU", "" + (curr / (curr + avail)) * 100);
         results[i] = (curr / (curr + avail)) * 100;
         cursor.moveToNext();
      }
      return results;

   }

   private Double[] getCPUStats() {
      String[] projection = new String[] { CPUStatsTable.COLUMN_ID, CPUStatsTable.COLUMN_USED,
            CPUStatsTable.COLUMN_FREE };
      Cursor cursor = getContentResolver().query(PrimaContentProvider.CONTENT_URI_CPU, projection,
            null, null, null);
      Double[] results = new Double[cursor.getCount()];
      cursor.moveToFirst();
      for (int i = 0; i < cursor.getCount(); i++) {
         double used = cursor.getInt(1);
         double free = cursor.getInt(2);
         Log.d("SCREW YOU", "" + (used / (used + free)) * 100);
         results[i] = (used / (used + free)) * 100;
         cursor.moveToNext();
      }
      return results;
   }
}