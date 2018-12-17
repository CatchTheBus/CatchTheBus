package com.example.munge.app.activities

import android.content.Context
import android.content.Intent
import android.support.constraint.R.id.parent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.munge.app.R
import kotlinx.android.synthetic.main.bus_list_item.view.*

private val INTENT_PREV_ACTIVITY = "prev_activity"
private val INTENT_BUS = "bus"
private val INTENT_TIME = "time"
private val INTENT_INFORMATION = "information"

class BusAdapter(val items : ArrayList<String>, val items2 : ArrayList<String>, val items3 : ArrayList<String>, val information : HashMap<String, String>  ,val context: Context) : RecyclerView.Adapter<ViewHolder>() {

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
        holder?.tvItems?.setOnClickListener { changeToCountdown(holder.tvItems?.text.toString(), holder.tvItems3.text.toString()) }
        holder?.tvItems2?.setOnClickListener { changeToCountdown(holder.tvItems?.text.toString(), holder.tvItems3.text.toString()) }
        holder?.tvItems3?.setOnClickListener { changeToCountdown(holder.tvItems?.text.toString(), holder.tvItems3.text.toString()) }

//        Log.d("information", information.toString())
    }

    fun changeToCountdown(busLine: String, depTime: String) {
        val intent = Intent(context, CountdownActivity::class.java)
        intent.putExtra(INTENT_PREV_ACTIVITY, "destinations")
        intent.putExtra(INTENT_BUS, busLine)
        intent.putExtra(INTENT_TIME, depTime)
        intent.putExtra(INTENT_INFORMATION, information)
        context.startActivity(intent)
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvItems = view.bus_line
    val tvItems2 = view.bus_to
    val tvItems3 = view.bus_ttd
}