package com.prototest.prima;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prototest.prima.DataStructures.ProcessStats;


public class ProcessListFragment extends ListFragment {

	private ProcessStats[] processes;
	private View mHeaderView;
	private ProcessListAdapter listAdapter; 
	private Handler handler;
	
	public ProcessListFragment() 
	{
		
	}
	
	
	private Runnable updateUI = new Runnable() {
		   @Override
		   public void run() {
			   listAdapter = new ProcessListAdapter(getActivity(), DeviceMonitor.getDeviceMonitor().processes);
			   setListAdapter(listAdapter);
			   listAdapter.notifyDataSetChanged();
  			   handler.postDelayed(this, 10000);
		   }
		};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		processes = new ProcessStats[50];
		handler = new Handler();
		handler.post(updateUI);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		DeviceMonitor.getDeviceMonitor().Kill();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		setListAdapter(null);
		   mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.process_list_header, null);
		    getListView().addHeaderView(mHeaderView);
		    listAdapter = new ProcessListAdapter(getActivity(), DeviceMonitor.getDeviceMonitor().processes);
			setListAdapter(listAdapter);
			listAdapter.notifyDataSetChanged();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//listAdapter = new ProcessListAdapter(getActivity(), DeviceMonitor.getDeviceMonitor().processes);
		//setListAdapter(listAdapter);
		//listAdapter.notifyDataSetChanged();
		mHeaderView = inflater.inflate(R.layout.process_list_header, container,false);
		
		return inflater.inflate(R.layout.process_list_fragment, container, false);
	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		
	}
}