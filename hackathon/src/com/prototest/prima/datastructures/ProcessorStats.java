package com.prototest.prima.datastructures;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.util.Log;

import com.prototest.prima.PrimaApp;
import com.prototest.prima.contentprovider.PrimaContentProvider;
import com.prototest.prima.database.CPUStatsTable;

public class ProcessorStats implements SystemStats {

   private static final String TAG = "ProcessorStats";

   public long numProcs=-1;
   public long used=-1;
   public long free=-1;
   public long total=-1;
   public float percentUsed=-1;

   public ProcessorStats() throws IOException {
	   numProcs = Runtime.getRuntime().availableProcessors();
   }

  
   @Override
   public void GetStats(){
	   try {
    	   numProcs=-1;
		   Log.d("ProcessorStats", "GetStats");
	       RandomAccessFile reader = null;
	     
	       reader = new RandomAccessFile("/proc/stat", "r");

	       String load = reader.readLine();
	      
	       String[] toks = new String[10];
	       while(load!=null)
	       {
		       //Log.d("ProcStats",load);
		       toks = GetToksFromLine(load);
		       
		       if(toks[0].equals("cpu"))
		       {
		    	   free = Long.parseLong(toks[4]);
		    	   used = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) +
					       Long.parseLong(toks[3])
					       + Long.parseLong(toks[5]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]);
		    	   total = free + used;
		    	   percentUsed = ((float)used/(float)total)*100;
//		    	   procNum++;
//		    	   long cpuFree = Long.parseLong(toks[4]);
//		    	   long cpuUsed = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) +
//					       Long.parseLong(toks[3])
//					       + Long.parseLong(toks[5]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]);
//		    	   long cpuTotal = cpuFree + cpuUsed;
//		    	   float cpuPercent = ((float)cpuUsed/(float)cpuTotal)*100;
//		    	   
//			       tempFree += cpuFree;
//			       tempUsed += cpuUsed;
//			       tempTotal += cpuTotal;
//			       tempPercent += cpuPercent;
//			       String msg = String.format("CPU %s: %s free. %s used. %s total. %s percent",procNum,cpuFree,cpuUsed, cpuTotal,cpuPercent);
//			       Log.d("ProcessorStats",msg);
		    	   
		       } 
		       if(toks[0].startsWith("cpu")){
		    	   numProcs++;
		       }
		       
		       load = reader.readLine();
	       }
	       reader.close();
//	       numProcs = procNum;
//	       free = tempFree/numProcs;
//	       used = tempUsed/numProcs;
//	       total = tempTotal/numProcs;
//	       percentUsed = tempPercent/numProcs;
	       String msg = String.format("Average: %s CPUs: %s free. %s used. %s total. %s percent",numProcs,free,used,total,percentUsed);
	       Log.d("ProcessorStats",msg);
	      
	} catch (Exception e) {
		Log.d("ProcessorStats","Exception caught getting stats : " + e.getMessage());
	}	
     

   }

   private String[] GetToksFromLine(String load) {
	  String[] toks = load.split(" "); 
       List<String> list = new ArrayList<String>(Arrays.asList(toks));
       list.removeAll(Arrays.asList(" ",""));
       toks = list.toArray(toks);
	return toks;
}


@Override
   public void ProcessStats() {
      Log.d(TAG, "ProcessStats()");

      ContentValues values = new ContentValues();
      values.put(CPUStatsTable.COLUMN_FREE, this.free);
      values.put(CPUStatsTable.COLUMN_USED, this.used);

      PrimaApp.getAppContext().getContentResolver()
            .insert(PrimaContentProvider.CONTENT_URI_CPU, values);

   }

}
