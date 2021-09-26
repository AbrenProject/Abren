package com.example.abren.viewmodel
//
//import android.R
//
//import android.view.LayoutInflater
//import android.view.View
//
//import android.view.ViewGroup
//
//import android.widget.TextView
//
//import androidx.recyclerview.widget.RecyclerView
//import com.example.abren.models.Location
//
//
//class LocationAdapter :RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {
//    private var mLocation: ArrayList<Location>? = null
//
//    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        lateinit var placeName: TextView
//
//
//        init {
//            placeName = itemView.findViewById(R.id.de)
//
//        }
//    }
//
//    fun ExampleAdapter(mLocation: ArrayList<Location>?) {
//        mExampleList = mLocation
//    }
//
//    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder? {
//
//        val v: View =
//            LayoutInflater.from(parent.context).inflate(R.layout., parent, false)
//        return LocationViewHolder(v)
//    }
//
//    fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
//        val currentItem: Location = mLocation!![position]
//        holder.placeName.setText(currentItem.displayName)
//
//    }
//
//    fun getItemCount(): Int {
//        return mLocation!!.size
//    }
//}