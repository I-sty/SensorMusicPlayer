package com.szollosi.sensormusicplayer.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.szollosi.sensormusicplayer.R

class DataSelectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_selector)
        findViewById<Button>(R.id.buttonToRight).setOnClickListener {
            startActivity(Intent(this, DataCollectorActivity::class.java))
        }
    }
}
