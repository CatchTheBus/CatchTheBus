package com.example.munge.app.activities


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
//import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.munge.app.R


class DeparturesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departures)

        //gps button
        val imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton?.setOnClickListener { Toast.makeText(this@DeparturesActivity, R.string.image_button_clicked, Toast.LENGTH_SHORT).show()}


        //imageButton?.setOnClickListener(object : View.OnClickListener() {
        //    override fun onClick(v: View) {
        //        // Code here executes on main thread after user presses button

         //   }
        //})

        setSupportActionBar(findViewById(R.id.toolbar))

        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Print" item
            Toast.makeText(this,"Settings",Toast.LENGTH_LONG).show()
            true
        }
        android.R.id.home ->{
            val activityToStart = intent.extras["prev_activity"].toString().split(" ").last()
            val c = Class.forName(activityToStart)
            val intent = Intent(this, c)
            startActivity(intent)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }



    }



