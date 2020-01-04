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

class FragmentXYZ : BaseFragment() {

    private var chart: LineChart? = null

    private var mHandler: Handler? = null

    private val refreshGraph = object : Runnable {

        override fun run() {
            synchronized(dataPointsX) {
                seriesX.values = dataPointsX.toArrayList()
                seriesY.values = dataPointsY.toArrayList()
                seriesZ.values = dataPointsZ.toArrayList()

                chart!!.data = LineData(seriesX, seriesY, seriesZ)
                chart!!.invalidate()
                mHandler!!.postDelayed(this, MyConstants.DELAY_REFRESH.toLong())
            }
        }
    }

    private var page: Int = 0

    private var title: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_section_xyz, container, false)
        chart = rootView.findViewById(R.id.graph_xyz)
        return rootView
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

    override fun onPause() {
        super.onPause()
        mHandler?.removeCallbacks(refreshGraph)
    }

    companion object {

        fun newInstance(page: Int, title: String): FragmentXYZ {
            val fragmentFirst = FragmentXYZ()
            val args = Bundle()
            args.putInt("someInt", page)
            args.putString("someTitle", title)
            fragmentFirst.arguments = args
            return fragmentFirst
        }
    }
}
