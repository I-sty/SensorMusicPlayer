package com.szollosi.sensormusicplayer

import com.github.mikephil.charting.data.Entry
import kotlin.math.abs

class MyDataPoint internal constructor(x: Float, y: Float) : Entry(x, y), Comparable<MyDataPoint> {

    override fun compareTo(other: MyDataPoint): Int {
        return (abs(this.y) - abs(other.y)).toInt()
  }
}
