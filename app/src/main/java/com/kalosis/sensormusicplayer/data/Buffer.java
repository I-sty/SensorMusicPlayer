package com.kalosis.sensormusicplayer.data;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
  public final List<Double> x;

  public final List<Double> y;

  public final List<Double> z;

  public Buffer(List<MyDataPoint> x, List<MyDataPoint> y, List<MyDataPoint> z) {
    this.x = new ArrayList<>();
    for (MyDataPoint dataPoint : x) {
      this.x.add(dataPoint.getY());
    }
    this.y = new ArrayList<>();
    for (MyDataPoint dataPoint : y) {
      this.y.add(dataPoint.getY());
    }
    this.z = new ArrayList<>();
    for (MyDataPoint dataPoint : z) {
      this.z.add(dataPoint.getY());
    }
  }
}
