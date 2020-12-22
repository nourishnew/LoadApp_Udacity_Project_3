package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private  var retrofit_download_id:Long=0
    private  var glide_download_id:Long=0
    private  var repository_download_id:Long=0
    private lateinit var downloadManager:DownloadManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(getString(R.string.download_notification_channel_id), getString(R.string.notification_title))

        custom_button.setOnClickListener {

                val checked= glide_download.isChecked || repository_download.isChecked|| retrofit_download.isChecked

                if(checked){
                    when {
                        glide_download.isChecked -> {
                            download(glideURL)
                        }
                        repository_download.isChecked -> {
                            download(repositoryURL)
                        }
                        retrofit_download.isChecked -> {
                            download(retrofitURL)
                        }
                    }
                }
            else{
                Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val cursor:Cursor=downloadManager.query(DownloadManager.Query().setFilterById(id!!))

            if (cursor.moveToNext()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val notificationManager= ContextCompat.getSystemService(context!!, NotificationManager::class.java) as NotificationManager

                cursor.close()
                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        when (id) {
                            glide_download_id -> {
                                notificationManager.sendNotification("Download complete", context!!, "glide","failed")

                            }
                            repository_download_id -> {
                                notificationManager.sendNotification("Download complete", context!!, "repository","failed")

                            }
                            retrofit_download_id -> {
                                notificationManager.sendNotification("Download complete", context!!, "retrofit","failed")

                            }

                        }
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        when (id) {
                            glide_download_id -> {
                                notificationManager.sendNotification("Download complete", context!!, "glide","success")

                            }
                            repository_download_id -> {
                                notificationManager.sendNotification("Download complete", context!!, "repository","success")

                            }
                            retrofit_download_id -> {
                                notificationManager.sendNotification("Download complete", context!!, "retrofit","success")

                            }

                        }
                    }
                }
            }




        }
    }

    private fun download(url: String) {
        val request =
                DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        when (url) {
            glideURL -> {
                glide_download_id = downloadManager.enqueue(request)
            }
            retrofitURL -> {
                retrofit_download_id = downloadManager.enqueue(request)

            }
            repositoryURL -> {
                repository_download_id = downloadManager.enqueue(request)

            }
        }
       // enqueue puts the download request in the queue.
    }

    companion object {
        private const val glideURL =
            "https://github.com/bumptech/glide"
        private const val repositoryURL="https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"

        private val retrofitURL="https://github.com/square/retrofit"

        private const val CHANNEL_ID = "channelId"
    }
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    // TODO: Step 2.4 change importance
                    NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"

            val notificationManager= getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

        }


    }

}

