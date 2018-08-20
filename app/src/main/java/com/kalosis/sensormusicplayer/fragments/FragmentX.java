package com.kalosis.sensormusicplayer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kalosis.sensormusicplayer.MyDataPoint;
import com.kalosis.sensormusicplayer.R;

import java.util.ArrayList;
import java.util.Iterator;

public class FragmentX extends GraphFragment {

  private static final String TAG = FragmentX.class.getName();

  @NonNull
  private static final ArrayList<MyDataPoint> dataPoints = new ArrayList<>();

  private static final LineGraphSeries<MyDataPoint> series = new LineGraphSeries<>();

  private GraphView graphView;

  private Handler mHandler;

  private final Runnable refreshGraph = new Runnable() {

    @Override
    public void run() {
      synchronized (dataPoints) {
        try {
          MyDataPoint[] list = dataPoints.toArray(new MyDataPoint[dataPoints.size()]);
          series.resetData(list);
          graphView.getViewport().setMinX(series.getLowestValueX());
          graphView.getViewport().setMaxX(series.getHighestValueX());
          graphView.getViewport().scrollToEnd();
        } catch (Exception e) {
          Log.e(TAG, "[refreshGraph] " + e);
        } finally {
          mHandler.postDelayed(this, DELAY_REFRESH);
        }
      }
    }
  };

  /**
   * Appends the specified element to the end of this list and remove the first element if the list
   * exceeded the preset size.
   *
   * @param dataPoint The element to append.
   */
  public static synchronized void appendData(@NonNull MyDataPoint dataPoint) {
    dataPoints.add(dataPoint);
    synchronized (dataPoints) {
      if (dataPoints.size() >= MAX_DATA_POINTS) {
        Iterator<MyDataPoint> iterator = dataPoints.iterator();
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
    graphView.addSeries(series);
    series.setColor(ContextCompat.getColor(inflater.getContext(), R.color.colorAxeX));
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
