package com.szollosi.sensormusicplayer.controller

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.szollosi.sensormusicplayer.Constants
import com.szollosi.sensormusicplayer.fragments.BaseFragment
import com.szollosi.sensormusicplayer.fragments.FragmentUtil

class PeakCalculator : JobService() {

  override fun onStartJob(params: JobParameters): Boolean {
    Log.i(TAG, "[onStartJob]")
    val list = FragmentUtil.getPeakWindow(BaseFragment.dataPointsZ)
    if (list.isEmpty()) {
      jobFinished(params, true)
      return true
    }
//    Collections.sort(list) { o1, o2 ->
//      if (Math.abs(o1.y) > Math.abs(o2.y)) {
//        return@Collections.sort - 1
//      } else if (Math.abs(o1.y) > Math.abs(o2.y)) {
//        return@Collections.sort 1
//      } else {
//        return@Collections.sort 0
//      }
//    }
    val peakPoint = list[0]
    Log.i(TAG, "[onStartJob] \nstart: " + list[0] + "\nend: " + list[list.size - 1] + "\npeak: " + peakPoint)

    //check if the peak is higher then a threshold
    val peak = peakPoint.y.toDouble()
    if (peak >= Constants.PEAK_THRESHOLD) {
      Log.i(TAG, "[onStartJob] Peak found: $peakPoint")
    }

    jobFinished(params, true)
    return true
  }

  override fun onStopJob(params: JobParameters): Boolean {
    Log.i(TAG, "[onStopJob]")
    return true
  }

  companion object {


    private val TAG = PeakCalculator::class.java.name
  }

}
