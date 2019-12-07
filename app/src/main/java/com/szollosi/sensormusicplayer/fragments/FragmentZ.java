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

public class FragmentZ extends BaseFragment {

  private Handler mHandler;

  private LineChart chart;

  private Runnable refreshGraph = new Runnable() {

    @Override
    public void run() {
      synchronized (dataPointsZ) {
        seriesY.setValues(dataPointsZ);
        chart.setData(new LineData(seriesZ));
        chart.invalidate();
        mHandler.postDelayed(this, Constants.DELAY_REFRESH);
      }
    }
  };

  private int page;

  private String title;

  public static FragmentZ newInstance(int page, String title) {
    FragmentZ fragmentFirst = new FragmentZ();
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
    View rootView = inflater.inflate(R.layout.fragment_section_z, container, false);
    chart = rootView.findViewById(R.id.graph_z);
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    mHandler = new Handler(Looper.myLooper());
    mHandler.postDelayed(refreshGraph, Constants.DELAY_REFRESH);
  }

  @Override
  public void onPause() {
    super.onPause();
    mHandler.removeCallbacks(refreshGraph);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    page = getArguments().getInt("someInt", 0);
    title = getArguments().getString("someTitle");
  }
}
