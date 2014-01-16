package com.prototest.prima.monitor;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.prototest.prima.datastructures.SystemStats;

public class SystemMonitor {
   protected SystemStats stats;
   private ScheduledExecutorService scheduler;
   private ScheduledFuture<?> handle;

   public SystemMonitor(SystemStats stats) {
      this.scheduler = Executors.newScheduledThreadPool(3);
      this.stats = stats;
   }

   protected Runnable monitor = new Runnable() {
      @Override
      public void run() {
         stats.GetStats();
         stats.ProcessStats();
      }
   };

   public void StartMonitoring() {
      handle = scheduler.scheduleAtFixedRate(monitor, 0, 1, SECONDS);
   }

   public void StopMonitoring() {
      handle.cancel(true);
   }
}
