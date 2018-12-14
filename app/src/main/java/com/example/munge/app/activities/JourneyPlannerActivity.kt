package com.example.munge.app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.example.munge.app.R
import org.json.JSONObject


class JourneyPlannerActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prev_activity"
    private val INTENT_SEARCH_FROM = "search_from"
    private val INTENT_SEARCH_TO = "search_to"
    private val INTENT_SEARCH_FROM_ID = "search_from_id"
    private val INTENT_SEARCH_TO_ID = "search_to_id"
    private val names: MutableList<String> = ArrayList()
    private var invalidResult = false
    private val idsFrom = mutableMapOf<Any, Any>()
    private val idsTo = mutableMapOf<Any, Any>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey_planner)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //search
        val searchButton = findViewById<Button>(R.id.search_journey)
        searchButton.setOnClickListener { changeToDestinations() }

        val searchFromJourney = findViewById<View>(R.id.search_from_journey) as AutoCompleteTextView
        val searchToJourney = findViewById<View>(R.id.search_to_journey) as AutoCompleteTextView

        searchFromJourney.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 1) {
                    names.clear()
                    idsFrom.clear()
                    val url = "https://www.skanetrafiken.se/handlers/LocationSearch.ashx?action=search&q=$s"
                    val newUrl: String = url.replace("ö", "%C3%B6").replace("ä", "%C3%A4").replace("å", "%C3%A5").replace(" ", "%20")
                    val data = GetAPIData("journey").execute(newUrl).get()[0]
                    val stops = JSONObject(data.toString()).getJSONArray("StartEndPoint")

                    for (i in 0..(stops.length() - 1)) {
                        val stop = stops.getJSONObject(i)
                        names.add(stop["Name"] as String)
                        idsFrom[stop["Name"]] = stop["Id"]
                    }

                    val adapter = ArrayAdapter(this@JourneyPlannerActivity, android.R.layout.select_dialog_item, names)

                    searchFromJourney.threshold = 1//will start working from first character
                    searchFromJourney.setAdapter(adapter)//setting the adapter data into the AutoCompleteTextView
                    searchFromJourney.setTextColor(Color.RED)
                }
            }
        })

        searchToJourney.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 1) {
                    names.clear()
                    idsTo.clear()
                    val url = "https://www.skanetrafiken.se/handlers/LocationSearch.ashx?action=search&q=$s"
                    val newUrl: String = url.replace("ö", "%C3%B6").replace("ä", "%C3%A4").replace("å", "%C3%A5").replace(" ", "%20")
                    val data = GetAPIData("journey").execute(newUrl).get()[0]
                    val stops = JSONObject(data.toString()).getJSONArray("StartEndPoint")

                    for (i in 0..(stops.length() - 1)) {
                        val stop = stops.getJSONObject(i)
                        names.add(stop["Name"] as String)
                        idsTo[stop["Name"]] = stop["Id"]
                    }

                    val adapter = ArrayAdapter(this@JourneyPlannerActivity, android.R.layout.select_dialog_item, names)

                    searchToJourney.threshold = 1//will start working from first character
                    searchToJourney.setAdapter(adapter)//setting the adapter data into the AutoCompleteTextView
                    searchToJourney.setTextColor(Color.RED)
                }
            }
        })
    }

    private fun changeToDestinations() {
        val formInputFrom = findViewById<AutoCompleteTextView>(R.id.search_from_journey).text.toString()
        val formInputTo = findViewById<AutoCompleteTextView>(R.id.search_to_journey).text.toString()

        val formIdFrom = idsFrom[formInputFrom]
        val formIdTo = idsTo[formInputTo]

        if (!names.contains(formInputFrom) && !names.contains(formInputTo)) {
            invalidResult = true
        }

        if (formInputFrom.isNotEmpty() && formInputTo.isNotEmpty() && !invalidResult) {
            val intent = Intent(this, DestinationActivity::class.java)
            intent.putExtra(INTENT_PREV_ACTIVITY, "journey")
            intent.putExtra(INTENT_SEARCH_FROM,  formInputFrom)
            intent.putExtra(INTENT_SEARCH_TO,  formInputTo)
            intent.putExtra(INTENT_SEARCH_FROM_ID, formIdFrom.toString())
            intent.putExtra(INTENT_SEARCH_TO_ID, formIdTo.toString())
            startActivity(intent)
        } else {
            Toast.makeText(this, "Check input", Toast.LENGTH_LONG).show()
        }
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun changeToSettings() {
        val intent = Intent(this, SettingsTestActivity::class.java)
        intent.putExtra(INTENT_PREV_ACTIVITY, "journey")
        startActivity(intent)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // open settings page
            changeToSettings()
            true
        }
        android.R.id.home ->{
//            val activityToStart = intent.extras["prev_activity"].toString().split(" ").last()
//            val c = Class.forName(activityToStart)
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
