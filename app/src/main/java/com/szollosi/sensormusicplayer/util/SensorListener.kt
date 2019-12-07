package com.szollosi.sensormusicplayer.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

import com.github.mikephil.charting.data.Entry
import com.szollosi.sensormusicplayer.Constants
import com.szollosi.sensormusicplayer.Counter
import com.szollosi.sensormusicplayer.fragments.BaseFragment
import com.szollosi.sensormusicplayer.fragments.FragmentUtil

class SensorListener : SensorEventListener {


  private val counter = Counter()

  private val gravity = FloatArray(3)


  override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

  }

  override fun onSensorChanged(event: SensorEvent) {
    synchronized(counter) {
      //Log.d(TAG, "[onSensorChanged] counter: " + counter.getCounter());
      // In this example, alpha is calculated as t / (t + dT),
      // where t is the low-pass filter's time-constant and
      // dT is the event delivery rate.
      val alpha = 0.8f

      // Isolate the force of gravity with the low-pass filter.
      val x = event.values[0]
      val y = event.values[1]
      val z = event.values[2]

      gravity[0] = alpha * gravity[0] + (1 - alpha) * x
      gravity[1] = alpha * gravity[1] + (1 - alpha) * y
      gravity[2] = alpha * gravity[2] + (1 - alpha) * z

      // Remove the gravity contribution with the high-pass filter.
      val counter1 = counter.counter

      val dataPointX = Entry(counter1.toFloat(), initYValue(x - gravity[0]))
      val dataPointY = Entry(counter1.toFloat(), initYValue(y - gravity[1]))
      val dataPointZ = Entry(counter1.toFloat(), initYValue(z - gravity[2]))

      FragmentUtil.addItemToList(BaseFragment.dataPointsX, dataPointX)
      FragmentUtil.addItemToList(BaseFragment.dataPointsY, dataPointY)
      FragmentUtil.addItemToList(BaseFragment.dataPointsZ, dataPointZ)

      counter.increment()
    }
  }

  private fun initYValue(v: Float): Float {
    return if (Math.abs(v) < Constants.THRESHOLD) 0f else v
  }

}
