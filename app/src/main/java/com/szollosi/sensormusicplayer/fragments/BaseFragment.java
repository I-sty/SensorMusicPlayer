package com.szollosi.sensormusicplayer.fragments;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.szollosi.sensormusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment extends Fragment {

  @NonNull
  public static final List<Entry> dataPointsY = new ArrayList<>();

  @NonNull
  public static final List<Entry> dataPointsZ = new ArrayList<>();

  @NonNull
  public static final List<Entry> dataPointsX = new ArrayList<>();

  private static final String TAG = BaseFragment.class.getSimpleName();

  static LineDataSet seriesX;

  static LineDataSet seriesY;

  static LineDataSet seriesZ;

  private static int colorX;

  public static void init(@NonNull Context context) {
    Log.d(TAG, "[init]");
    if (colorX != 0) {
      return;
    }
    colorX = ContextCompat.getColor(context, R.color.colorAxeX);
    int colorY = ContextCompat.getColor(context, R.color.colorAxeY);
    int colorZ = ContextCompat.getColor(context, R.color.colorAxeZ);

    String seriesXName = context.getString(R.string.tab_text_x);
    String seriesYName = context.getString(R.string.tab_text_y);
    String seriesZName = context.getString(R.string.tab_text_z);

    seriesX = new LineDataSet(dataPointsX, seriesXName);
    seriesY = new LineDataSet(dataPointsY, seriesYName);
    seriesZ = new LineDataSet(dataPointsZ, seriesZName);

    seriesX.setColor(colorX);
    seriesY.setColor(colorY);
    seriesZ.setColor(colorZ);
  }
}
