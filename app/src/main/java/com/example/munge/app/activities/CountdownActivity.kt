package com.example.munge.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.munge.app.R


class CountdownActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prev_activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        //setting toolbar
        //setSupportActionBar(findViewById(R.id.toolbar))

        //home navigation
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun changeToSettings() {
        val intent = Intent(this, SettingsTestActivity::class.java)
        intent.putExtra(INTENT_PREV_ACTIVITY, "countdown")
        startActivity(intent)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            //go to settings
            changeToSettings()
            true
        }
        android.R.id.home ->{
            val activityToStart = intent.extras["prev_activity"].toString().split(" ").last()
            val c = Class.forName(activityToStart)
            val intent = Intent(this, c)
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
