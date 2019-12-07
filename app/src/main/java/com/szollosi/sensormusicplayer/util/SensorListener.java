package com.szollosi.sensormusicplayer.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.github.mikephil.charting.data.Entry;
import com.szollosi.sensormusicplayer.Constants;
import com.szollosi.sensormusicplayer.Counter;
import com.szollosi.sensormusicplayer.fragments.BaseFragment;
import com.szollosi.sensormusicplayer.fragments.FragmentUtil;

public class SensorListener implements SensorEventListener {


  private final Counter counter = new Counter();

  private float[] gravity = new float[3];


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
      float x = event.values[0];
      float y = event.values[1];
      float z = event.values[2];

      gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
      gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
      gravity[2] = alpha * gravity[2] + (1 - alpha) * z;

      // Remove the gravity contribution with the high-pass filter.
      final long counter1 = counter.getCounter();

      Entry dataPointX =
          new Entry(counter1, initYValue(x - gravity[0]));
      Entry dataPointY =
          new Entry(counter1, initYValue(y - gravity[1]));
      Entry dataPointZ =
          new Entry(counter1, initYValue(z - gravity[2]));

      FragmentUtil.addItemToList(BaseFragment.dataPointsX, dataPointX);
      FragmentUtil.addItemToList(BaseFragment.dataPointsY, dataPointY);
      FragmentUtil.addItemToList(BaseFragment.dataPointsZ, dataPointZ);

      counter.increment();
    }
  }

  private float initYValue(float v) {
    return Math.abs(v) < Constants.THRESHOLD ? 0f : v;
  }

}
