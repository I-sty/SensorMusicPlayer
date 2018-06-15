package com.kalosis.sensormusicplayer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.series.DataPoint;
import com.kalosis.sensormusicplayer.fragments.FragmentX;
import com.kalosis.sensormusicplayer.fragments.FragmentY;
import com.kalosis.sensormusicplayer.fragments.FragmentZ;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
  /**
   * The fragment argument representing the section number for this
   * fragment.
   */
  private static final String ARG_SECTION_NUMBER = "section_number";

  private static final byte SECTION_X = 1;

  private static final byte SECTION_Y = 2;

  private static final byte SECTION_Z = 3;

  @Nullable
  private static SensorEventListener accelerometerEventListener;

  @Nullable
  private static FragmentX fragmentX;

  @Nullable
  private static FragmentY fragmentY;

  @Nullable
  private static FragmentZ fragmentZ;

  private double counter;

  private float[] gravity = new float[3];

  public PlaceholderFragment() {
  }

  @Nullable
  public static SensorEventListener getAccelerometerEventListener() {
    return accelerometerEventListener;
  }

  /**
   * Returns a new instance of this fragment for the given section
   * number.
   */
  public static PlaceholderFragment newInstance(int sectionNumber) {
    PlaceholderFragment fragment = new PlaceholderFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final int sectionNumber = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : SECTION_X;
    switch (sectionNumber) {
      case SECTION_Y: {
        fragmentY = new FragmentY();
        return fragmentY.onCreateView(inflater, container, savedInstanceState);
      }
      case SECTION_Z: {
        fragmentZ = new FragmentZ();
        return fragmentZ.onCreateView(inflater, container, savedInstanceState);
      }
      default: {
        //section x
        fragmentX = new FragmentX();
        return fragmentX.onCreateView(inflater, container, savedInstanceState);
      }
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    accelerometerEventListener = null;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (fragmentX != null) {
      counter = fragmentX.getLastX();
    }
    accelerometerEventListener = new SensorEventListener() {
      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {

      }

      @Override
      public void onSensorChanged(SensorEvent event) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final float alpha = 0.8f;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        FragmentX.appendData(new DataPoint(counter, event.values[0] - gravity[0]));
        FragmentY.appendData(new DataPoint(counter, event.values[1] - gravity[1]));
        FragmentZ.appendData(new DataPoint(counter, event.values[2] - gravity[2]));
        ++counter;
      }
    };
  }
}
