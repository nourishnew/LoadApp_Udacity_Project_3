package com.udacity

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        var extras: Bundle= intent.extras!!
        var type=extras.getString("TYPE")
        file_type.text=type
        val status= extras.getString("STATUS")
        status_text.text=status


        ok_button.setOnClickListener{
            val mainIntent= Intent(applicationContext,MainActivity::class.java)
            startActivity(mainIntent)
        }


    }

}
