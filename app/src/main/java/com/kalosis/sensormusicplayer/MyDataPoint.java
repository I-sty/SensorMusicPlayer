package com.kalosis.sensormusicplayer;

import android.support.annotation.NonNull;

import com.jjoe64.graphview.series.DataPoint;

public class MyDataPoint extends DataPoint implements Comparable<MyDataPoint> {
  MyDataPoint(double x, double y) {
    super(x, y);
  }

  @Override
  public int compareTo(@NonNull MyDataPoint o) {
    return (int) (Math.abs(this.getY()) - Math.abs(o.getY()));
  }
}
