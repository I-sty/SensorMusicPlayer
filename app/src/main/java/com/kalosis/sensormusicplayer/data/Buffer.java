package com.kalosis.sensormusicplayer.data;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
  public final List<Double> value;

  public Buffer(List<MyDataPoint> value) {
    this.value = new ArrayList<>();
    for (MyDataPoint dataPoint : value) {
      this.value.add(dataPoint.getY());
    }
  }
}
