package com.example.munge.app.activities

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.munge.app.R
import kotlinx.android.synthetic.main.bus_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private val INTENT_PREV_ACTIVITY = "prevActivity"
private val INTENT_BUS = "bus"
private val INTENT_TIME = "time"
private val INTENT_INFORMATION = "information"

class BusAdapter(val items : ArrayList<String>, val items2 : ArrayList<String>, val items3 : ArrayList<String>, val information : HashMap<String, String>  ,val context: Context, val prevActivity: String) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.bus_list_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.tvItems?.text = items.get(position)
        holder?.tvItems2?.text = items2.get(position)
        holder?.tvItems3?.text = items3.get(position)
        val timeString = holder?.tvItems3?.text.toString()
        holder?.tvItems?.setOnClickListener { changeToCountdown(holder.tvItems?.text.toString(), timeString, holder.tvItems2?.text.toString()) }
        holder?.tvItems2?.setOnClickListener { changeToCountdown(holder.tvItems?.text.toString(), timeString, holder.tvItems2?.text.toString()) }
        holder?.tvItems3?.setOnClickListener { changeToCountdown(holder.tvItems?.text.toString(), timeString, holder.tvItems2?.text.toString()) }
        holder?.tvItems3?.text = timeStringEven(getTime(holder?.tvItems3?.text.toString()))
    }

    fun changeToCountdown(busLine: String, depTime: String, destination: String) {
        if (prevActivity == "journeys") {
            val intent = Intent(context, CountdownActivity::class.java)
            intent.putExtra(INTENT_PREV_ACTIVITY, "journeys")
            intent.putExtra(INTENT_BUS, busLine)
            intent.putExtra(INTENT_TIME, depTime)
            intent.putExtra(INTENT_INFORMATION, information)
            context.startActivity(intent)
        } else if (prevActivity == "departures") {
            val intent = Intent(context, CountdownActivity::class.java)
            intent.putExtra(INTENT_PREV_ACTIVITY, "departures")
            intent.putExtra(INTENT_BUS, busLine)
            intent.putExtra(INTENT_TIME, depTime)
            information["searchTo"] = destination
            intent.putExtra(INTENT_INFORMATION, information)
            context.startActivity(intent)
        }
    }

    private fun getTime(depTime: String): Long {
        val currentTime = Calendar.getInstance().getTime()
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = format.parse(depTime)
        val millisInFuture = date.time - currentTime.time
        return millisInFuture
    }

    private fun timeStringEven(millisUntilFinished:Long):String{
        val millisTotal = millisUntilFinished
        var millisUntilFinished:Long = millisUntilFinished

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
                        "%2d h %2d min",
                        hours, minutes
                )
            } else if (millisTotal > 60000) {
                notificationString = String.format(
                        Locale.getDefault(),
                        "%2d min",
                        minutes
                )
            } else {
                notificationString = String.format(
                        Locale.getDefault(),
                        "< 1 min",
                        seconds
                )
            }
        }
        return notificationString
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvItems = view.bus_line
    val tvItems2 = view.bus_to
    val tvItems3 = view.bus_ttd
}