package com.szollosi.sensormusicplayer;

public class Counter {

  private long counter;

  public Counter() {
    this.counter = 0;
  }

  public synchronized long getCounter() {
    return counter;
  }

  public synchronized void increment() {
    ++counter;
  }
}
