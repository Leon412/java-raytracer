package com.safjnest;

public class StopWatch {
    private long startTime;
    private long stopTime;
    private boolean running;

    public StopWatch() {
        startTime = 0;
        stopTime = 0;
        running = false;
    }
    
    public void start() {
      this.startTime = System.nanoTime();
      this.running = true;
    }
    
    public void stop() {
      this.stopTime = System.nanoTime();
      this.running = false;
    }
    
    public long getElapsedTime() {
      long elapsed;
      if (running) {
        elapsed = (System.nanoTime() - startTime);
      } else {
        elapsed = (stopTime - startTime);
      }
      return elapsed;
    }
    
    public long getElapsedTimeMillis() {
      long elapsed;
      if (running) {
        elapsed = ((System.nanoTime() - startTime) / 1000000);
      } else {
        elapsed = ((stopTime - startTime) / 1000000);
      }
      return elapsed;
    }
  }