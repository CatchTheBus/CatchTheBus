package com.example.munge.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_destination.*
import org.json.JSONArray

class DestinationActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prev_activity"
    private val buses: ArrayList<String> = ArrayList()
    private val busDestinations: ArrayList<String> = ArrayList()
    private val busTimes: ArrayList<String> = ArrayList()


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

            val searchFromId = intent.extras["search_from_id"]
            val searchToId = intent.extras["search_to_id"]

            findViewById<TextView>(R.id.location).text = searchFrom

            val url = "http://www.labs.skanetrafiken.se/v2.2/resultspage.asp?cmdaction=next&selPointFr=$searchFrom|$searchFromId|0&selPointTo=$searchTo|$searchToId|0"
            val newUrl: String = url.replace("ö", "%C3%B6").replace("ä", "%C3%A4").replace("å", "%C3%A5").replace(" ", "%20")

            val data = GetAPIData("destination_journey").execute(newUrl).get()

            for (i in 0..(data.length() - 1)) {
                val routes = data.getJSONObject(i).getJSONObject("RouteLinks")["RouteLink"]
                var name = ""
                var depTime = ""

                if (routes is JSONArray) {
                    val routeLink = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONArray("RouteLink")

                    for (x in 0..(routeLink.length() - 1)) {
                        if (x == 0) {
                            val item = routeLink.getJSONObject(x)
                            name = item.getJSONObject("Line")["Name"].toString()
                            depTime = item["DepDateTime"].toString()
                        }
                    }
                } else {
                    name = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink").getJSONObject("Line")["Name"].toString()
                    depTime = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink")["DepDateTime"].toString()
                }

                buses.add(name)
                busTimes.add(depTime)
                busDestinations.add(searchTo)
            }
        } else {
            // Set header to location name
            findViewById<TextView>(R.id.location).text = intent.extras["search"].toString()

            val searchFromId = intent.extras["search_from_id_departures"]

            val url = "http://www.labs.skanetrafiken.se/v2.2/stationresults.asp?selPointFrKey=$searchFromId"

            val data = GetAPIData("destination_departure").execute(url).get()

            for (i in 0..(data.length() - 1)) {
                val name = data.getJSONObject(i)["Name"].toString()
                val depTime = data.getJSONObject(i)["JourneyDateTime"].toString()
                val to = data.getJSONObject(i)["Towards"].toString()

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
        bus_list.adapter = BusAdapter(buses, busDestinations, busTimes, this)
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
        intent.putExtra(INTENT_PREV_ACTIVITY, "destination")
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
}
