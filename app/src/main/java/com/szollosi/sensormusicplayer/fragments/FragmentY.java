package com.szollosi.sensormusicplayer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.szollosi.sensormusicplayer.Constants;
import com.szollosi.sensormusicplayer.R;

public class FragmentY extends BaseFragment {

  private Handler mHandler;

  private LineChart chart;

  private Runnable refreshGraph = new Runnable() {

    @Override
    public void run() {
      synchronized (dataPointsY) {
        seriesY.setValues(dataPointsY);
        chart.setData(new LineData(seriesY));
        chart.invalidate();
        mHandler.postDelayed(this, Constants.DELAY_REFRESH);
      }
    }
  };

  private int page;

  private String title;

  public static FragmentY newInstance(int page, String title) {
    FragmentY fragmentFirst = new FragmentY();
    Bundle args = new Bundle();
    args.putInt("someInt", page);
    args.putString("someTitle", title);
    fragmentFirst.setArguments(args);
    return fragmentFirst;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_section_y, container, false);
    chart = rootView.findViewById(R.id.graph_y);
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
    mHandler.removeCallbacks(refreshGraph);
  }

  @Override
  public void onResume() {
    super.onResume();
    mHandler = new Handler(Looper.myLooper());
    mHandler.postDelayed(refreshGraph, Constants.DELAY_REFRESH);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    page = getArguments().getInt("someInt", 0);
    title = getArguments().getString("someTitle");
  }
}
