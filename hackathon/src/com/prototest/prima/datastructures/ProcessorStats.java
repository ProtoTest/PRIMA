package com.prototest.prima.datastructures;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

import android.util.Log;

public class ProcessorStats implements SystemStats {
	public long numProcs;
	public long used;
	public long free;
	public float percentUsed;
	
	public ProcessorStats() throws IOException
	{
        this.numProcs = getNumCores();
	}
	
	public int getNumCores() {
	    //Private Class to display only CPU devices in the directory listing
	    class CpuFilter implements FileFilter {
	        @Override
	        public boolean accept(File pathname) {
	            //Check if filename is "cpu", followed by a single digit number
	            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
	                return true;
	            }
	            return false;
	        }      
	    }

	    try {
	        //Get directory containing CPU info
	        File dir = new File("/sys/devices/system/cpu/");
	        //Filter to only list the devices we care about
	        File[] files = dir.listFiles(new CpuFilter());
	        //Return the number of cores (virtual CPU devices)
	        return files.length;
	    } catch(Exception e) {
	        //Default to return 1 core
	        return 1;
	    }
	}

	@Override
	public void GetStats() {
		Log.d("ProcessorStats","GetStats");
//        RandomAccessFile reader = null;
//		try {
//			reader = new RandomAccessFile("/proc/stat", "r");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        String load = "";
//   
//        String[] toks = new String[10];
//        long idle;
//        long cpu;
//
//        
//        for(int i=0;i<=getNumCores()+1;i++)
//        {
//        	try {
//				load = reader.readLine();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	toks = load.split(" ");
//	    	if(toks[0].contains("cpu"))
//	    	{
//	
//	    		this.free = Long.parseLong(toks[4]);
//	    		this.used = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) + Long.parseLong(toks[3])
//	  	              + Long.parseLong(toks[5]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]);  
//	    		this.percentUsed = ((float)used/(float)used+free)*100;
//	    	}
//        }
//    	try {
//			reader.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    
		
	}
	
	
}
