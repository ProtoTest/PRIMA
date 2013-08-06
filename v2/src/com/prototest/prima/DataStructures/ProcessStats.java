package com.prototest.prima.DataStructures;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.prototest.PRIMA;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.R.string;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;

public class ProcessStats {
	public String name = "";
	public String packageName = "";
	public int pid = 0;
	public Drawable icon;
	public int memUsedKb =0;
	public int uid =0;
	public String user = "";
	public int numThreads = 9;
	public int cpuUsage =0;
	public int vssMemoryKb=0;
	public int rssMemoryKb=0;
	
	public ProcessStats()
	{
		
		
	}
	public String getAppName(String packageName)
	{
		

		PackageManager pm = PRIMA.getAppContext().getPackageManager();
		ApplicationInfo ai;
		try {
		    ai = pm.getApplicationInfo(packageName, 0);
		} catch (final NameNotFoundException e) {
		    ai = null;
		}
		String applicationName = "";
		if(ai ==null)
		{
			String[] toks = packageName.split("\\.");
			applicationName=toks[toks.length-1];
		}
		else
		{
			applicationName = (String) pm.getApplicationLabel(ai);
		}
		return applicationName;
	}
	
	Drawable getAppIcon(String packageName)
	{
		PackageManager pm = PRIMA.getAppContext().getPackageManager();
		ApplicationInfo ai;
		Drawable icon;
		try {
		    ai = pm.getApplicationInfo(packageName, 0);
		    icon = (Drawable) pm.getApplicationIcon(ai);	
		} catch (final NameNotFoundException e) {
		    ai = null;
		    icon = PRIMA.getAppContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon);
		}
		

		return (resizeImage(icon));
	}


	public int getMemUsedByPid(int pid)
	{
		ActivityManager manager = (ActivityManager)PRIMA.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
		int[] pids = new int[1];
		pids[0] = pid;
		Debug.MemoryInfo[] info = manager.getProcessMemoryInfo(pids);
		return info[0].getTotalPss();
	}	
	
	public BufferedReader getProcessReader() throws IOException, InterruptedException
	{
		
		Process p = Runtime.getRuntime().exec("top -d 1 -n 1");
		p.waitFor();
		return new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		 
	}
	private String getAppName(int pID)
	{
	    String processName = "";
	    ActivityManager am = (ActivityManager)PRIMA.getAppContext().getSystemService(PRIMA.getAppContext().ACTIVITY_SERVICE);
	    List l = am.getRunningAppProcesses();
	    Iterator i = l.iterator();
	    PackageManager pm = PRIMA.getAppContext().getPackageManager();
	    while(i.hasNext()) 
	    {
	          ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
	          try 
	          { 
	              if(info.pid == pID)
	              {
	                  CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
	                  //Log.d("Process", "Id: "+ info.pid +" ProcessName: "+ info.processName +"  Label: "+c.toString());
	                  //processName = c.toString();
	                  processName = info.processName;
	              }
	          }
	          catch(Exception e) 
	          {
	                //Log.d("Process", "Error>> :"+ e.toString());
	          }
	   }
	    return processName;
	}
	
    private Drawable resizeImage(Drawable Icon) {
		if(Icon!=null)
		{
                Bitmap BitmapOrg = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888); 
                Canvas BitmapCanvas = new Canvas(BitmapOrg);
                Icon.setBounds(0, 0, 50, 50);
                Icon.draw(BitmapCanvas); 
                return new BitmapDrawable(BitmapOrg);
		}
		return null;

}
}
