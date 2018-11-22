package com.example.munge.app.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.munge.app.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//
//    fun clickButton(view: View) {
//        val meh = arrayOf(1,2, "hej")
//        val text = Toast.makeText(this, Arrays.toString(meh), Toast.LENGTH_SHORT)
//        text.show()
//    }


    fun changeActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("keyIdentifier", "test")
        startActivity(intent)
    }
}