package com.prototest.prima.DataStructures;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

public class ProcessorStats {
	public long numProcs;
	public long used;
	public long free;
	public float percentUsed;
	
	public ProcessorStats() throws IOException
	{
//        this.numProcs = getNumCores();
//        
//        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
//        String load = "";
//   
//        String[] toks = new String[10];
//        long idle;
//        long cpu;
//
//        
//        for(int i=0;i<=getNumCores()+1;i++)
//        {
//        	load = reader.readLine();
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
//    	reader.close();
//        
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
	
	
}
