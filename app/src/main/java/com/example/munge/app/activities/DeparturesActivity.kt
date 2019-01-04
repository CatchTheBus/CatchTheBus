package com.example.munge.app.activities


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.munge.app.R
import org.json.JSONObject


class DeparturesActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prevActivity"
    private val INTENT_SEARCH = "search"
    private val INTENT_SEARCH_FROM_ID = "search_from_id_departures"
    private val names: MutableList<String> = ArrayList()
    private var invalidResult = false
    private val idsFrom = mutableMapOf<Any, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departures)

        //gps button
//        val imageButton = findViewById<ImageButton>(R.id.imageButton)
//        imageButton?.setOnClickListener { Toast.makeText(this, R.string.image_button_clicked, Toast.LENGTH_SHORT).show() }

        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val searchButton = findViewById<Button>(R.id.search_departure)

        val searchFromDeparture = findViewById<View>(R.id.search_from_departure) as AutoCompleteTextView

        searchButton.setOnClickListener { changeToDestinations() }

        searchFromDeparture.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 1 || s.length > 1) {
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

                    val adapter = ArrayAdapter(this@DeparturesActivity, android.R.layout.select_dialog_item, names)

                    searchFromDeparture.threshold = 1//will start working from first character
                    searchFromDeparture.setAdapter(adapter)//setting the adapter data into the AutoCompleteTextView
                    searchFromDeparture.setTextColor(Color.RED)
                }
            }
        })

    }

    private fun changeToDestinations() {
        val formInput = findViewById<AutoCompleteTextView>(R.id.search_from_departure).text.toString()
        val formIdFrom = idsFrom[formInput]

        invalidResult = !names.contains(formInput)

        if (formInput.isNotEmpty() && !invalidResult) {
            val intent = Intent(this, DestinationDepartureActivity::class.java)
            intent.putExtra(INTENT_PREV_ACTIVITY, "departure")
            intent.putExtra(INTENT_SEARCH, formInput)
            intent.putExtra(INTENT_SEARCH_FROM_ID, formIdFrom.toString())
            startActivity(intent)
        } else {
            Toast.makeText(this, "From cannot be empty", Toast.LENGTH_LONG).show()
        }
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // open settings from departures
    private fun changeToSettings() {
        val intent = Intent(this, SettingsTestActivity::class.java)
        intent.putExtra(INTENT_PREV_ACTIVITY, "departure")
        startActivity(intent)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // go to settings
            changeToSettings()
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



