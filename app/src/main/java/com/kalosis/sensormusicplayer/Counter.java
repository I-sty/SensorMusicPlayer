package com.kalosis.sensormusicplayer;

public class Counter {
  private long counter;

  Counter() {
    this.counter = 0;
  }

  public synchronized long getCounter() {
    return counter;
  }

  public synchronized void increment() {
    ++counter;
  }
}
