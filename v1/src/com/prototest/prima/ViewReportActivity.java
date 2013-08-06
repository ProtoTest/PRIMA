package com.prototest.prima;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

public class ViewReportActivity extends Activity {

	public TextView text;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_report);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	    displayReport();
	}
	
	public void displayReport()
	{
		//String textString = "";
		//text = (TextView) findViewById(R.id.view_report_stats_text);
		//for (int i=0;i<GlobalData.monitor.num_ticks;i++){
		//	textString += String.valueOf(GlobalData.monitor.memoryStats[i].current) +"\n";
		//}
		//text.setText(textString);
		

		GraphViewStyle style1 = new GraphViewStyle(Color.RED, 10);
		GraphViewStyle style2 = new GraphViewStyle(Color.BLUE, 10);
		GraphViewStyle style3 = new GraphViewStyle(Color.GREEN, 10);
		
		GraphViewData[] batteryData = new GraphViewData[GlobalData.monitor.num_ticks]; 
		GraphViewData[] memoryData = new GraphViewData[GlobalData.monitor.num_ticks]; 
		GraphViewData[] cpuData = new GraphViewData[GlobalData.monitor.num_ticks]; 
		
		
		for (int i=0; i<GlobalData.monitor.num_ticks; i++) {  
		   batteryData[i] = new GraphViewData(i*GlobalData.monitor.record_frequency_ms, GlobalData.monitor.batteryStats[i].percent);
		   memoryData[i] = new GraphViewData(i*GlobalData.monitor.record_frequency_ms, GlobalData.monitor.memoryStats[i].getPercentage());
		   cpuData[i] = new GraphViewData(i*GlobalData.monitor.record_frequency_ms, GlobalData.monitor.processorStats[i].percentUsed);
		   
		}
		
		GraphViewSeries batterySeries = new GraphViewSeries("Battery Level", style1, batteryData);  
		GraphViewSeries memorySeries = new GraphViewSeries("Memory Level", style2, memoryData);  
		GraphViewSeries cpuSeries = new GraphViewSeries("CPU Level", style3, cpuData);  
		
		GraphView graphView = new LineGraphView(  
		      this // context  
		      , "Device Usage" // heading  
		);  
		graphView.addSeries(batterySeries);
		graphView.addSeries(memorySeries);
		graphView.addSeries(cpuSeries);
		graphView.setShowLegend(true);
		//graphView.setLegendWidth(200);
		graphView.setLegendAlign(LegendAlign.BOTTOM);
		//graphView.setHorizontalLabels(null);
		//graphView.setVerticalLabels(null);
		LinearLayout layout = (LinearLayout) findViewById(R.id.view_report_layout);  
	
		layout.addView(graphView); 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_report, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
