package com.szollosi.sensormusicplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FragmentXYZ extends GraphFragment {

  @NonNull
  private static List<Entry> dataPointsX = new ArrayList<>();

  @NonNull
  private static final List<Entry> dataPointsY = new ArrayList<>();

  @NonNull
  private static final List<Entry> dataPointsZ = new ArrayList<>();

  private static LineDataSet seriesX = new LineDataSet(dataPointsX, "x");

  private static LineDataSet seriesY = new LineDataSet(dataPointsY, "y");

  private static LineDataSet seriesZ = new LineDataSet(dataPointsZ, "z");

  private LineChart chart;

  private Handler mHandler;

  private String seriesXName;

  private String seriesYName;

  private String seriesZName;

  private Runnable refreshGraph = new Runnable() {

    @Override
    public void run() {
      synchronized (dataPointsX) {
        seriesX = new LineDataSet(dataPointsX, seriesXName);
        seriesY = new LineDataSet(dataPointsY, seriesYName);
        seriesZ = new LineDataSet(dataPointsZ, seriesZName);
        chart.invalidate();
//        chart.getViewport().setMinX(seriesX.getLowestValueX());
//        chart.getViewport().setMaxX(seriesX.getHighestValueX());
//        chart.getViewport().scrollToEnd();
        mHandler.postDelayed(this, DELAY_REFRESH);
      }
    }
  };

  public static void appendData(@NonNull Entry dataPointX, @NonNull Entry dataPointY,
      @NonNull Entry dataPointZ) {
//    addItemToList(dataPointsX, dataPointX);
//    addItemToList(dataPointsY, dataPointY);
//    addItemToList(dataPointsZ, dataPointZ);
  }

  private static synchronized void addItemToList(@NonNull final Collection<Entry> list,
      @NonNull Entry item) {
//    list.add(item);
//    if (list.size() >= MAX_DATA_POINTS) {
//      Iterator<Entry> iterator = list.iterator();
//      if (iterator.hasNext()) {
//        iterator.next();
//        iterator.remove();
//      }
//    }
  }

  public static List<Entry> getPeakWindow() {
//    synchronized (dataPointsZ) {
//      final int size = dataPointsZ.size();
//      if (size == 0) {
//        return new ArrayList<>();
//      }
//      return dataPointsZ.subList(0, size);
//    }
    return null;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    Context context = inflater.getContext();
    seriesXName = context.getString(R.string.tab_text_x);
    seriesYName = context.getString(R.string.tab_text_y);
    seriesZName = context.getString(R.string.tab_text_z);

    View rootView = inflater.inflate(R.layout.fragment_section_xyz, container, false);

    dataPointsX = new ArrayList<>();
    for(int i = 0; i < 10; ++i){
      dataPointsX.add(new Entry(i, i*i));
      dataPointsY.add(new Entry(i, i*i*i));
      dataPointsZ.add(new Entry(i, i*2));
    }
    seriesX = new LineDataSet(dataPointsX, seriesXName);
    seriesY = new LineDataSet(dataPointsY, seriesYName);
    seriesZ = new LineDataSet(dataPointsZ, seriesZName);

    chart = rootView.findViewById(R.id.graph_xyz);
    chart.setData(new LineData(seriesX, seriesY, seriesZ));
    seriesX.setColor(ContextCompat.getColor(context, R.color.colorAxeX));
//    chart.setData(new LineData(seriesY));
//    seriesY.setColor(ContextCompat.getColor(context, R.color.colorAxeY));
//    chart.setData(new LineData(seriesZ));
//    seriesZ.setColor(ContextCompat.getColor(context, R.color.colorAxeZ));
//    chart.getViewport().setXAxisBoundsManual(true);
//    chart.getGridLabelRenderer().setNumHorizontalLabels(5);
//    chart.getGridLabelRenderer().setNumVerticalLabels(8);
//    chart.getLegendRenderer().setVisible(true);
//    chart.getLegendRenderer().setAlign(LegendRenderer);
    chart.invalidate();
//    mHandler = new Handler(Looper.myLooper());
//    mHandler.postDelayed(refreshGraph, DELAY_REFRESH);
    return rootView;
  }

//  @Override
//  public void onPause() {
//    super.onPause();
//    mHandler.removeCallbacks(refreshGraph);
//  }
}
