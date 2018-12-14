package com.example.munge.app.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.munge.app.R


class JourneyPlannerActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prev_activity"
    private val INTENT_SEARCH = "search"

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
    }


    private fun changeToDestinations() {
        val intent = Intent(this, DestinationActivity::class.java)
        intent.putExtra(INTENT_PREV_ACTIVITY, "journey")
        intent.putExtra(INTENT_SEARCH, findViewById<TextInputEditText>(R.id.search_from).getText().toString())
        startActivity(intent)
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
