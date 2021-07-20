package com.example.inocentemontemayorcrowdcontrol.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inocentemontemayorcrowdcontrol.R
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location

interface OnLocationItemClickListener {
    fun onClick(location: Location)
    fun onPlusClick(location: Location)
    fun onMinusClick(location: Location)
}

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.ViewHolder>
{
    class ViewHolder : RecyclerView.ViewHolder {
        var locationName : TextView? = null
        var locationNumbers : TextView? = null
        var locationContainer: RelativeLayout? = null
        var locationPlus : TextView? = null
        var locationMinus : TextView? = null

        constructor(view : View) : super(view) {
            locationContainer = view.findViewById(R.id.location_container)
            locationName = view.findViewById(R.id.location_item_name)
            locationNumbers = view.findViewById(R.id.location_numbers)
            locationPlus = view.findViewById(R.id.plus_button)
            locationMinus = view.findViewById(R.id.minus_button)
        }
    }

    private var locations : List<Location>? = null
    private var listener : OnLocationItemClickListener? = null
    private var context : Context? = null

    constructor(locations : List<Location>,
                listener : OnLocationItemClickListener,
                context : Context
    ) : super(){
        this.locations = locations
        this.listener = listener
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.location_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations!![position]

        holder.locationName!!.text = location.name
        holder.locationNumbers!!.text = "${location.currAttendance}/${location.maxCapacity}"

        holder.locationContainer!!.setOnClickListener {
            listener!!.onClick(locations!![position])
        }

        holder.locationMinus!!.setOnClickListener {
            listener!!.onMinusClick(locations!![position])
        }

        holder.locationPlus!!.setOnClickListener {
            listener!!.onPlusClick(locations!![position])
        }
    }

    override fun getItemCount(): Int {
        return locations!!.size;
    }
}