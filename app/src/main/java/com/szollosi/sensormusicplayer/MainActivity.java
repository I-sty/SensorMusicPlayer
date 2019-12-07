package com.szollosi.sensormusicplayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.szollosi.sensormusicplayer.fragments.BaseFragment;
import com.szollosi.sensormusicplayer.util.SensorListener;

public class MainActivity extends AppCompatActivity {

  private String TAG = MainActivity.class.getName();

  private Sensor mSensor;

  @Nullable
  private SensorManager mSensorManager;

  /*private Handler mHandler;*/

/*  private Runnable calcPeak = new Runnable() {

    @Override
    public void run() {
      final ArrayList<Entry> list =
          new ArrayList<>(FragmentXYZ.getPeakWindow());
      if (list.isEmpty()) {
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
      Entry peakPoint = list.get(0);
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
  };*/

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

  private SensorEventListener accelerometerListener = new SensorListener();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    BaseFragment.init(this);

    SectionsPagerAdapter mSectionsPagerAdapter =
        new SectionsPagerAdapter(getSupportFragmentManager());
    ViewPager mViewPager = findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(mViewPager);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

      @Override
      public void onTabSelected(TabLayout.Tab tab) {
//        mViewPager.setCurrentItem(tab.getPosition());
        Log.i(TAG, "[onTabSelected]");
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
        Log.i(TAG, "[onTabUnselected]");

//        mSectionsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
        Log.i(TAG, "[onTabReselected]");
      }
    });

    //mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (mSensorManager != null) {
      mSensorManager.unregisterListener(accelerometerListener);
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
            .registerListener(accelerometerListener, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
      }
    }, Constants.DELAY_TO_LISTEN_SENSOR);


//    JobInfo.Builder builder =
//        new JobInfo.Builder(1, new ComponentName(getPackageName(), PeakCalculator.class.getName()));
//    builder.setPeriodic(500);
//    builder.setBackoffCriteria(500, JobInfo.BACKOFF_POLICY_LINEAR);
//    builder.setRequiresCharging(false);
//
//    if (mJobScheduler.schedule(builder.build()) <= 0) {
//      Log.e(TAG, "onCreate: Some error while scheduling the job");
//    }
//    mHandler = new Handler(Looper.myLooper());
//    mHandler.postDelayed(calcPeak, DELAY_CALC_PEAK);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    accelerometerListener = null;
  }
}
