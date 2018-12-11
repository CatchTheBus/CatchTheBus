package com.example.munge.app.activities

import android.os.Bundle
import android.view.Menu
import com.example.munge.app.R
import android.graphics.Color
import kotlinx.android.synthetic.main.activity_settings.*



// https://android--code.blogspot.com/2018/04/android-kotlin-countdowntimer-days.html
class AppSettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Set an checked change listener for switch button
        switch_button.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked
                val text_view.text = "Switch on"

                // Change the app background color
                root_layout.setBackgroundColor(Color.GREEN)
            } else {
                // The switch is disabled
                val text_view.text = "Switch off"

                // Set the app background color to light gray
                root_layout.setBackgroundColor(Color.LTGRAY)
            }
        }


        // Set a click listener for root layout object
        root_layout.setOnClickListener{
            // Get the switch button state programmatically
            if(switch_button.isChecked){
                // If switch button is checked/on then
                // The switch is enabled/checked
                val text_view.text = "Switch is on"

                // Change the app background color
                root_layout.setBackgroundColor(Color.GREEN)
            }else{
                // The switch is unchecked
                val text_view.text = "Switch is off"

                // Set the app background color to light gray
                root_layout.setBackgroundColor(Color.LTGRAY)
            }
        }


        // Set a click listener for the button widget
        button.setOnClickListener{
            // Change the switch button checked state on button click
            switch_button.isChecked = if(switch_button.isChecked)false else true
        }
    }
}
