package com.prototest.prima;

import android.app.Application;
import android.content.Context;

public class PRIMA extends Application{

	private static Context context;
	
	public void onCreate(){
	    super.onCreate();
	    PRIMA.context = getApplicationContext();
	}
	
	public static Context getAppContext() {
	    return PRIMA.context;
	}
}
