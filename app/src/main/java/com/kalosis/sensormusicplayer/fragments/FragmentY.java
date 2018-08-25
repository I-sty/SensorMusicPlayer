package com.kalosis.sensormusicplayer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kalosis.sensormusicplayer.R;
import com.kalosis.sensormusicplayer.data.MyDataPoint;
import com.kalosis.sensormusicplayer.utility.Utility;

import org.apache.commons.collections.buffer.CircularFifoBuffer;


public class FragmentY extends Fragment {

  private static final String TAG = FragmentY.class.getName();

  @NonNull
  private static final CircularFifoBuffer dataPoints = new CircularFifoBuffer(Utility.CIRCULAR_BUFFER_SIZE);

  private static final LineGraphSeries<MyDataPoint> series = new LineGraphSeries<>();

  private GraphView graphView;

  private Handler mHandler;

  private final Runnable refreshGraph = new Runnable() {
    @Override
    public void run() {
      synchronized (dataPoints) {
        series.resetData((MyDataPoint[]) dataPoints.toArray(new MyDataPoint[dataPoints.size()]));
        graphView.getViewport().setMinX(series.getLowestValueX());
        graphView.getViewport().setMaxX(series.getHighestValueX());
        graphView.getViewport().scrollToEnd();
        mHandler.postDelayed(this, Utility.DELAY_REFRESH);
      }
    }
  };

  public static void appendData(@NonNull MyDataPoint dataPoint) {
    synchronized (dataPoints) {
      dataPoints.add(dataPoint);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_section_y, container, false);
    graphView = rootView.findViewById(R.id.graph_y);
    graphView.addSeries(series);
    series.setColor(ContextCompat.getColor(inflater.getContext(), R.color.colorAxeY));
    graphView.getViewport().setXAxisBoundsManual(true);
    graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
    graphView.getGridLabelRenderer().setNumVerticalLabels(5);
    mHandler = new Handler(Looper.myLooper());
    mHandler.postDelayed(refreshGraph, Utility.DELAY_REFRESH);
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
    mHandler.removeCallbacks(refreshGraph);
    dataPoints.clear();
  }

  @Override
  public void onResume() {
    super.onResume();
    dataPoints.clear();
  }
}
