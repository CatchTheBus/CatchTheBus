package com.example.munge.app.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.example.munge.app.R
import android.widget.ArrayAdapter




class JourneyPlannerActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prev_activity"
    private val INTENT_SEARCH_FROM = "search_from"
    private val INTENT_SEARCH_TO = "search_to"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey_planner)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val searchButton = findViewById<Button>(R.id.search_journey)

        searchButton.setOnClickListener { changeToDestinations() }

//        val adapter = ArrayAdapter<String>(this,
//                android.R.layout.activity_list_item, COUNTRIES)
//        findViewById<AutoCompleteTextView>(R.id.search_from).setAdapter<ArrayAdapter<String>>(adapter)

    }

//    private val COUNTRIES = arrayOf("Belgium", "France", "Italy", "Germany", "Spain")


    private fun changeToDestinations() {
        val formInputFrom = findViewById<AutoCompleteTextView>(R.id.search_from).text.toString()
        val formInputTo = findViewById<AutoCompleteTextView>(R.id.search_to).text.toString()

        if (formInputFrom.isNotEmpty() && formInputTo.isNotEmpty()) {
            val intent = Intent(this, DestinationActivity::class.java)
            intent.putExtra(INTENT_PREV_ACTIVITY, "journey")
            intent.putExtra(INTENT_SEARCH_FROM,  formInputFrom)
            intent.putExtra(INTENT_SEARCH_TO,  formInputTo)
            startActivity(intent)
        } else {
            Toast.makeText(this, "From and To cannot be empty", Toast.LENGTH_LONG).show()
        }
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
