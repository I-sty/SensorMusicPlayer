package com.kalosis.sensormusicplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kalosis.sensormusicplayer.R;
import com.kalosis.sensormusicplayer.data.MyDataPoint;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import java.util.Arrays;
import java.util.List;

public class FragmentXYZ extends GraphFragment {

  @NonNull
  private static final CircularFifoBuffer dataPointsX = new CircularFifoBuffer(CIRCULAR_BUFFER_SIZE);

  @NonNull
  private static final CircularFifoBuffer dataPointsY = new CircularFifoBuffer(CIRCULAR_BUFFER_SIZE);

  @NonNull
  private static final CircularFifoBuffer dataPointsZ = new CircularFifoBuffer(CIRCULAR_BUFFER_SIZE);

  private static final LineGraphSeries<MyDataPoint> seriesX = new LineGraphSeries<>();

  private static final LineGraphSeries<MyDataPoint> seriesY = new LineGraphSeries<>();

  private static final LineGraphSeries<MyDataPoint> seriesZ = new LineGraphSeries<>();

  private GraphView graphView;

  private Handler mHandler;

  private final Runnable refreshGraph = new Runnable() {

    @Override
    public void run() {
      synchronized (dataPointsX) {
        seriesX.resetData(toArray(dataPointsX));
      }
      synchronized (dataPointsY) {
        seriesY.resetData(toArray(dataPointsY));
      }
      synchronized (dataPointsY) {
        seriesZ.resetData(toArray(dataPointsZ));
      }
      graphView.getViewport().setMinX(seriesZ.getLowestValueX());
      graphView.getViewport().setMaxX(seriesZ.getHighestValueX());
      graphView.getViewport().scrollToEnd();
      mHandler.postDelayed(this, DELAY_REFRESH);
    }
  };

  public static void appendData(@NonNull MyDataPoint dataPointX, @NonNull MyDataPoint dataPointY,
      @NonNull MyDataPoint dataPointZ) {
    dataPointsX.add(dataPointX);
    dataPointsY.add(dataPointY);
    dataPointsZ.add(dataPointZ);
  }

  /**
   * Return the peak window of the Z axis
   *
   * @return The sublist of the Z axis.
   */
  @NonNull
  public static List<MyDataPoint> getPeakWindow() {
    synchronized (dataPointsZ) {
      return Arrays.asList(toArray(dataPointsZ));
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_section_xyz, container, false);
    graphView = rootView.findViewById(R.id.graph_xyz);
    graphView.addSeries(seriesX);
    Context context = inflater.getContext();
    seriesX.setColor(ContextCompat.getColor(context, R.color.colorAxeX));
    seriesX.setTitle(context.getString(R.string.tab_text_x));
    graphView.addSeries(seriesY);
    seriesY.setColor(ContextCompat.getColor(context, R.color.colorAxeY));
    seriesY.setTitle(context.getString(R.string.tab_text_y));
    graphView.addSeries(seriesZ);
    seriesZ.setColor(ContextCompat.getColor(context, R.color.colorAxeZ));
    seriesZ.setTitle(context.getString(R.string.tab_text_z));
    graphView.getViewport().setXAxisBoundsManual(true);
    graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
    graphView.getGridLabelRenderer().setNumVerticalLabels(8);
    graphView.getLegendRenderer().setVisible(true);
    graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    mHandler = new Handler(Looper.myLooper());
    mHandler.postDelayed(refreshGraph, DELAY_REFRESH);
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
    mHandler.removeCallbacks(refreshGraph);
  }

  @NonNull
  private synchronized static MyDataPoint[] toArray(CircularFifoBuffer fifoBuffer) {
    if (fifoBuffer == null || fifoBuffer.isEmpty()) {
      return new MyDataPoint[0];
    }
    return (MyDataPoint[]) fifoBuffer.toArray(new MyDataPoint[fifoBuffer.size()]);
  }
}
