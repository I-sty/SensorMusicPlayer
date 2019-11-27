package com.szollosi.sensormusicplayer;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;

public class MyDataPoint extends Entry implements Comparable<MyDataPoint> {

  MyDataPoint(float x, float y) {
    super(x, y);
  }

  @Override
  public int compareTo(@NonNull MyDataPoint o) {
    return (int) (Math.abs(this.getY()) - Math.abs(o.getY()));
  }
}
