package com.example.munge.app.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.munge.app.R
import java.util.*

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }


    fun clickButton(view: View) {
        val meh = arrayOf(1,2, "hej")
        val text = Toast.makeText(this, Arrays.toString(meh), Toast.LENGTH_SHORT)
        text.show()
    }
}