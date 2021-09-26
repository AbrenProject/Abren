package com.example.abren.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.anton46.stepsview.StepsView
import com.example.abren.R
import com.example.abren.models.Route

class RouteAdapter(context: Context?, resource: Int, private val routeList: List<Route>) :
    ArrayAdapter<String?>(context!!, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val rowView: View = inflater.inflate(R.layout.row, null, true)

        val mLabel: TextView = rowView.findViewById<View>(R.id.label) as TextView
        val mStepsView: StepsView = rowView.findViewById<View>(R.id.stepsView) as StepsView

        val current = routeList[position]
        val labels: ArrayList<String> = ArrayList()
        labels.add(current.startingLocation?.name?.substringBefore(",")!!)

        for (j in 0 until current.waypointLocations.size) {
            labels.add(current.waypointLocations[j].name?.substringBefore(",")!!)
        }

        labels.add(current.destinationLocation?.name?.substringBefore(",")!!)

        mLabel.text = "${labels[0]} --> ${labels[labels.size - 1]}"
        mStepsView.setCompletedPosition(labels.size - 1)
            .setLabels(labels.toTypedArray())
            .setBarColorIndicator(
                context.resources.getColor(android.R.color.darker_gray)
            )
            .setProgressColorIndicator(context.resources.getColor(android.R.color.darker_gray))
            .setLabelColorIndicator(ContextCompat.getColor(context, R.color.darkGray))
            .drawView()

        return rowView
    }

    fun getObject(position: Int): Route {
        return routeList[position]
    }
}
