package com.example.munge.app.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.munge.app.R
import kotlinx.android.synthetic.main.activity_countdown.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class CountdownActivity : AppCompatActivity() {

    private var isCancelled = false
    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        //setting toolbar
        //setSupportActionBar(findViewById(R.id.toolbar))

        //home navigation
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.countdown_header).text = intent.extras["bus"].toString()

        val countDownInterval:Long = 1000

        timer(getTime(), countDownInterval).start()

        isCancelled = false

        // Notification
        notificationManager =
                getSystemService(
                        Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
                "com.example.munge.app",
                "Catch The Bus",
                "Time until bus leaves")

        val timer = Timer()
        val interval: Long = 300000

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (getTime() > 0) {
                    sendNotification(timeStringEven(getTime()))
                } else {
                    Log.d("countdown", "cancel timer")
                    timer.cancel()
                }
            }
        }, interval, interval)
    }

    private fun sendNotification(contentText: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationID = 101

            val channelID = "com.example.munge.app"

            val notification = Notification.Builder(this@CountdownActivity,
                    channelID)
                    .setContentTitle("Time until departure")
                    .setContentText(contentText)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setChannelId(channelID)
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
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun getTime(): Long {
        val currentTime = Calendar.getInstance().getTime()

        val dtStart = "2018-12-12T14:13:00"
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = format.parse(dtStart)
        val millisInFuture = date.time - currentTime.time
        return millisInFuture
    }

    // Method to configure and return an instance of CountDownTimer object
    private fun timer(millisInFuture:Long,countDownInterval:Long):CountDownTimer{
        val text_view = findViewById<TextView>(R.id.countdown_view)
        return object: CountDownTimer(millisInFuture,countDownInterval){
            override fun onTick(millisUntilFinished: Long){
                val timeRemaining = timeString(millisUntilFinished)
                if (isCancelled){
                    text_view.text = "${text_view.text}\nStopped.(Cancelled)"
                    cancel()
                } else {
                    text_view.text = timeRemaining
                }
            }

            override fun onFinish() {
                text_view.text = "The bus left"
                sendNotification("The bus left")
            }
        }
    }

    private fun timeString(millisUntilFinished:Long):String{
        var millisUntilFinished:Long = millisUntilFinished
//        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
//        millisUntilFinished -= TimeUnit.DAYS.toMillis(days)

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

        Log.d("countdown", millisTotal.toString())

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

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Print" item
            Toast.makeText(this,"Settings", Toast.LENGTH_LONG).show()
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
