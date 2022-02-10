package com.udacity.loadapp.view

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.loadapp.util.Channel
import com.udacity.loadapp.util.Message
import com.udacity.loadapp.util.createChannel
import com.udacity.loadapp.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var selectedURL: String? = null
    private var selectedFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = Channel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                getString(R.string.notification_channel_description)
            )
            createChannel(channel, this)
        }
        custom_button.setOnClickListener {
            onButtonClicked()
        }
    }

    private fun onButtonClicked() {
        if (selectedURL != null) {
            custom_button.buttonState = ButtonState.Loading
            download()

        } else {
            val toast = Toast.makeText(
                applicationContext,
                getString(R.string.please_select_option),
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            custom_button.buttonState = ButtonState.Completed

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                var downloadStatus = "Fail"
                if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    downloadStatus = "Success"
                }

                val toast = Toast.makeText(
                    applicationContext,
                    getString(R.string.notification_description),
                    Toast.LENGTH_LONG
                )
                toast.show()

                val notificationManager = getSystemService(NotificationManager::class.java)
                val message = Message(selectedFileName!!, downloadStatus)
                notificationManager.sendNotification(
                    CHANNEL_ID,
                    message,
                    applicationContext
                )
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(selectedURL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {

            when (view.getId()) {
                R.id.glide_option ->
                    selectedURL = GLIDE_URL
                R.id.load_app_option ->
                    selectedURL = LOAD_APP_URL
                R.id.option_retrofit ->
                    selectedURL = RETROFIT_URL
            }

            selectedFileName = view.text.toString()
        }
    }

    companion object {
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/masterXXX.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"

        private const val CHANNEL_ID = "download_channel"
    }
}
