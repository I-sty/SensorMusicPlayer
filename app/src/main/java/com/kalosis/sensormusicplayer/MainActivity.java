package com.kalosis.sensormusicplayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.kalosis.sensormusicplayer.fragments.FragmentXYZ;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

  private static final short DELAY_CALC_PEAK = 1200;

  private static final int PEAK_THRESHOLD = 7;

  private String TAG = MainActivity.class.getName();

  private Sensor mSensor;

  @Nullable
  private SensorManager mSensorManager;

  private Handler mHandler;

  private Runnable calcPeak = new Runnable() {

    @Override
    public void run() {
      final ArrayList<MyDataPoint> list =
          new ArrayList<>(FragmentXYZ.getPeakWindow());
      if (list.size() == 0) {
        mHandler.postDelayed(this, DELAY_CALC_PEAK);
        return;
      }
      Collections.sort(list, (o1, o2) -> {
        if (Math.abs(o1.getY()) > Math.abs(o2.getY())) {
          return -1;
        } else if (Math.abs(o1.getY()) > Math.abs(o2.getY())) {
          return 1;
        } else {
          return 0;
        }
      });
      DataPoint peakPoint = list.get(0);
      Log.d(TAG, "[run] \nstart: " + list.get(0) + "\nend: " + list.get(list.size() - 1) +
          "\npeak: " + peakPoint);

      //check if the peak is higher then a threshold
      double peak = peakPoint.getY();
      if (peak >= PEAK_THRESHOLD) {
        Log.i(TAG, "[run] Peak found: " + peakPoint);
        Toast.makeText(getApplicationContext(), "Peak found: " + peakPoint, Toast.LENGTH_SHORT)
            .show();
      }
      mHandler.postDelayed(this, DELAY_CALC_PEAK);
    }
  };

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    SectionsPagerAdapter mSectionsPagerAdapter =
        new SectionsPagerAdapter(getSupportFragmentManager());
    ViewPager mViewPager = findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = findViewById(R.id.tabs);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    //mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (mSensorManager != null) {
      mSensorManager.unregisterListener(PlaceholderFragment.getAccelerometerEventListener());
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    new Handler().postDelayed(() -> {
      mSensorManager =
          (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
      if (mSensorManager != null) {
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager
            .registerListener(PlaceholderFragment.getAccelerometerEventListener(), mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
      }
    }, 600);

//    JobInfo.Builder builder =
//        new JobInfo.Builder(1, new ComponentName(getPackageName(), PeakCalculator.class.getName()));
//    builder.setPeriodic(500);
//    builder.setBackoffCriteria(500, JobInfo.BACKOFF_POLICY_LINEAR);
//    builder.setRequiresCharging(false);
//
//    if (mJobScheduler.schedule(builder.build()) <= 0) {
//      Log.e(TAG, "onCreate: Some error while scheduling the job");
//    }
    mHandler = new Handler(Looper.myLooper());
    mHandler.postDelayed(calcPeak, DELAY_CALC_PEAK);
  }

}
