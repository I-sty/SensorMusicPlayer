package com.szollosi.sensormusicplayer.activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager

import com.google.android.material.tabs.TabLayout
import com.szollosi.sensormusicplayer.MyConstants
import com.szollosi.sensormusicplayer.R
import com.szollosi.sensormusicplayer.SectionsPagerAdapter
import com.szollosi.sensormusicplayer.fragments.BaseFragment
import com.szollosi.sensormusicplayer.util.SensorListener

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.name
    }

    private var mSensor: Sensor? = null

    private var mSensorManager: SensorManager? = null

    private var accelerometerListener: SensorEventListener? = SensorListener()

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        BaseFragment.init(this)

        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val mViewPager = findViewById<ViewPager>(R.id.container)
        mViewPager.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)

        mViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                //        mViewPager.setCurrentItem(tab.getPosition());
                Log.i(TAG, "[onTabSelected]")
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Log.i(TAG, "[onTabUnselected]")

                //        mSectionsPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                Log.i(TAG, "[onTabReselected]")
            }
        })

        //mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(accelerometerListener)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            mSensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mSensorManager?.let {
                mSensor = it.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                it.registerListener(accelerometerListener, mSensor,
                        SensorManager.SENSOR_DELAY_NORMAL)
            }
        }, MyConstants.DELAY_TO_LISTEN_SENSOR.toLong())


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

    override fun onDestroy() {
        super.onDestroy()
        accelerometerListener = null
    }
}
