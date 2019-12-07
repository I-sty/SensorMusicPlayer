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

public class FragmentXYZ extends BaseFragment {

  private LineChart chart;

  private Handler mHandler;

  private Runnable refreshGraph = new Runnable() {

    @Override
    public void run() {
      synchronized (dataPointsX) {
        seriesX.setValues(dataPointsX);
        seriesY.setValues(dataPointsY);
        seriesZ.setValues(dataPointsZ);

        chart.setData(new LineData(seriesX, seriesY, seriesZ));
        chart.invalidate();
        mHandler.postDelayed(this, Constants.DELAY_REFRESH);
      }
    }
  };

  private int page;

  private String title;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_section_xyz, container, false);
    chart = rootView.findViewById(R.id.graph_xyz);
    return rootView;
  }

  public static FragmentXYZ newInstance(int page, String title) {
    FragmentXYZ fragmentFirst = new FragmentXYZ();
    Bundle args = new Bundle();
    args.putInt("someInt", page);
    args.putString("someTitle", title);
    fragmentFirst.setArguments(args);
    return fragmentFirst;
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

  @Override
  public void onPause() {
    super.onPause();
    mHandler.removeCallbacks(refreshGraph);
  }
}
