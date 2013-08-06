package com.prototest.prima;
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.prototest.prima.DataStructures.ProcessStats;
 
public class ProcessListAdapter extends ArrayAdapter<ProcessStats> {
	private final Context context;
	private final ProcessStats[] values;
 
	public ProcessListAdapter(Context context, ProcessStats[] values) {
		super(context, R.layout.process_list_fragment, values);
		this.context = context;
		this.values = values;
	}
	

 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.process_list_item, parent, false);
		TextView appName = (TextView) rowView.findViewById(R.id.appName);
		TextView numThreads = (TextView) rowView.findViewById(R.id.numThreads);
		ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
		TextView usage = (TextView) rowView.findViewById(R.id.usage);
		TextView memory = (TextView) rowView.findViewById(R.id.memory);
		
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		
		
		if(values[position]!=null)
		{
			
			appName.setText(values[position].name);
			numThreads.setText(String.valueOf(values[position].numThreads));
			usage.setText(String.valueOf((values[position].cpuUsage))+"%");
			memory.setText(String.valueOf(values[position].memUsedKb));
			icon.setImageDrawable(values[position].icon);
		}

			return rowView;
		}
	}

