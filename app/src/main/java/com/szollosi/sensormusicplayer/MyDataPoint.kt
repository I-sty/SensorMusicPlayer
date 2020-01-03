package com.szollosi.sensormusicplayer

import com.github.mikephil.charting.data.Entry

class MyDataPoint internal constructor(x: Float, y: Float) : Entry(x, y), Comparable<MyDataPoint> {

  override fun compareTo(o: MyDataPoint): Int {
    return (Math.abs(this.y) - Math.abs(o.y)).toInt()
  }
}
