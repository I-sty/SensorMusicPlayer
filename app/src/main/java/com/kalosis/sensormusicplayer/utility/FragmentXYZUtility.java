package com.kalosis.sensormusicplayer.utility;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.jjoe64.graphview.series.LineGraphSeries;
import com.kalosis.sensormusicplayer.data.MyDataPoint;
import com.kalosis.sensormusicplayer.fragments.FragmentXYZ;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import java.util.Arrays;
import java.util.List;

public class FragmentXYZUtility {
  private static FragmentXYZUtility self;

  @NonNull
  private final CircularFifoBuffer dataPointsX = new CircularFifoBuffer(Utility.CIRCULAR_BUFFER_SIZE);

  @NonNull
  private final CircularFifoBuffer dataPointsY = new CircularFifoBuffer(Utility.CIRCULAR_BUFFER_SIZE);

  @NonNull
  private final CircularFifoBuffer dataPointsZ = new CircularFifoBuffer(Utility.CIRCULAR_BUFFER_SIZE);

  private FragmentXYZ fragmentXYZ;

  private Handler mHandler;

  private MyDataPoint recentX;

  private MyDataPoint recentY;

  private MyDataPoint recentZ;

  private LineGraphSeries<MyDataPoint> seriesX = new LineGraphSeries<>();

  private LineGraphSeries<MyDataPoint> seriesY = new LineGraphSeries<>();

  private LineGraphSeries<MyDataPoint> seriesZ = new LineGraphSeries<>();

  private final Runnable refreshGraph = new Runnable() {

    @Override
    public void run() {
      if (recentX == null || recentY == null || recentZ == null) {
        mHandler.postDelayed(this, Utility.DELAY_REFRESH * 2);
        return;
      }
      seriesX.appendData(recentX, true, Utility.CIRCULAR_BUFFER_SIZE, false);
      seriesY.appendData(recentY, true, Utility.CIRCULAR_BUFFER_SIZE, false);
      seriesZ.appendData(recentZ, true, Utility.CIRCULAR_BUFFER_SIZE, false);
      fragmentXYZ.getGraphView().getViewport().setMinX(seriesZ.getLowestValueX());
      fragmentXYZ.getGraphView().getViewport().setMaxX(seriesZ.getHighestValueX());
      mHandler.postDelayed(this, Utility.DELAY_REFRESH);
    }
  };

  private FragmentXYZUtility() {}

  public static FragmentXYZUtility getInstance() {
    if (self == null) {
      self = new FragmentXYZUtility();
    }
    return self;
  }

  public void appendData(@NonNull MyDataPoint dataPointX, @NonNull MyDataPoint dataPointY,
      @NonNull MyDataPoint dataPointZ) {
    dataPointsX.add(dataPointX);
    recentX = dataPointX;
    seriesX.appendData(dataPointX, true, Utility.CIRCULAR_BUFFER_SIZE, true);

    dataPointsY.add(dataPointY);
    recentY = dataPointY;
    seriesY.appendData(dataPointY, true, Utility.CIRCULAR_BUFFER_SIZE, true);

    dataPointsZ.add(dataPointZ);
    recentZ = dataPointZ;
    seriesZ.appendData(dataPointZ, true, Utility.CIRCULAR_BUFFER_SIZE, true);
  }

  /**
   * Clears the peak window
   */
  public void clearPeakWindowX() {
    synchronized (dataPointsX) {
      dataPointsX.clear();
    }
  }

  /**
   * Clears the peak window
   */
  public void clearPeakWindowY() {
    synchronized (dataPointsY) {
      dataPointsY.clear();
    }
  }

  /**
   * Clears the peak window
   */
  public void clearPeakWindowZ() {
    synchronized (dataPointsZ) {
      dataPointsZ.clear();
    }
  }

  /**
   * Clears peak windows
   */
  public void clearPeakWindows() {
    clearPeakWindowX();
    clearPeakWindowY();
    clearPeakWindowZ();
  }

  public void destroy() {
    seriesX = null;
    seriesY = null;
    seriesZ = null;
  }

  /**
   * Return the peak window of the X axis
   *
   * @return The sublist of the X axis.
   */
  @NonNull
  public List<MyDataPoint> getPeakWindowX() {
    synchronized (dataPointsX) {
      return Arrays.asList(toArray(dataPointsX));
    }
  }

  /**
   * Return the peak window of the Y axis
   *
   * @return The sublist of the Y axis.
   */
  @NonNull
  public List<MyDataPoint> getPeakWindowY() {
    synchronized (dataPointsY) {
      return Arrays.asList(toArray(dataPointsY));
    }
  }

  /**
   * Return the peak window of the Z axis
   *
   * @return The sublist of the Z axis.
   */
  @NonNull
  public List<MyDataPoint> getPeakWindowZ() {
    synchronized (dataPointsZ) {
      return Arrays.asList(toArray(dataPointsZ));
    }
  }

  /**
   * @param fragmentXYZ
   * @param context
   */
  public void setFragment(FragmentXYZ fragmentXYZ, Context context) {
    this.fragmentXYZ = fragmentXYZ;
    this.fragmentXYZ.addToGraphView(context, seriesX, seriesY, seriesZ);
  }

  public void setHandler() {
    mHandler = new Handler(Looper.myLooper());
    mHandler.postDelayed(refreshGraph, Utility.DELAY_REFRESH * 2);
  }

  @NonNull
  private synchronized static MyDataPoint[] toArray(CircularFifoBuffer fifoBuffer) {
    if (fifoBuffer == null || fifoBuffer.isEmpty()) {
      return new MyDataPoint[0];
    }
    return (MyDataPoint[]) fifoBuffer.toArray(new MyDataPoint[fifoBuffer.size()]);
  }
}
