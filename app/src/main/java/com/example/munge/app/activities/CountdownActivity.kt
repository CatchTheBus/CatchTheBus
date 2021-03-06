package com.example.munge.app.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_countdown.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class CountdownActivity : AppCompatActivity() {

    private val INTENT_PREV_ACTIVITY = "prevActivity"
    private var isCancelled = false
    private var notificationManager: NotificationManager? = null
    private val departures: ArrayList<String> = ArrayList()
    private var depIndex: Int = 0
    private val INTENT_SEARCH_FROM = "search_from"
    private val INTENT_SEARCH_TO = "search_to"
    private val INTENT_SEARCH_FROM_ID = "search_from_id"
    private val INTENT_SEARCH_TO_ID = "search_to_id"
    private val INTENT_SEARCH_FROM_ID_DEPARTURES = "search_from_id_departures"
    private val INTENT_SEARCH_FROM_DEPARTURES_NAME = "search"
    private var notificationTimer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        //setting toolbar
        //setSupportActionBar(findViewById(R.id.toolbar))

        //home navigation
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.countdown_header).text = intent.extras["bus"].toString()

        val depTime = intent.extras["time"].toString()
        val hashMapObject = intent.getSerializableExtra("information") as HashMap<String, String>

        if (hashMapObject["searchFrom"] == null) {
            findViewById<TextView>(R.id.location).text = hashMapObject["search"] + " → " + hashMapObject["searchTo"]
        } else {
            findViewById<TextView>(R.id.location).text = hashMapObject["searchFrom"] + " → " + hashMapObject["searchTo"]
        }

        isCancelled = false

        notificationManager =
                getSystemService(
                        Context.NOTIFICATION_SERVICE) as NotificationManager

        val countDownInterval: Long = 1000
        var countDown = timer(getTime(depTime), countDownInterval)
        countDown.start()

        var interval: Long = 60000

        val sharedPreferences = getSharedPreferences("com.example.munge.app.settings", Context.MODE_PRIVATE)

        if (sharedPreferences.contains("interval")) {
            val milliSeconds = sharedPreferences.getInt("interval", 0) * 60000
            val setting = milliSeconds.toLong()
            Log.d("countdown", setting.toString())
            interval = setting
        } else {
            Log.d("countdown", "not set")
        }

        fun startTimer() = notificationTimer.schedule(object : TimerTask() {
            override fun run() {
                if (getTime(depTime) > 0) {
                    sendNotification(timeStringEven(getTime(depTime)))
                } else {
                    Log.d("countdown", "cancel timer")
                    notificationTimer.cancel()
                }
            }
        }, interval, interval)

        if (sharedPreferences.contains("notifications")) {
            if (sharedPreferences.getBoolean("notifications", true)) {
                if (getTime(depTime) > 0) {
                    sendNotification(timeStringEven(getTime(depTime)))
                }
                startTimer()
            }
        } else {
            startTimer()
            val editor = sharedPreferences.edit()
            editor.putBoolean("notifications", true)
            editor.apply()
        }

        next_bus.isEnabled = false

        findViewById<TextView>(R.id.stop_timer).setOnClickListener {
            notificationTimer.cancel()
            notificationTimer.purge()
            notificationManager?.cancelAll()
            isCancelled = true
            stop_timer.isEnabled = false
            next_bus.isEnabled = true
        }
        findViewById<TextView>(R.id.next_bus).setOnClickListener {
            notificationTimer = Timer()
            startTimer()
            stop_timer.isEnabled = true
            next_bus.isEnabled = false
            isCancelled = false
            countDown = timer(getTime(depTime), countDownInterval)
            countDown.start()
        }
    }

    private fun sendNotification(contentText: String) {
        val notificationID = 101
        val resultIntent = Intent(this, CountdownActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                    "com.example.munge.app",
                    "Catch The Bus",
                    "Time until bus leaves")
            val channelID = "com.example.munge.app"

            val notification = Notification.Builder(this@CountdownActivity,
                    channelID)
                    .setContentTitle("Time until departure")
                    .setContentText(contentText)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setChannelId(channelID)
                    .setContentIntent(pendingIntent)
                    .build()

            notificationManager?.notify(notificationID, notification)
        } else {
            val notification = Notification.Builder(this@CountdownActivity)
                    .setContentTitle("Time until departure")
                    .setContentText(contentText)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setVibrate(longArrayOf(750, 500, 400))
                    .setLights(Color.YELLOW, 3000, 3000)
                    .setContentIntent(pendingIntent)
                    .build()

            notificationManager?.notify(notificationID, notification)
        }
    }

    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)

            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.YELLOW
            channel.enableVibration(true)
            channel.vibrationPattern =
                    longArrayOf(750, 500, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun getTime(depTime: String): Long {
        departures.add(depTime)
        val departureTime = departures.get(depIndex)
        val currentTime = Calendar.getInstance().getTime()
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = format.parse(departureTime)
        val millisInFuture = date.time - currentTime.time
        return millisInFuture
    }

    // Method to configure and return an instance of CountDownTimer object
    private fun timer(millisInFuture:Long,countDownInterval:Long):CountDownTimer {
        val text_view = findViewById<TextView>(R.id.countdown_view)
        return object: CountDownTimer(millisInFuture,countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = timeString(millisUntilFinished)
                if (isCancelled){
                    text_view.text = "${text_view.text}\nStopped"
                    cancel()
                } else {
                    text_view.text = timeRemaining
                }
            }

            override fun onFinish() {
                text_view.text = "UNLUCKY"
                if (!isCancelled) {
                    sendNotification("Unlucky, you missed your departure")
                }
            }
        }
    }

    private fun timeString(millisUntilFinished:Long):String{
        var millisUntilFinished:Long = millisUntilFinished

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours, minutes, seconds
        )
    }

    private fun timeStringEven(millisUntilFinished:Long):String{
        val millisTotal = millisUntilFinished
        var millisUntilFinished:Long = millisUntilFinished
//        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
//        millisUntilFinished -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        var notificationString = ""

        if (millisTotal > 0) {
            if (millisTotal > 3600000) {
                notificationString = String.format(
                        Locale.getDefault(),
                        "%2d hour(s) %2d minute(s)",
                        hours, minutes
                )
            } else if (millisTotal > 60000) {
                notificationString = String.format(
                        Locale.getDefault(),
                        "%2d minute(s)",
                        minutes
                )
            } else {
                notificationString = String.format(
                        Locale.getDefault(),
                        "%2d second(s)",
                        seconds
                )
            }
        }
        return notificationString
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
            when (intent.extras["prevActivity"].toString()) {
                "journeys" -> {
                    val intentPrev = intent
                    val hashMapObject = intentPrev.getSerializableExtra("information") as HashMap<String, String>
                    val intent = Intent(this, DestinationJourneyActivity::class.java)
                    intent.putExtra(INTENT_PREV_ACTIVITY, "journey")
                    intent.putExtra(INTENT_SEARCH_FROM,  hashMapObject["searchFrom"])
                    intent.putExtra(INTENT_SEARCH_TO,  hashMapObject["searchTo"])
                    intent.putExtra(INTENT_SEARCH_FROM_ID, hashMapObject["searchFromId"])
                    intent.putExtra(INTENT_SEARCH_TO_ID, hashMapObject["searchToId"])
                    startActivity(intent)
                }
                "departures" -> {
                    val intentPrev = intent
                    val hashMapObject = intentPrev.getSerializableExtra("information") as HashMap<String, String>
                    val intent = Intent(this, DestinationDepartureActivity::class.java)
                    intent.putExtra(INTENT_PREV_ACTIVITY, "departure")
                    intent.putExtra(INTENT_SEARCH_FROM_ID_DEPARTURES,  hashMapObject["search_from_id_departures"])
                    intent.putExtra(INTENT_SEARCH_FROM_DEPARTURES_NAME,  hashMapObject["search"])
                    startActivity(intent)
                }
            }
            notificationTimer.cancel()
            notificationTimer.purge()
            notificationManager?.cancelAll()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        notificationTimer.cancel()
        notificationTimer.purge()
        notificationManager?.cancelAll()
        isCancelled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager?.cancelAll()
        isCancelled = true
    }
}
