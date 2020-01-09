package com.szollosi.sensormusicplayer.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Environment
import android.os.Vibrator
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.szollosi.sensormusicplayer.MyConstants
import com.szollosi.sensormusicplayer.R
import com.szollosi.sensormusicplayer.util.Gesture
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.abs


class DataCollectorActivity : AppCompatActivity(), SensorEventListener {
    companion object {
        private const val REQUEST_CODE_ASK_PERMISSIONS = 123
        private const val INVALID_THRESHOLD = 0.2f
    }

    private lateinit var f: FileOutputStream
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f

    private lateinit var vibrator: Vibrator
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var deltaXMax = 0f
    private var deltaYMax = 0f
    private var deltaZMax = 0f

    private var deltaX = 0f
    private var deltaY = 0f
    private var deltaZ = 0f

    private var vibrateThreshold = 0f

    private var currentX: TextView? = null
    private var currentY: TextView? = null
    private var currentZ: TextView? = null
    private var maxX: TextView? = null
    private var maxY: TextView? = null
    private var maxZ: TextView? = null

    private lateinit var labelStart: String
    private lateinit var labelStop: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collector)
        labelStart = this.getString(R.string.label_start)
        labelStop = this.getString(R.string.label_stop)
        val extras = intent?.extras ?: return

        val username = extras.getString(MyConstants.KEY_USERNAME)
        val gesture = extras.getSerializable(MyConstants.KEY_GESTURE) as Gesture

        Toast.makeText(this, "username = $username, gesture = ${gesture.name}", Toast.LENGTH_LONG).show()
        initializeViews()
        checkPermissions()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager?.let {
            if (it.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                // success! we have an accelerometer

                accelerometer = it.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

            } else {
                // fail! we don't have an accelerometer!
                Toast.makeText(baseContext, "Can't Find Data ", Toast.LENGTH_LONG).show()
            }
        }

        //initialize vibration
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private fun initializeViews() {
        currentX = findViewById(R.id.currentX)
        currentY = findViewById(R.id.currentY)
        currentZ = findViewById(R.id.currentZ)

        maxX = findViewById(R.id.maxX)
        maxY = findViewById(R.id.maxY)
        maxZ = findViewById(R.id.maxZ)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.values == null) return

        // clean current values
        displayCleanValues()
        // display the current x,y,z accelerometer values
        displayCurrentValues()
        // display the max x,y,z accelerometer values
        displayMaxValues()

        // get the change of the x,y,z values of the accelerometer
        deltaX = abs(lastX - event.values[0])
        deltaY = abs(lastY - event.values[1])
        deltaZ = abs(lastZ - event.values[2])

        lastX = event.values[0]
        lastY = event.values[1]
        lastZ = event.values[2]

        // if the change is below 2, it is just plain noise
        if (deltaX < INVALID_THRESHOLD)
            deltaX = 0f
        if (deltaY < INVALID_THRESHOLD)
            deltaY = 0f
        if (deltaZ < INVALID_THRESHOLD)
            deltaZ = 0f


        val entry = currentX?.text.toString() + "," + currentY?.text.toString() + "," + currentZ?.text.toString() + "\n"

        try {
            f.write(entry.toByteArray(Charsets.UTF_8))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun displayCleanValues() {
        currentX?.text = "0.0"
        currentY?.text = "0.0"
        currentZ?.text = "0.0"
    }

    // display the current x,y,z accelerometer values
    private fun displayCurrentValues() {
        currentX?.text = deltaX.toString()
        currentY?.text = deltaY.toString()
        currentZ?.text = deltaZ.toString()
    }

    // display the max x,y,z accelerometer values
    private fun displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX
            maxX?.text = deltaX.toString()
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY
            maxY?.text = deltaY.toString()
        }

        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ
            maxZ?.text = deltaZ.toString()
        }
    }

    private fun checkPermissions() {
        val hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_ASK_PERMISSIONS)
            return
        }
        Toast.makeText(this, "Permission is already granted", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(baseContext, "Permission Granted", Toast.LENGTH_LONG).show()
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL_STORAGE Denied", Toast.LENGTH_SHORT).show()
                }
            else -> {
            }
        }
    }

    fun toggleRecording(v: View) {
        if (v !is TextView) {
            return
        }

        if (v.text == labelStart) {
            //start recording
            v.text = labelStop

            openFile()
            accelerometer?.let { acc ->
                sensorManager?.registerListener(this@DataCollectorActivity, acc, SensorManager.SENSOR_DELAY_UI)
                vibrateThreshold = acc.maximumRange / 3
            }
        } else if (v.text == labelStop) {
            //stop recording
            v.text = labelStart

            sensorManager?.unregisterListener(this)
            closeFile()
        }
    }

    private fun closeFile() {
        try {
            f.flush()
            f.close()
            Toast.makeText(baseContext, "Data saved", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun openFile() {
        val sdCard = Environment.getExternalStorageDirectory()
        val dir = File(sdCard.absolutePath + "/isti-example")
        dir.mkdir()

        val file = File(dir, "output_${Date().time}.csv")
        try {
            f = FileOutputStream(file, true)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}
