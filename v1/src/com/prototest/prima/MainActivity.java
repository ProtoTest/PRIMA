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
		GlobalData.monitor = new DeviceMonitor(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void startRecording(View view) {
		Intent intent = new Intent(this, RecordingActivity.class);
		startActivity(intent);
	}

	public void viewStats(View view) {
	Intent intent = new Intent(this,ViewProcessesActivity.class);
	startActivity(intent);
	}
}