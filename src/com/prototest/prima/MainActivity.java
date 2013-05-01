package com.prototest.prima;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.prototest.prima.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
public final static String EXTRA_MESSAGE = "com.prototest.prima.MESSAGE";
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		

		
	}
	
	@SuppressLint("NewApi")
	private String ReadCPUinfo()
	{
	ProcessBuilder cmd;
	StringBuffer strMemory = new StringBuffer();
	//final ActivityManager activityManager =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	ActivityManager actvityManager = (ActivityManager) this.getSystemService(  
	ACTIVITY_SERVICE );
	ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo ();
	actvityManager.getMemoryInfo( mInfo );
	strMemory.append("Available Memory : ");
	strMemory.append(mInfo.availMem/1048576L + " mb");
	strMemory.append("\n");
	strMemory.append("Total Memory : ");
	strMemory.append(mInfo.totalMem/1048576L + " mb");
	strMemory.append("\n");
	strMemory.append("\n");
	String result=strMemory.toString();
	try{
	String[] args = {"/system/bin/cat", "/proc/meminfo"};
	cmd = new ProcessBuilder(args);
	Process process = cmd.start();
	InputStream in = process.getInputStream();
	byte[] re = new byte[1024];
	while(in.read(re) != -1){
	System.out.println("itthhe   ====  ---   >>>>    "+new String(re));
	result = result + new String(re);
	}
	in.close();
	} catch(IOException ex){
	ex.printStackTrace();
	}
	return result;
	}

	
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void startRecording(View view) {
		Intent intent = new Intent(this, RecordingActivity.class);
		//EditText editText = (EditText) findViewById(R.id.max_duration_time);
		//String message = editText.getText().toString();
		//intent.putExtra(EXTRA_MESSAGE, message);
		
		startActivity(intent);
	}
	
	public void viewStats(View view) {
	Intent intent = new Intent(this,ViewStatsActivity.class);
	startActivity(intent);
	}
}
