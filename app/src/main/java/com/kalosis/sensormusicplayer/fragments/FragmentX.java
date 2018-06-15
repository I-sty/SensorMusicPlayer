package com.kalosis.sensormusicplayer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kalosis.sensormusicplayer.R;

import java.util.ArrayList;

public class FragmentX extends GraphFragment {

  private static final String TAG = FragmentX.class.getName();

  @NonNull
  private static final ArrayList<DataPoint> dataPoints = new ArrayList<>();

  private static final LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

  private GraphView graphView;

  private Handler mHandler;

  private Runnable refreshGraph = new Runnable() {
    @Override
    public void run() {
      synchronized (dataPoints) {
        try {
          series.resetData(dataPoints.toArray(new DataPoint[dataPoints.size()]));
          graphView.getViewport().setMinX(series.getLowestValueX());
          graphView.getViewport().setMaxX(series.getHighestValueX());
          graphView.getViewport().scrollToEnd();
        } catch (Exception e) {
          Log.e(TAG, "[run] exception: " + e);
        }
        mHandler.postDelayed(this, DELAY_REFRESH);
      }
    }
  };

  public static void appendData(DataPoint dataPoint) {
    if (dataPoint == null) {
      return;
    }
    AsyncTask.execute(() -> {
      synchronized (dataPoints) {
        if (dataPoints.size() >= MAX_DATA_POINTS) {
          dataPoints.remove(0);
        }
        dataPoints.add(dataPoint);
      }
    });
  }

  public double getLastX() {
    return series.getHighestValueX();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_section_x, container, false);
    graphView = rootView.findViewById(R.id.graph_x);
    graphView.addSeries(series);
    graphView.getViewport().setXAxisBoundsManual(true);
    graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
    graphView.getGridLabelRenderer().setNumVerticalLabels(5);
    mHandler = new Handler(Looper.myLooper());
    mHandler.postDelayed(refreshGraph, DELAY_REFRESH);
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
    mHandler.removeCallbacks(refreshGraph);
  }
}
