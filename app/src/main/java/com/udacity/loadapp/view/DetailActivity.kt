package com.udacity.loadapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.loadapp.util.clearNotification
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

const val EXTRA_NOTIFICATION_ID = "notification_id"
const val EXTRA_DOWNLOAD_STATUS = "download_status"
const val EXTRA_FILE_NAME = "file_name"

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        // Remove the notification of the status bar
        clearNotification(this, intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1))

        // initializing views
        tv_file_name.text = intent.getStringExtra(EXTRA_FILE_NAME)

        val downloadStatus = intent.getStringExtra(EXTRA_DOWNLOAD_STATUS)
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

}
