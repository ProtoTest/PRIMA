package com.prototest.Queries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.prototest.prima.DataStructures.ProcessStats;

public class TopQuery {
	public List<ProcessStats> processes;
	private BufferedReader reader;
	public int userUsage = 0;
	public int systemUsage = 0;
	public int iowUsage = 0;
	public int irqUsage = 0;
	public int userThreads = 0;
	public int niceThreads = 0;
	public int systemThreads = 0;
	public int idleThreads = 0;
	public int iowThreads = 0;
	public int irqThreads = 0;
	public int sirqThreads = 0;
	public int totalThreads = 0;
	public int numProcesses = 0;
	
	public TopQuery() throws IOException, InterruptedException
	{
		processes = new ArrayList<ProcessStats>();
		reader=getProcessReader();
		readStatsFromTop();
	}
	public ProcessStats getProcessStatsByPid(int pid)
	{
		for (ProcessStats stats : processes) {
			if(stats.pid==pid)
				return stats;
		}
		return new ProcessStats();
		
	}
	
	
	private BufferedReader getProcessReader() throws IOException, InterruptedException
	{
		Process p = Runtime.getRuntime().exec("top -d 0 -n 1");
		p.waitFor();
		return new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		 
	}
	private String[] getToksFromLine(String line)
	{
		String newLine = line.replaceAll("^[ \t]+|[ \t]+$", "");
		newLine = newLine.replace("%", "");
		newLine = newLine.replace(",", "");
		return newLine.split(" +");
	}
	public ProcessStats getProcessByPid(int pid)
	{
		for (ProcessStats stat : processes) {
			if(pid==stat.pid)
				return stat;
		}
		return null;
	}
	public ProcessStats[] getProcesses()
	{
		return processes.toArray(new ProcessStats[processes.size()]);
	}
	private void readStatsFromTop() throws IOException, InterruptedException
	{
		String line = reader.readLine();
		String[] toks;
		while(!line.contains("User"))
		{  
			line=reader.readLine();
		}
		
		toks = getToksFromLine(line);
		userUsage = Integer.parseInt(toks[1]);
		systemUsage = Integer.parseInt(toks[3]);
		iowUsage = Integer.parseInt(toks[5]);
		irqUsage = Integer.parseInt(toks[7]);
		
		line=reader.readLine();
		toks = getToksFromLine(line);
		
		userThreads = Integer.parseInt(toks[1]);
		niceThreads = Integer.parseInt(toks[4]);
		systemThreads = Integer.parseInt(toks[7]);
		idleThreads = Integer.parseInt(toks[10]);
		iowThreads = Integer.parseInt(toks[13]);
		irqThreads = Integer.parseInt(toks[16]);
		sirqThreads = Integer.parseInt(toks[19]);
		totalThreads = Integer.parseInt(toks[21]);
		
		line=reader.readLine(); //whitespace
		line=reader.readLine(); //Column headers
		line=reader.readLine(); //first process
		while(line!=null)
		{
			toks = getToksFromLine(line);
			ProcessStats stat = new ProcessStats();
			
			stat.pid=Integer.parseInt(toks[0]);
			stat.cpuUsage = Integer.parseInt(toks[2].replace("%",""));
			stat.numThreads = Integer.parseInt(toks[4]);
			stat.vssMemoryKb = Integer.parseInt(toks[5].replace("K", ""));
			stat.rssMemoryKb = Integer.parseInt(toks[6].replace("K",""));
			
			//some rows contain a column for PCY.
			if(toks.length==10)
			{
				stat.user = toks[8];
				stat.name = toks[9];
			}
			else
			{
				stat.user=toks[7];
				stat.name=toks[8];
			}
			
			processes.add(stat);
			numProcesses ++;
			line = reader.readLine(); //get next process
		}
		
	}
}

