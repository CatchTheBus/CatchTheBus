package com.example.munge.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_destination.*

class DestinationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set header to location name
        findViewById<TextView>(R.id.location).text = intent.extras["search"].toString()

        addBuses()
        addDestinations()
        addTimes()

        // Creates a vertical Layout Manager
        bus_list.layoutManager = LinearLayoutManager(this)

         // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 3)

        // Access the RecyclerView Adapter and load the data into it
        bus_list.adapter = BusAdapter(buses, bus_destinations, bus_times, this)
    }

    //settings menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun changeToSettings() {
        //val intent = Intent(this, AppSettingsActivity::class.java)
        val intent = Intent(this, SettingsTestActivity::class.java)
        startActivity(intent)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // move to settings
            changeToSettings()
            true
        }
        android.R.id.home ->{
            when (intent.extras["prev_activity"].toString()) {
                "journey" -> startActivity(Intent(this, JourneyPlannerActivity::class.java))
                "departure" ->  startActivity(Intent(this, DeparturesActivity::class.java))
            }
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    val buses: ArrayList<String> = ArrayList()

    fun addBuses() {
        buses.add("441")
        buses.add("440")
        buses.add("444")
        buses.add("470")
    }

    val bus_destinations: ArrayList<String> = ArrayList()

    fun addDestinations() {
        bus_destinations.add("To: Stockholm")
        bus_destinations.add("To: Stockholm")
        bus_destinations.add("To: Stockholm")
        bus_destinations.add("To: Stockholm")
    }

    val bus_times: ArrayList<String> = ArrayList()

    fun addTimes() {
        bus_times.add("TTD: 5 min")
        bus_times.add("TTD: 5 min")
        bus_times.add("TTD: 5 min")
        bus_times.add("TTD: 5 min")
    }
}
