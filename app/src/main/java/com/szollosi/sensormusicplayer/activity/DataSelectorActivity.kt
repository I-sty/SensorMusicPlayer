package com.szollosi.sensormusicplayer.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputEditText
import com.szollosi.sensormusicplayer.MyConstants
import com.szollosi.sensormusicplayer.R
import com.szollosi.sensormusicplayer.databinding.ActivityDataSelectorBinding
import com.szollosi.sensormusicplayer.util.Gesture

class DataSelectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDataSelectorBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_selector)
        binding.activity = this
    }

    private val editText = findViewById<TextInputEditText>(R.id.usernameTextInputEditText)

    fun onGestureButtonClick(gesture: Gesture?) {
        val username: String = editText?.text?.toString() ?: "John Doe"
        val dataCompatActivityIntent = Intent(this, DataCollectorActivity::class.java).apply {
            putExtra(MyConstants.KEY_USERNAME, username)
            putExtra(MyConstants.KEY_GESTURE, gesture)
        }
        startActivity(dataCompatActivityIntent)

    }

}