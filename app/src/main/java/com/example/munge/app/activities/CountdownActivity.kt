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
    private val departures: ArrayList<String> = ArrayList()
    private var depIndex: Int = 0
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

        notificationManager =
                getSystemService(
                        Context.NOTIFICATION_SERVICE) as NotificationManager

        val countDownInterval: Long = 1000
        var countDown = timer(getTime(), countDownInterval)
        countDown.start()

        val interval: Long = 10000

        fun startTimer() = notificationTimer.schedule(object : TimerTask() {
            override fun run() {
                if (getTime() > 0) {
                    sendNotification(timeStringEven(getTime()))
                } else {
                    Log.d("countdown", "cancel timer")
                    notificationTimer.cancel()
                }
            }
        }, interval, interval)

        startTimer()

        findViewById<TextView>(R.id.stop_timer).setOnClickListener {
            notificationTimer.cancel()
            notificationTimer.purge()
            isCancelled = true
        }
        findViewById<TextView>(R.id.next_bus).setOnClickListener {
            if (!isCancelled) {
                countDown.cancel()
                notificationTimer.cancel()
                notificationTimer.purge()
                notificationTimer = Timer()
                startTimer()
            } else if (isCancelled) {
                notificationTimer = Timer()
                startTimer()
            }
            if (departures.size > depIndex + 1) {
                depIndex += 1
            } else {
                next_bus.isEnabled = false
            }
            isCancelled = false
            countDown = timer(getTime(), countDownInterval)
            countDown.start()
        }
    }

    private fun sendNotification(contentText: String) {
        val notificationID = 101
        val resultIntent = Intent(this, CountdownActivity::class.java)
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

    private fun getTime(): Long {
        if (departures.size == 0) {
            fun addTimes() {
                departures.add("2018-17-13T21:00:00")
                departures.add("2018-17-13T21:10:00")
                departures.add("2018-17-13T21:20:00")
                departures.add("2018-17-13T21:30:00")
            }
            addTimes()
        }
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
            when (intent.extras["prev_activity"].toString()) {
                "destinations" -> startActivity(Intent(this, MainActivity::class.java))
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
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager?.cancelAll()
    }
}
