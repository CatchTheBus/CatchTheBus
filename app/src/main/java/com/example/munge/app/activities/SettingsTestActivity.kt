package com.example.munge.app.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_settings.*


// spinner or other list instead?
//spinner: https://www.androidly.net/214/android-spinner-using-kotlin
// listView: https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
class SettingsTestActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var notificationTime = arrayOf("30 sec", "1 min", "5 min", "10 min", "20 min", "30 min")
    val NEW_SPINNER_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set an checked change listener for switch buttons
        switchButtonGps.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Change the app background color
                //linearLayout.setBackgroundColor(Color.DKGRAY)
            } else {
                // Set the app background color to light gray
                linearLayout.setBackgroundColor(Color.LTGRAY)
            }
        }
        // Set a click listener for the button widget
        switchButtonGps.setOnClickListener {
            // Change the switch button checked state on button click
            switchButtonGps.isChecked = if (switchButtonGps.isChecked) false else true
        }
        // Notification button
        switchButtonNotificatios.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Change the app background color
                //linearLayout.setBackgroundColor(Color.DKGRAY)
            } else {
                // Set the app background color to light gray
                linearLayout.setBackgroundColor(Color.LTGRAY)
            }
        }
        // Set a click listener for the button widget
        switchButtonNotificatios.setOnClickListener {
            // Change the switch button checked state on button click
            switchButtonNotificatios.isChecked = if (switchButtonNotificatios.isChecked) false else true
        }

        // Spinner
        var aa = ArrayAdapter(this@SettingsTestActivity, android.R.layout.simple_spinner_item, notificationTime)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(notificationSpinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@SettingsTestActivity
            prompt = "Select notification interval"
            gravity = Gravity.CENTER

        }

        val spinner = Spinner(this)
        spinner.id = NEW_SPINNER_ID

        val ll = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        ll.setMargins(10, 40, 10, 10)
        linearLayout.addView(spinner)

        aa = ArrayAdapter(this@SettingsTestActivity, R.layout.spinner_right_aligned, notificationTime)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)

        with(spinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@SettingsTestActivity
            layoutParams = ll
            prompt = "Select notification interval"
            setPopupBackgroundResource(R.color.material_grey_600)

        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }

    //Spinner: display selected result
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (view?.id) {
            1 -> showToast(message = "Spinner 2 Position:${position} and language: ${notificationTime[position]}")
            else -> {
                showToast(message = "Spinner 1 Position:${position} and language: ${notificationTime[position]}")
            }
        }
    }
    //Spinner: create messages for toastmessage
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

    // actions on click home button, send back to previous page
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home ->{
            when (intent.extras["prevActivity"].toString()) {
                "journey" -> startActivity(Intent(this, JourneyPlannerActivity::class.java))
                "departure" ->  startActivity(Intent(this, DeparturesActivity::class.java))
                "destination" ->  startActivity(Intent(this, DestinationJourneyActivity::class.java))
                //"countdown" ->  startActivity(Intent(this, CountdownActivity::class.java))
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
