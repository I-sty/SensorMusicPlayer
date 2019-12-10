package com.szollosi.sensormusicplayer.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.szollosi.sensormusicplayer.Constants
import com.szollosi.sensormusicplayer.R

class FragmentX : BaseFragment() {

  private var mHandler: Handler? = null

  private var chart: LineChart? = null

  private val refreshGraph = object : Runnable {

    override fun run() {
      synchronized(dataPointsX) {
        seriesY.values = dataPointsX.toArrayList()
        chart!!.data = LineData(seriesX)
        chart!!.invalidate()
        mHandler!!.postDelayed(this, Constants.DELAY_REFRESH.toLong())
      }
    }
  }

  private var page: Int = 0

  private var title: String? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.fragment_section_x, container, false)
    chart = rootView.findViewById(R.id.graph_x)
    return rootView
  }

  override fun onResume() {
    super.onResume()
    mHandler = Handler(Looper.myLooper()!!)
    mHandler!!.postDelayed(refreshGraph, Constants.DELAY_REFRESH.toLong())
  }

  override fun onPause() {
    super.onPause()
    mHandler!!.removeCallbacks(refreshGraph)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    page = arguments!!.getInt("someInt", 0)
    title = arguments!!.getString("someTitle")
  }

  companion object {

    fun newInstance(page: Int, title: String): FragmentX {
      val fragmentFirst = FragmentX()
      val args = Bundle()
      args.putInt("someInt", page)
      args.putString("someTitle", title)
      fragmentFirst.arguments = args
      return fragmentFirst
    }
  }
}
