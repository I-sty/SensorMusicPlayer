package com.szollosi.sensormusicplayer.fragments;

import android.content.Context;
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
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.LegendRenderer;
import com.szollosi.sensormusicplayer.MyDataPoint;
import com.szollosi.sensormusicplayer.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentXYZ extends GraphFragment {

  @NonNull
  private static final ArrayList<MyDataPoint> dataPointsX = new ArrayList<>();

  @NonNull
  private static final ArrayList<MyDataPoint> dataPointsY = new ArrayList<>();

  @NonNull
  private static final ArrayList<MyDataPoint> dataPointsZ = new ArrayList<>();

  private static final LineDataSet seriesX = null;

  private static final LineDataSet seriesY = null;

  private static final LineDataSet seriesZ = null;

  private LineChart graphView;

  private Handler mHandler;

  private Runnable refreshGraph = new Runnable() {

    @Override
    public void run() {
      synchronized (dataPointsX) {
        seriesX = new LineDataSet(dataPointsX.toArray(new MyDataPoint[dataPointsX.size()]), "series X");
        seriesY.resetData(dataPointsY.toArray(new MyDataPoint[dataPointsY.size()]));
        seriesZ.resetData(dataPointsZ.toArray(new MyDataPoint[dataPointsZ.size()]));
        graphView.getViewport().setMinX(seriesX.getLowestValueX());
        graphView.getViewport().setMaxX(seriesX.getHighestValueX());
        graphView.getViewport().scrollToEnd();
        mHandler.postDelayed(this, DELAY_REFRESH);
      }
    }
  };

  public static void appendData(@NonNull MyDataPoint dataPointX, @NonNull MyDataPoint dataPointY,
      @NonNull MyDataPoint dataPointZ) {
    addItemToList(dataPointsX, dataPointX);
    addItemToList(dataPointsY, dataPointY);
    addItemToList(dataPointsZ, dataPointZ);
  }

  private static synchronized void addItemToList(@NonNull final List<MyDataPoint> list,
      @NonNull MyDataPoint item) {
    list.add(item);
    if (list.size() >= MAX_DATA_POINTS) {
      Iterator<MyDataPoint> iterator = list.iterator();
      if (iterator.hasNext()) {
        iterator.next();
        iterator.remove();
      }
    }
  }

  public static List<MyDataPoint> getPeakWindow() {
    synchronized (dataPointsZ) {
      final int size = dataPointsZ.size();
      final byte LIST_SIZE = 50;
      if (size == 0) {
        return new ArrayList<>();
      }
      if (size < LIST_SIZE) {
        return dataPointsZ.subList(0, size);
      }
      return dataPointsZ.subList(size - LIST_SIZE, size);
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
}
