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
  private static final List<Entry> dataPointsX = new ArrayList<>();

  @NonNull
  private static final List<Entry> dataPointsY = new ArrayList<>();

  @NonNull
  private static final List<Entry> dataPointsZ = new ArrayList<>();

  private static LineDataSet seriesX;

  private static LineDataSet seriesY;

  private static LineDataSet seriesZ;

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
//        graphView.getViewport().setMinX(seriesX.getLowestValueX());
//        graphView.getViewport().setMaxX(seriesX.getHighestValueX());
//        graphView.getViewport().scrollToEnd();
        mHandler.postDelayed(this, DELAY_REFRESH);
      }
    }
  };

  public static void appendData(@NonNull Entry dataPointX, @NonNull Entry dataPointY,
      @NonNull Entry dataPointZ) {
    addItemToList(dataPointsX, dataPointX);
    addItemToList(dataPointsY, dataPointY);
    addItemToList(dataPointsZ, dataPointZ);
  }

  private static synchronized void addItemToList(@NonNull final Collection<Entry> list,
      @NonNull Entry item) {
    list.add(item);
    if (list.size() >= MAX_DATA_POINTS) {
      Iterator<Entry> iterator = list.iterator();
      if (iterator.hasNext()) {
        iterator.next();
        iterator.remove();
      }
    }
  }

  public static List<Entry> getPeakWindow() {
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
    Context context = inflater.getContext();
    seriesXName = context.getString(R.string.tab_text_x);
    seriesYName = context.getString(R.string.tab_text_y);
    seriesZName = context.getString(R.string.tab_text_z);

    View rootView = inflater.inflate(R.layout.fragment_section_xyz, container, false);

    LineChart graphView = rootView.findViewById(R.id.graph_xyz);
    graphView.setData(new LineData(seriesX));
//    seriesX.setColor(ContextCompat.getColor(context, R.color.colorAxeX));
    graphView.setData(new LineData(seriesY));
//    seriesY.setColor(ContextCompat.getColor(context, R.color.colorAxeY));
    graphView.setData(new LineData(seriesZ));
//    seriesZ.setColor(ContextCompat.getColor(context, R.color.colorAxeZ));
//    graphView.getViewport().setXAxisBoundsManual(true);
//    graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
//    graphView.getGridLabelRenderer().setNumVerticalLabels(8);
//    graphView.getLegendRenderer().setVisible(true);
//    graphView.getLegendRenderer().setAlign(LegendRenderer);
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
