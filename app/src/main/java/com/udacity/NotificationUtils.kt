package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


private var NOTIFICATION_ID=0

fun NotificationManager.sendNotification(messageBody:String, applicationContext: Context,type:String,status:String){

    val detailIntent= Intent(applicationContext,DetailActivity::class.java)
    when (type ) {
        "glide" -> {
            detailIntent.putExtra("TYPE","Glide")
            detailIntent.putExtra("STATUS",status)
        }
        "repository" -> {
            detailIntent.putExtra("TYPE","Repository")
            detailIntent.putExtra("STATUS",status)
        }
        "retrofit" -> {
            detailIntent.putExtra("TYPE","Retrofit")
            detailIntent.putExtra("STATUS",status)
        }
    }

    val detailPendingIntent= PendingIntent.getActivity(applicationContext, NOTIFICATION_ID,detailIntent,PendingIntent.FLAG_UPDATE_CURRENT)
    val builder= NotificationCompat.Builder(applicationContext,applicationContext.getString(R.string.download_notification_channel_id))
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            .setContentIntent(detailPendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_assistant_black_24dp,"Check Status",detailPendingIntent)
    notify(NOTIFICATION_ID,builder.build())



}