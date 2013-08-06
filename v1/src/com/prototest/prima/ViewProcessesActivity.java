package com.prototest.prima;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.prototest.prima.DataStructures.ProcessStats;
import com.prototest.prima.adapters.ProcessAdapter;

public class ViewProcessesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_processes);
		ProcessStats[] stats = new ProcessStats[2];
		stats[0] = new ProcessStats();
		stats[0].pid = 12345;
		stats[1] = new ProcessStats();
		stats[1].pid = 23231;
		
		//= GlobalData.monitor.getProcessStats();
		ProcessAdapter adapter = new ProcessAdapter(this, R.layout.listview_item_row,stats);
		
		ListView listView = (ListView)findViewById(R.id.view_process_info);
		
		View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_processes, menu);
		return true;
	}

}
