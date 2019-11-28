package com.szollosi.sensormusicplayer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.szollosi.sensormusicplayer.R;

import java.util.ArrayList;

public class FragmentY extends GraphFragment {

  private static final String TAG = FragmentY.class.getName();

  @NonNull
  private static final ArrayList<Entry> dataPoints = new ArrayList<>();

  private static LineDataSet series;

  private Handler mHandler;

  private Runnable refreshGraph = new Runnable() {
    @Override
    public void run() {
      synchronized (dataPoints) {
        series = new LineDataSet(dataPoints, "f");
//        graphView.getViewport().setMinX(series.getLowestValueX());
//        graphView.getViewport().setMaxX(series.getHighestValueX());
//        graphView.getViewport().scrollToEnd();
        mHandler.postDelayed(this, DELAY_REFRESH);
      }
    }
  };

  public static void appendData(Entry dataPoint) {
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

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_section_y, container, false);
    LineChart graphView = rootView.findViewById(R.id.graph_y);
    graphView.setData(new LineData(series));
    series.setColor(ContextCompat.getColor(inflater.getContext(), R.color.colorAxeY));
//    graphView.getViewport().setXAxisBoundsManual(true);
//    graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
//    graphView.getGridLabelRenderer().setNumVerticalLabels(5);
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
