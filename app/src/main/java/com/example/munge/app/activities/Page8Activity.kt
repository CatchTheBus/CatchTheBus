package com.example.munge.app.activities


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.munge.app.R
import java.util.*





class Page8Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page8)

        //gps button
        val imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton?.setOnClickListener { Toast.makeText(this@Page8Activity, R.string.image_button_clicked, Toast.LENGTH_SHORT).show()}
    }



    }



