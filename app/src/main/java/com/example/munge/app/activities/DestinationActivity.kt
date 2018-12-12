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

    val buses: ArrayList<String> = ArrayList()
    val bus_destinations: ArrayList<String> = ArrayList()
    val bus_times: ArrayList<String> = ArrayList()

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

            Log.d("journeys", newUrl)

            val data = GetAPIData("destination").execute(newUrl).get()



            for (i in 0..(data.length() - 1)) {

//                val id = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink").getJSONObject("Line")["LineTypeId"]

                val routes = data.getJSONObject(i).getJSONObject("RouteLinks")["RouteLink"]

//                Log.d("journey", routes::class.toString())
//
//                Log.d("journey", routes.toString())
                var name: Any
                var depTime: String

                if (routes is JSONArray) {
                    name = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONArray("RouteLink")[0]
                    Log.d("journey", name.toString())
                    Log.d("journey", name::class.toString())
                } else {
                    Log.d("journey", "Object")
                }

//
//                if (routes > 1) {
//                    Log.d("journey", data.getJSONObject(i).getJSONObject("RouteLinks").toString())
////                    name = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink").getJSONObject("Line")["Name"].toString()
////                    depTime = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink")["DepDateTime"].toString()
//                } else {
//                    name = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink").getJSONObject("Line")["Name"].toString()
//                    depTime = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink")["DepDateTime"].toString()
//                }
//
//                buses.add(name)
//                bus_times.add(depTime)
//                bus_destinations.add(searchTo)
//
//                try {
//                   val name = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink").getJSONObject("Line")["Name"]
//                   val depTime = data.getJSONObject(i).getJSONObject("RouteLinks").getJSONObject("RouteLink")["DepDateTime"]
//
//
//                    buses.add(name.toString())
//                    bus_times.add(depTime.toString())
//                    bus_destinations.add(searchTo)
//                } catch (ex: Exception) {
//
//               }
//
//                Log.d("info", number.toString())
//                Log.d("info", depTime.toString())
            }
//            Log.d("journeys", data.length().toString())
//            Log.d("journeys", data.toString())
//            Log.d("journeys", data::class.toString())


//            val journeys = data.getJSONArray("journeys")
//
//            Log.d("journeys", journeys.toString())


//            val start = data.getJSONArray("start")
//            val end = data.getJSONArray("end")

//            var startId = 0
//            var endId = 0
//
////
//            for (i in 0..(start.length() - 1)) {
//                val item = start.getJSONObject(i)
//
//                if (item["Name"] == searchFrom) {
//                    startId = item["Id"] as Int
//                }
//            }
//
//            for (i in 0..(end.length() - 1)) {
//                val item = end.getJSONObject(i)
//
//                if (item["Name"] == searchTo) {
//                    endId = item["Id"] as Int
//                }
//            }
//
//            Log.d("type", start.toString())
//            Log.d("type", end.toString())
//
//
//            Log.d("id", startId.toString())
//            Log.d("id", endId.toString())

//
//            for (i in 0..(start.length() - 1)) {
//                val item = start.getJSONObject(i)
//            }



        } else {
            // Set header to location name
            findViewById<TextView>(R.id.location).text = intent.extras["search"].toString()
        }

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
}
