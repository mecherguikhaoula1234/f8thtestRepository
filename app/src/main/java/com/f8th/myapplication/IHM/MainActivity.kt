package com.f8th.myapplication.IHM

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.f8th.myapplication.R
import com.f8th.myapplication.Services.serviceForBackgroundForegroundRunnig

/**
 * activity for managing the app
 */
class MainActivity : AppCompatActivity(), LifecycleObserver {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, serviceForBackgroundForegroundRunnig::class.java))
        if (intent.flags == FLAG_ACTIVITY_NEW_TASK) {
            moveTaskToBack(true)
        } else {
            setContentView(R.layout.activity_main)
        }
    }
    
    override fun onPause() {
        super.onPause()
    }
    
    override fun onBackPressed() {}
}
