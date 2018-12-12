package com.example.munge.app.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import com.example.munge.app.R
import android.graphics.Color
import android.view.View
import android.widget.*
import com.example.munge.app.R.id.*
import kotlinx.android.synthetic.main.activity_settings.*



class AppSettingsActivity : AppCompatPreferenceActivity(), AdapterView.OnItemSelectedListener {

    // Drop down notifications vars
    var notification_alert_choice = arrayOf("notifications off", "5 min", "10 min", "20 min", "30 min")
    var spinner:Spinner? = null
    var textView_msg: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //val listView = findViewById<View>(R.id.list) as ListView
        // Set an checked change listener for switch buttons
        switch_button_gps.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Change the app background color
                root_layout.setBackgroundColor(Color.DKGRAY)
            } else {
                // Set the app background color to light gray
                root_layout.setBackgroundColor(Color.LTGRAY)
            }
        }
        // Set a click listener for the button widget
        switch_button_gps.setOnClickListener{
            // Change the switch button checked state on button click
            switch_button_gps.isChecked = if(switch_button_gps.isChecked)false else true
        }


        // Drop down notifications
        //https://www.tutorialkart.com/kotlin-android/android-spinner-kotlin-example/
        //https://tutorialwing.com/android-spinner-using-kotlin-with-example/
        textView_msg = this.spinner_msg

        spinner = this.notifications_spinner
        spinner!!.setOnItemSelectedListener(this)

        //val listView = findViewById<View>(R.id.list) as ListView

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, notification_alert_choice)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
            spinner!!.setAdapter(aa)
            //listView.setAdapter(aa)


    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        textView_msg!!.text = "notifications interval: "+notification_alert_choice[position]
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}
