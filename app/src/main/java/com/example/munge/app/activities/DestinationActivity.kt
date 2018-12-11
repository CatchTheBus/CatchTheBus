package com.example.munge.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.Log.*
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_destination.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text

class DestinationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (intent.extras["prev_activity"].toString() == "journey") {

            val searchFrom = intent.extras["search_from"].toString()
            val searchTo = intent.extras["search_to"].toString()

            findViewById<TextView>(R.id.location).text = searchFrom

            val url = "http://www.labs.skanetrafiken.se/v2.2/querypage.asp?inpPointFr=$searchFrom&inpPointTo=$searchTo"
            val data = GetAPIData(findViewById(R.id.testField)).execute(url).get().getJSONObject(0)

            Log.d("type", data::class.toString())


            val start = data.getJSONArray("start")
            val end = data.getJSONArray("end")

            var startId = 0
            var endId = 0

//
            for (i in 0..(start.length() - 1)) {
                val item = start.getJSONObject(i)

                if (item["Name"] == searchFrom) {
                    startId = item["Id"] as Int
                }
            }

            for (i in 0..(end.length() - 1)) {
                val item = end.getJSONObject(i)

                if (item["Name"] == searchTo) {
                    endId = item["Id"] as Int
                }
            }

            Log.d("type", start.toString())
            Log.d("type", end.toString())


            Log.d("id", startId.toString())
            Log.d("id", endId.toString())

//
//            for (i in 0..(start.length() - 1)) {
//                val item = start.getJSONObject(i)
//            }



        } else {
            // Set header to location name
            findViewById<TextView>(R.id.location).text = intent.extras["search"].toString()
        }

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

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Print" item
            Toast.makeText(this,"Settings", Toast.LENGTH_LONG).show()
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
