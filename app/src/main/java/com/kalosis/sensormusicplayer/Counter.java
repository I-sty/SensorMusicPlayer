package com.kalosis.sensormusicplayer;

public class Counter {
  private long counter;

  Counter() {
    this.counter = 0;
  }

  public long getCounter() {
    return this.counter;
  }

  public synchronized void increment() {
    ++counter;
  }

  public String toString() {
    return "Counter(counter=" + this.getCounter() + ")";
  }
}
