package com.szollosi.sensormusicplayer.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.szollosi.sensormusicplayer.MyConstants
import com.szollosi.sensormusicplayer.R
import com.szollosi.sensormusicplayer.util.Gesture

class DataCollectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collector)

        val extras = intent?.extras ?: return

        val username = extras.getString(MyConstants.KEY_USERNAME)
        val gesture = extras.getSerializable(MyConstants.KEY_GESTURE) as Gesture

        Toast.makeText(this, "username = $username, gesture = ${gesture.name}", Toast.LENGTH_LONG).show()
    }

}
