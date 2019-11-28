package com.szollosi.sensormusicplayer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.data.Entry;
import com.szollosi.sensormusicplayer.controller.Section;
import com.szollosi.sensormusicplayer.fragments.FragmentX;
import com.szollosi.sensormusicplayer.fragments.FragmentXYZ;
import com.szollosi.sensormusicplayer.fragments.FragmentY;
import com.szollosi.sensormusicplayer.fragments.FragmentZ;

import static com.szollosi.sensormusicplayer.controller.Section.SECTION_XYZ;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

  /**
   * The fragment argument representing the section number for this
   * fragment.
   */
  private static final String ARG_SECTION_NUMBER = "section_number";

  private static final float THRESHOLD = 0.3f;

  @Nullable
  private static SensorEventListener accelerometerEventListener;

  private final Counter counter = new Counter();

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
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final Section sectionNumber =
        getArguments() != null ? Section.values()[getArguments().getInt(ARG_SECTION_NUMBER) - 1] :
            SECTION_XYZ;
    switch (sectionNumber) {
      case SECTION_Y: {
        FragmentY fragmentY = new FragmentY();
        return fragmentY.onCreateView(inflater, container, savedInstanceState);
      }
      case SECTION_Z: {
        FragmentZ fragmentZ = new FragmentZ();
        return fragmentZ.onCreateView(inflater, container, savedInstanceState);
      }
      case SECTION_X: {
        FragmentX fragmentX = new FragmentX();
        return fragmentX.onCreateView(inflater, container, savedInstanceState);
      }
      case SECTION_XYZ: {
        FragmentXYZ fragmentXYZ = new FragmentXYZ();
        return fragmentXYZ.onCreateView(inflater, container, savedInstanceState);
      }
      case SECTION_CONFIG: {
//        FragmentConfig fragmentConfig = new FragmentConfig();
//        fragmentConfig.onCreate(savedInstanceState);
//        return fragmentConfig.getView();
      }
      default: {
        return null;
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
    accelerometerEventListener = new SensorEventListener() {

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {

      }

      @Override
      public void onSensorChanged(SensorEvent event) {
        synchronized (counter) {
          //Log.d(TAG, "[onSensorChanged] counter: " + counter.getCounter());
          // In this example, alpha is calculated as t / (t + dT),
          // where t is the low-pass filter's time-constant and
          // dT is the event delivery rate.
          final float alpha = 0.8f;

          // Isolate the force of gravity with the low-pass filter.
          gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
          gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
          gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

          // Remove the gravity contribution with the high-pass filter.
          Entry dataPointX =
              new Entry(counter.getCounter(), initYValue(event.values[0] - gravity[0]));
          FragmentX.appendData(dataPointX);
          Entry dataPointY =
              new Entry(counter.getCounter(), initYValue(event.values[1] - gravity[1]));
          FragmentY.appendData(dataPointY);
          Entry dataPointZ =
              new Entry(counter.getCounter(), initYValue(event.values[2] - gravity[2]));
          FragmentZ.appendData(dataPointZ);
          FragmentXYZ.appendData(dataPointX, dataPointY, dataPointZ);
          counter.increment();
        }
      }
    };
  }

  private float initYValue(float v) {
    return Math.abs(v) < THRESHOLD ? 0f : v;
  }
}
