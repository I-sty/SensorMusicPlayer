package com.kalosis.sensormusicplayer;

@lombok.Data
public class Counter {
  private long counter;

  Counter() {
    this.counter = 0;
  }

  public synchronized void increment() {
    ++counter;
  }
}
