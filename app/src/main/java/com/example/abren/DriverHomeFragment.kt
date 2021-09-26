package com.example.abren

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.anton46.stepsview.StepsView
import com.example.abren.viewmodel.RouteViewModel


class DriverHomeFragment : Fragment() {

    private val routeViewModel: RouteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routeViewModel.listRoutes(requireContext())
        return inflater.inflate(R.layout.fragment_driver_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routeViewModel.createdRouteLiveDataList?.observe(viewLifecycleOwner, Observer { routeList ->
            Log.d("Calling on listRoute: Returned Route = ", routeList.toString())
            Log.d("check routeList length = ", routeList.size.toString())
            Log.d("check routeList[0] of name = ", routeList[0].startingLocation?.name.toString())

            val views = arrayOfNulls<String>(routeList.size)
            val mListView = view.findViewById<View>(R.id.list_listView) as ListView

            val allRouteLabels: ArrayList<Array<String>> = ArrayList()

            for (i in routeList.indices) {
                val labels: ArrayList<String> = ArrayList()
                labels.add(routeList[i].startingLocation?.name?.substringBefore(",")!!)

                for (j in 0 until routeList[i].waypointLocations.size) {
                    labels.add(routeList[i].waypointLocations[j].name?.substringBefore(",")!!)
                }

                labels.add(routeList[i].destinationLocation?.name?.substringBefore(",")!!)

                allRouteLabels.add(labels.toTypedArray())
            }


            val adapter = MyAdapter(requireContext(), 0, allRouteLabels)
            adapter.addAll(*views)
            Log.d("views = ", views.toString())
            mListView.adapter = adapter
        })

        view.findViewById<Button>(R.id.create_route_button).setOnClickListener {
            findNavController().navigate(R.id.action_driverHomeFragment_to_createRouteFragment)
        }
    }

}

class MyAdapter(context: Context?, resource: Int, private val labels: ArrayList<Array<String>>) :
    ArrayAdapter<String?>(context!!, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val rowView: View = inflater.inflate(R.layout.row, null, true)

        val mLabel: TextView = rowView.findViewById<View>(R.id.label) as TextView
        val mStepsView: StepsView = rowView.findViewById<View>(R.id.stepsView) as StepsView

        val current = labels[position]
        mLabel.text = getItem(position)
        mStepsView.setCompletedPosition(current.size - 1)
            .setLabels(current)
            .setBarColorIndicator(
                context.resources.getColor(android.R.color.darker_gray)
            )
            .setProgressColorIndicator(context.resources.getColor(R.color.orange))
            .setLabelColorIndicator(context.resources.getColor(R.color.orange))
            .drawView()

        return rowView
    }
}
