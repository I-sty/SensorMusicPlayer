package com.szollosi.sensormusicplayer.fragments;

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
import java.util.Iterator;

public class FragmentX extends GraphFragment {

  @NonNull
  private static final ArrayList<Entry> dataPoints = new ArrayList<>();

  private static LineDataSet series;

  private LineChart graphView;

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

  /**
   * Appends the specified element to the end of this list and remove the first element if the list exceeded the preset
   * size.
   *
   * @param dataPoint
   *     *     The element to append.
   */
  public static synchronized void appendData(@NonNull Entry dataPoint) {
    dataPoints.add(dataPoint);
    synchronized (dataPoints) {
      if (dataPoints.size() >= MAX_DATA_POINTS) {
        Iterator<Entry> iterator = dataPoints.iterator();
        iterator.next();
        iterator.remove();
      }
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_section_x, container, false);
    graphView = rootView.findViewById(R.id.graph_x);
    graphView.setData(new LineData(series));
    series.setColor(ContextCompat.getColor(inflater.getContext(), R.color.colorAxeX));
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
