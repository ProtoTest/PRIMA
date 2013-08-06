package com.prototest.prima.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prototest.prima.R;
import com.prototest.prima.DataStructures.ProcessStats;

public class ProcessAdapter extends ArrayAdapter<ProcessStats>{
	Context context;
	int layoutResourceId;
	ProcessStats processes[] = null;
	
	public ProcessAdapter(Context context, int layoutResourceId, ProcessStats[] processes)
	{
		super(context, layoutResourceId, processes);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.processes = processes;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		ProcessHolder holder = null;
//		if(row==null)
//		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId,parent, false);
//			holder = new ProcessHolder();
//			holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
//			holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
//			
//			row.setTag(holder);
//			
//		}
//		else
//		{
//			holder = (ProcessHolder) row.getTag();
//		}
		
		ProcessStats stats = processes[position];
		//ImageView imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
		TextView txtTitle = (TextView)row.findViewById(R.id.txtTitle);
		txtTitle.setText(stats.pid);
		//imgIcon.setImageResource(R.drawable.ic_launcher);
		//holder.imgIcon.setImageDrawable(stats.icon);
		
		return row;
		
	}

}

class ProcessHolder
{
	ImageView imgIcon;
	TextView txtTitle;
}
