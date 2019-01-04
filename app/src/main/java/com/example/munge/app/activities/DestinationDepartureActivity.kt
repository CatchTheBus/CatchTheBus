package com.example.munge.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_destination.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class DestinationDepartureActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prevActivity"
    private val buses: ArrayList<String> = ArrayList()
    private val busDestinations: ArrayList<String> = ArrayList()
    private val busTimes: ArrayList<String> = ArrayList()
    private val information = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_departure)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set header to location name
        findViewById<TextView>(R.id.location).text = intent.extras["search"].toString()

        val searchFromId = intent.extras["search_from_id_departures"]


        information["search_from_id_departures"] = searchFromId.toString()
        information["search"] = intent.extras["search"].toString()

        val url = "http://www.labs.skanetrafiken.se/v2.2/stationresults.asp?selPointFrKey=$searchFromId"

        val data = GetAPIData("destination_departure").execute(url).get()

        for (i in 0..(data.length() - 1)) {
            val name = data.getJSONObject(i)["Name"].toString()
            val depTime = data.getJSONObject(i)["JourneyDateTime"].toString()
            val to = data.getJSONObject(i)["Towards"].toString()

            if (getTime(depTime) > 0) {
                buses.add(name)
                busTimes.add(depTime)
                busDestinations.add(to)
            }
        }


        // Creates a vertical Layout Manager
        bus_list.layoutManager = LinearLayoutManager(this)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 3)

        // Access the RecyclerView Adapter and load the data into it
        bus_list.adapter = BusAdapter(buses, busDestinations, busTimes, information ,this, "departures")
    }


    //settings menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun changeToSettings() {
        //val intent = Intent(this, AppSettingsActivity::class.java)
        val intent = Intent(this, SettingsTestActivity::class.java)
        //startActivity(intent)
        intent.putExtra(INTENT_PREV_ACTIVITY, "destinationDeparture")
        //intent.putExtra(INTENT_SEARCH, findViewById<TextInputEditText>(R.id.search_from).getText().toString())
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
            when (intent.extras["prevActivity"].toString()) {
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

    private fun getTime(depTime: String): Long {
        val currentTime = Calendar.getInstance().getTime()
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = format.parse(depTime)
        val millisInFuture = date.time - currentTime.time
        return millisInFuture
    }
}