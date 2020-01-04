package com.szollosi.sensormusicplayer.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.szollosi.sensormusicplayer.MyConstants
import com.szollosi.sensormusicplayer.R

class FragmentY : BaseFragment() {

    private var mHandler: Handler? = null

    private var chart: LineChart? = null

    private val refreshGraph = object : Runnable {

        override fun run() {
            synchronized(dataPointsY) {
                seriesY.values = dataPointsY.toArrayList()
                chart?.apply {
                    data = LineData(seriesY)
                    invalidate()
                }
                mHandler?.postDelayed(this, MyConstants.DELAY_REFRESH.toLong())
            }
        }
    }

    private var page: Int = 0

    private var title: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_section_y, container, false)
        chart = rootView.findViewById(R.id.graph_y)
        return rootView
    }

    override fun onPause() {
        super.onPause()
        mHandler?.removeCallbacks(refreshGraph)
    }

    override fun onResume() {
        super.onResume()
        Looper.myLooper()?.let {
            mHandler = Handler(it).apply {
                postDelayed(refreshGraph, MyConstants.DELAY_REFRESH.toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getInt("someInt", 0)
            title = it.getString("someTitle")
        }
    }

    companion object {

        fun newInstance(page: Int, title: String): FragmentY {
            val fragmentFirst = FragmentY()
            fragmentFirst.arguments = Bundle().apply {
                putInt("someInt", page)
                putString("someTitle", title)
            }
            return fragmentFirst
        }
    }
}
