package com.kalosis.sensormusicplayer;

import lombok.Getter;

@Getter
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
