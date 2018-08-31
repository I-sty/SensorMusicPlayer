package com.kalosis.sensormusicplayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kalosis.sensormusicplayer.utility.FragmentXYZUtility;

public class MainActivity extends AppCompatActivity {

  /** Delay before the sensor recording start */
  private static final short DELAY_RECORD_SENSOR = 500;


  private Sensor mSensor;

  @Nullable
  private SensorManager mSensorManager;

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
      Toast.makeText(getApplicationContext(), "Settings is not implemented yet", Toast.LENGTH_SHORT).show();
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
    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    ViewPager mViewPager = findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = findViewById(R.id.tabs);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    FragmentXYZUtility.getInstance().destroy();
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
      mSensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
      if (mSensorManager != null) {
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(PlaceholderFragment.getAccelerometerEventListener(), mSensor,
            SensorManager.SENSOR_DELAY_FASTEST);
      }
    }, DELAY_RECORD_SENSOR);
  }
}
