package com.kalosis.sensormusicplayer.data;

import android.support.annotation.Nullable;

public enum Shape {
  CIRCLE(1), SQUARE(2);

  Shape(int i) {

  }

  @Nullable
  public static Shape value(int i) {
    switch (i) {
      case 1:
        return CIRCLE;
      case 2:
        return SQUARE;
      default:
        return null;
    }
  }
}
