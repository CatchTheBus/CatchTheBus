package com.example.munge.app.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_settings.*


// spinner or other list instead?
//spinner: https://www.androidly.net/214/android-spinner-using-kotlin
// listView: https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
class SettingsTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set an checked change listener for switch buttons
        switch_button_gps.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Change the app background color
                linearLayout.setBackgroundColor(Color.DKGRAY)
            } else {
                // Set the app background color to light gray
                linearLayout.setBackgroundColor(Color.LTGRAY)
            }
        }
        // Set a click listener for the button widget
        switch_button_gps.setOnClickListener {
            // Change the switch button checked state on button click
            switch_button_gps.isChecked = if (switch_button_gps.isChecked) false else true
        }
    }

    // actions on click menu items (home button)
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home ->{
            when (intent.extras["prev_activity"].toString()) {
                "journey" -> startActivity(Intent(this, JourneyPlannerActivity::class.java))
                "departure" ->  startActivity(Intent(this, DeparturesActivity::class.java))
                "main" ->  startActivity(Intent(this, MainActivity::class.java))
            }
            true
            //back to main
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            //true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}
