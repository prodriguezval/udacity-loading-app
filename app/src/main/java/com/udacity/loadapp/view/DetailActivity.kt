package com.udacity.loadapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.loadapp.util.clearNotification
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private var notificationId = -1
    private lateinit var downloadStatus: String
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        loadExtras()

        // Remove the notification of the status bar
        clearNotification(this, notificationId)

        // initializing views
        tv_file_name.text = fileName


        tv_download_status.text = if (downloadStatus == "Success") {
            getString(R.string.success)
        } else {
            getString(R.string.fail)
        }
        layout_details.transitionToEnd()

        // back button
        btn_back.setOnClickListener {
            finish()
        }


    }

    private fun loadExtras() {
        val extras = intent.extras
        extras?.let {
            notificationId = it.getInt(EXTRA_NOTIFICATION_ID)
            downloadStatus = it.getString(EXTRA_DOWNLOAD_STATUS)!!
            fileName = it.getString(EXTRA_FILE_NAME)!!
        }
    }

    companion object {
        private const val EXTRA_NOTIFICATION_ID = "channel_id"
        private const val EXTRA_DOWNLOAD_STATUS = "download_status"
        private const val EXTRA_FILE_NAME = "file_name"

        fun withExtras(
            notificationId: Int,
            downloadStatus: String,
            fileName: String
        ): Bundle {
            return Bundle().apply {
                putInt(EXTRA_NOTIFICATION_ID, notificationId)
                putString(EXTRA_DOWNLOAD_STATUS, downloadStatus)
                putString(EXTRA_FILE_NAME, fileName)
            }
        }
    }
}
