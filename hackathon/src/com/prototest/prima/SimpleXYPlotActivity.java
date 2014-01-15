package com.prototest.prima;

import java.util.Arrays;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
 
/**
* A straightforward example of using AndroidPlot to plot some data.
*/
public class SimpleXYPlotActivity extends Activity
{
 
    private XYPlot plot;
 
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ContentValues batt_values = new ContentValues();

        //batt_values.get(BATTStatsTable.COLUMN_LEVEL, )
        // fun little snippet that prevents users from taking screenshots
        // on ICS+ devices :-)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                                 WindowManager.LayoutParams.FLAG_SECURE);
 
        setContentView(R.layout.simple_xy_plot_example);
 
        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
 
        // Create a couple arrays of y-values to plot:
        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
        Number[] series2Numbers = {4, 6, 3, 8, 2, 10};
 
        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Series1");                             // Set the display title of the series
 
        // same as above
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
 
        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.layout.line_point_formatter_with_plf1);
 
        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
 
        // same as above:
        LineAndPointFormatter series2Format = new LineAndPointFormatter();
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        series2Format.configure(getApplicationContext(),
                R.layout.line_point_formatter_with_plf2);
        plot.addSeries(series2, series2Format);
 
        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
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
}