package com.szollosi.sensormusicplayer.controller;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.szollosi.sensormusicplayer.fragments.FragmentXYZ;

import java.util.Collections;
import java.util.List;

public class PeakCalculator extends JobService {

  private static final int PEAK_THRESHOLD = 7;

  private static final String TAG = PeakCalculator.class.getName();

  @Override
  public boolean onStartJob(JobParameters params) {
    Log.i(TAG, "[onStartJob]");
    List<Entry> list = FragmentXYZ.getPeakWindow();
    if (list.isEmpty()) {
      jobFinished(params, true);
      return true;
    }
    Collections.sort(list, (o1, o2) -> {
      if (Math.abs(o1.getY()) > Math.abs(o2.getY())) {
        return -1;
      } else if (Math.abs(o1.getY()) > Math.abs(o2.getY())) {
        return 1;
      } else {
        return 0;
      }
    });
    Entry peakPoint = list.get(0);
    Log.e(TAG, "[onStartJob] \nstart: " + list.get(0) + "\nend: " + list.get(list.size() - 1) + "\npeak: " + peakPoint);

    //check if the peak is higher then a threshold
    double peak = peakPoint.getY();
    if (peak >= PEAK_THRESHOLD) {
      Log.e(TAG, "[onStartJob] Peak found: " + peakPoint);
    }

    jobFinished(params, true);
    return true;
  }

  @Override
  public boolean onStopJob(JobParameters params) {
    Log.i(TAG, "[onStopJob]");
    return true;
  }

}
