package com.prototest.prima;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class OverviewFragment extends Fragment {

	private Handler handler;
	public OverviewFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.handler = new Handler();
		handler.post(updateUI);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		
	}
	
	private Runnable updateUI = new Runnable() {
		   @Override
		   public void run() {
			   updateOverviewFragment();
			   handler.postDelayed(this, 5000);
		   }
		};
		
	public View updateOverviewFragment()
	{
		View overView = getActivity().getLayoutInflater().inflate(R.layout.overview_fragment, null);
		TextView cpuText = (TextView)overView.findViewById(R.id.overview_cpu_percent);
		int usage = DeviceMonitor.getDeviceMonitor().topQuery.systemUsage;
		cpuText.setText(String.valueOf(usage));
		
		TextView memoryUsage = (TextView)overView.findViewById(R.id.overview_memory_percent);
		int memUsage = (int)(DeviceMonitor.getDeviceMonitor().memory.getPercentage()*100);
		memoryUsage.setText(String.valueOf(memUsage));
		
		TextView memoryKb = (TextView)overView.findViewById(R.id.overview_memory_kbUsed);
		int memKb = (int)DeviceMonitor.getDeviceMonitor().memory.current;
		memoryKb.setText(String.valueOf(memKb));
		
		return overView;
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.overview_fragment, container, false);
		updateOverviewFragment();
		return view;
	}


}
