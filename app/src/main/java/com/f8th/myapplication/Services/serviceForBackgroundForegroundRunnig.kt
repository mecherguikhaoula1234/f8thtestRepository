package com.f8th.myapplication.Services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.VolumeProviderCompat

/**
 * class to manage the execution of the application in Background and foreground
 */
class serviceForBackgroundForegroundRunnig : Service() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground()
        else startForeground(1, Notification())
        setEvent()

    }

    /**
     * to manage the listeners of the class serviceForBackgroundForegroundRunnig
     */
    fun setEvent() {
        mediaSession = MediaSessionCompat(this, "serviceForBackgroundRunnig")
        mediaSession!!.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession!!.setPlaybackState(PlaybackStateCompat.Builder()
                                                           .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0f)
                                                           //you simulate a player which plays something.
                                                           .build())

        // to listen to the number of click on the button "volume down"
        val myVolumeProvider: VolumeProviderCompat = object : VolumeProviderCompat(
                VOLUME_CONTROL_RELATIVE,  /*max volume*/100,  /*initial volume level*/50) {
            override fun onAdjustVolume(direction: Int) {
                /*
               -1 -- volume down
               1 -- volume up
               0 -- volume button released
                */
                if (direction == -1) {
                    numberOfClick ++
                    if (numberOfClick > 2) {
                        val i = Intent()

                        i.setClassName("com.f8th.myapplication", "com.f8th.myapplication.IHM.MainActivity")
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext.startActivity(i)
                        // after 3 clicks consecutively , upadate the value of number of click
                        numberOfClick = 0
                    }
                }
            }
        }
        mediaSession!!.setPlaybackToRemote(myVolumeProvider)
        mediaSession!!.isActive = true
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "com.f8th.myapplication"
        val channelName = "My Background Service"
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE)

        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
                                                            .setContentTitle("App is running in background")
                                                            .setPriority(NotificationManager.IMPORTANCE_MIN)
                                                            .setCategory(Notification.CATEGORY_SERVICE)
                                                            .build()
        startForeground(2, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession!!.release()
    }

    //---------------------------------------------------------------------
    //  Members
    //---------------------------------------------------------------------

    var numberOfClick = 0
    var mediaSession: MediaSessionCompat? = null
}