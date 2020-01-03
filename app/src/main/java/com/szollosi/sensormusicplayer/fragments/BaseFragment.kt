package com.szollosi.sensormusicplayer.fragments

import android.content.Context
import android.util.Log
import androidx.collection.CircularArray
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.szollosi.sensormusicplayer.R

abstract class BaseFragment : Fragment() {
  companion object {

    val dataPointsY: CircularArray<Entry> = CircularArray()

    val dataPointsZ: CircularArray<Entry> = CircularArray()

    val dataPointsX: CircularArray<Entry> = CircularArray()

    private val TAG = BaseFragment::class.java.simpleName

    internal lateinit var seriesX: LineDataSet

    internal lateinit var seriesY: LineDataSet

    internal lateinit var seriesZ: LineDataSet

    private var colorX: Int = 0

    fun init(context: Context) {
      Log.d(TAG, "[init]")
      if (colorX != 0) {
        return
      }
      colorX = ContextCompat.getColor(context, R.color.colorAxeX)
      val colorY = ContextCompat.getColor(context, R.color.colorAxeY)
      val colorZ = ContextCompat.getColor(context, R.color.colorAxeZ)

      val seriesXName = context.getString(R.string.tab_text_x)
      val seriesYName = context.getString(R.string.tab_text_y)
      val seriesZName = context.getString(R.string.tab_text_z)

      seriesX = LineDataSet(emptyList(), seriesXName)
      seriesY = LineDataSet(emptyList(), seriesYName)
      seriesZ = LineDataSet(emptyList(), seriesZName)

      seriesX.color = colorX
      seriesY.color = colorY
      seriesZ.color = colorZ
    }

    @Synchronized
    fun <E> CircularArray<E>.toArrayList(): ArrayList<E> {
      val size = this.size()
      val array = ArrayList<E>()
      (0 until size).mapTo(array) { this[it] }
      return array
    }
  }
}
