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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.szollosi.sensormusicplayer.MyDataPoint;
import com.szollosi.sensormusicplayer.R;

import java.util.ArrayList;

public class FragmentZ extends GraphFragment {

  private static final String TAG = FragmentZ.class.getName();

  @NonNull
  private static final ArrayList<MyDataPoint> dataPoints = new ArrayList<>();

  private static final LineGraphSeries<MyDataPoint> series = new LineGraphSeries<>();

  private GraphView graphView;

  private Handler mHandler;

  private Runnable refreshGraph = new Runnable() {
    @Override
    public void run() {
      synchronized (dataPoints) {
        series.resetData(dataPoints.toArray(new MyDataPoint[dataPoints.size()]));
        graphView.getViewport().setMinX(series.getLowestValueX());
        graphView.getViewport().setMaxX(series.getHighestValueX());
        graphView.getViewport().scrollToEnd();
        mHandler.postDelayed(this, DELAY_REFRESH);
      }
    }
  };

  public static void appendData(MyDataPoint dataPoint) {
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
    View rootView = inflater.inflate(R.layout.fragment_section_z, container, false);
    graphView = rootView.findViewById(R.id.graph_z);
    graphView.addSeries(series);
    series.setColor(ContextCompat.getColor(inflater.getContext(), R.color.colorAxeZ));
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
