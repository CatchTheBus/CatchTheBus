package com.example.munge.app.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.munge.app.R


class MainActivity : AppCompatActivity() {

    val CONNECTON_TIMEOUT_MILLISECONDS = 60000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val journeyPlanner = findViewById<Button>(R.id.journey_planner)
        val departures = findViewById<Button>(R.id.depatures)

        journeyPlanner.setOnClickListener { changeToJourneyPlanner() }
        departures.setOnClickListener { changeToDepartures() }

        val url = "http://www.labs.skanetrafiken.se/v2.2/querypage.asp?inpPointFr=lund&inpPointTo=ystad"
        GetAPIData(this).execute(url)
    }


    private fun changeToJourneyPlanner() {
        val intent = Intent(this, JourneyPlannerActivity::class.java)
        startActivity(intent)
    }

    private fun changeToDepartures() {
        val intent = Intent(this, DeparturesActivity::class.java)
        startActivity(intent)
    }

    private fun changeToSettings() {
        val intent = Intent(this, AppSettingsActivity::class.java)
        startActivity(intent)
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
            //Toast.makeText(this,"Settings",Toast.LENGTH_LONG).show()
            //true

            changeToSettings()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}