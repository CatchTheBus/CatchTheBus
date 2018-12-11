package com.example.munge.app.activities

import android.content.Context
import android.support.constraint.R.id.parent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.munge.app.R
import kotlinx.android.synthetic.main.bus_list_item.view.*

class BusAdapter(val items : ArrayList<String>, val items2 : ArrayList<String>, val items3 : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

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
        holder?.tvItems?.setOnClickListener( { Log.d("shit", "hej") } )
        holder?.tvItems2?.setOnClickListener( { Log.d("shit", "hej") } )
        holder?.tvItems3?.setOnClickListener( { Log.d("shit", "hej") } )
//        Log.d("shit", holder?.tvItems.toString())
//        holder?.tvItems?.parent?.parent.toString()
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvItems = view.bus_line
    val tvItems2 = view.bus_to
    val tvItems3 = view.bus_ttd
}