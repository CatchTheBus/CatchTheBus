package com.example.munge.app.activities


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.munge.app.R


class DeparturesActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prev_activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departures)

        //gps button
        val imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton?.setOnClickListener { Toast.makeText(this, R.string.image_button_clicked, Toast.LENGTH_SHORT).show() }

        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val searchButton = findViewById<Button>(R.id.search_departure)

        searchButton.setOnClickListener { changeToDestinations() }
    }

    private fun changeToDestinations() {
        val intent = Intent(this, DestinationActivity::class.java)
        intent.putExtra(INTENT_PREV_ACTIVITY, "departure")
        startActivity(intent)
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Print" item
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
            true
        }
        android.R.id.home -> {
            val intent = Intent(this, MainActivity::class.java)
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



