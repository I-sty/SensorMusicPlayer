package com.szollosi.sensormusicplayer.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.szollosi.sensormusicplayer.R

class SelectorActivity : AppCompatActivity() {

    companion object {
        private val TAG = SelectorActivity::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selector)

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, DataSelectorActivity::class.java))
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}