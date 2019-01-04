package com.example.munge.app.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsTestActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var notificationTime = arrayOf("1 min", "5 min", "10 min", "20 min", "30 min")
    val NEW_SPINNER_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("com.example.munge.app.settings", Context.MODE_PRIVATE)

        setContentView(R.layout.activity_settings)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (sharedPreferences.contains("notifications")) {
            switchButtonNotificatios.isChecked = sharedPreferences.getBoolean("notifications", true)
            if (sharedPreferences.getBoolean("notifications", true)) {
                switchButtonNotificatios.text = "Notifications ON"
            } else {
                switchButtonNotificatios.text = "Notifications OFF"
            }
        } else {
            switchButtonNotificatios.isChecked = true
            val editor = sharedPreferences.edit()
            editor.putBoolean("notifications", true)
            editor.apply()
            switchButtonNotificatios.text = "Notifications ON"
        }

        /*// Set an checked change listener for switch buttons
        switchButtonGps.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Change the app background color
                //linearLayout.setBackgroundColor(Color.DKGRAY)
            } else {
                // Set the app background color to light gray
                //linearLayout.setBackgroundColor(Color.LTGRAY)
            }
        }

        // Set a click listener for the button widget
        switchButtonGps.setOnClickListener {
            // Change the switch button checked state on button click
            switchButtonGps.isChecked = if (switchButtonGps.isChecked) false else true
        }*/

        // Notification on off button
        switchButtonNotificatios.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val editor = sharedPreferences.edit()
                editor.putBoolean("notifications", true)
                editor.apply()
                switchButtonNotificatios.text = "Notifications ON"
                // Change the app background color
                //linearLayout.setBackgroundColor(Color.DKGRAY)
            } else {
                val editor = sharedPreferences.edit()
                editor.putBoolean("notifications", false)
                editor.apply()
                switchButtonNotificatios.text = "Notifications OFF"
                // Set the app background color to light gray
                //linearLayout.setBackgroundColor(Color.LTGRAY)
            }
        }

        // Set a click listener for the button widget
        switchButtonNotificatios.setOnClickListener {
            // Change the switch button checked state on button click
            // Unnecessary
//            switchButtonNotificatios.isChecked = !switchButtonNotificatios.isChecked
        }

        //SPINNER:
        val spinner = Spinner(this)
        spinner.id = NEW_SPINNER_ID
        spinner.elevation = 2.0F
        spinner.setBackgroundColor(Color.parseColor("#ffffff"))

        val ll = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        ll.setMargins(0, 0, 0, 0)
        val textview = TextView(this)
        textview.text = "Notification interval"
//        textview.setTextColor(Color.parseColor("#000000"))
//        textview.textSize = 16.0F
//        textview.gravity = Gravity.CENTER
        linearLayout.addView(textview)
        linearLayout.addView(spinner)

        val aa = ArrayAdapter(this@SettingsTestActivity, R.layout.spinner_right_aligned, notificationTime)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)

        val intervalString: String
        var settingPosition = 0

        if (sharedPreferences.contains("interval")) {
            val setting = sharedPreferences.getInt("interval", 0)
            intervalString = "${setting} min"
            settingPosition = notificationTime.indexOf(intervalString)
        }

        with(spinner)
        {
            adapter = aa
            setSelection(settingPosition, false)
            onItemSelectedListener = this@SettingsTestActivity
            layoutParams = ll
            prompt = "Select notification interval"
            setPopupBackgroundResource(R.color.colorAccent)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        showToast(this, "Nothing selected")
    }

    //Spinner: when item is selected; display selected result, send time to CountDownActivity
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        //send to CountdownActivity ${notificationTime[position]}
        //make new get selected time fun for countdownactivity?

        when (view?.id) {
            1 -> showToast(this, "Notification interval: ${notificationTime[position]}")
            //else -> showToast(this, "Spinner 1 Position:${position} notification interval: ${notificationTime[position]}")
        }

        val sharedPreferences = getSharedPreferences("com.example.munge.app.settings", Context.MODE_PRIVATE)
        val intervalTime = notificationTime[position].replace(" min", "")
        val editor = sharedPreferences.edit()
        editor.putInt("interval", intervalTime.toInt())
        editor.apply()
    }

    //Spinner: create messages for toastmessage
    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

    //HOME button
    // actions on click home button, send back to previous page
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home ->{
            when (intent.extras["prevActivity"].toString()) {
                "journey" -> startActivity(Intent(this, JourneyPlannerActivity::class.java))
                "departure" ->  startActivity(Intent(this, DeparturesActivity::class.java))
                "destination" ->  startActivity(Intent(this, DestinationJourneyActivity::class.java))
                "main" ->  startActivity(Intent(this, MainActivity::class.java))
                "destinationDeparture" -> startActivity(Intent(this, DestinationDepartureActivity::class.java))
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
