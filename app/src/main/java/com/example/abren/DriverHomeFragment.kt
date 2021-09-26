package com.example.abren

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.anton46.stepsview.StepsView
import com.example.abren.models.Route
import com.example.abren.viewmodel.RouteViewModel

class DriverHomeFragment : Fragment() {

    lateinit var sharedPrefs: SharedPreferences
    private val PREF_NAME = "route_names"
    private lateinit var routeList:List<Route>

    private val routeViewModel: RouteViewModel by activityViewModels()
    private lateinit var views:Array<Int>


    private lateinit var startingLocation: com.example.abren.models.Location
    private lateinit var destinationLocation: com.example.abren.models.Location
    private lateinit var wayPointLocations: ArrayList<com.example.abren.models.Location>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routeViewModel.listRoutes(requireContext())
        routeViewModel.createdRouteLiveDataList?.observe(viewLifecycleOwner, Observer { route ->
            Log.d("Calling on listRoute: Returned Route = ", route.toString())
            routeList = route
            Log.d("check routeList length = ", routeList.size.toString())
            Log.d("check routeList[0] of name = ", routeList[0].startingLocation?.name.toString())

        })
        return inflater.inflate(R.layout.fragment_driver_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val PREF_START_NAME = "startPreference"
        val PREF_MIDDLE_NAME1 = "middlePreference1"
        val PREF_MIDDLE_NAME2 = "middlePreference2"
        val PREF_DEST_NAME = "destPreference"

        routeViewModel.createdRouteLiveDataList?.observe(viewLifecycleOwner, Observer { route->
            routeList = route
            val views = arrayOfNulls<String>(routeList.size)
            val mListView = view.findViewById<View>(R.id.list_listView) as ListView


                 ///

            for(routee in routeList) {
                routee.startingLocation
                routee.destinationLocation
                routee.waypointLocations.size
                val lables = arrayOfNulls<String>(routee.waypointLocations.size+2)
            }


            val adapter = MyAdapter(requireContext(), 0)
            adapter.addAll(*views)
            Log.d("views = ",views.toString())
            mListView.adapter = adapter

        })


//        val locationNameArray:Array<String> =
//            arrayOf(arrayOf(startingLocation.displayName,destinationLocation.displayName,wayPointLocations[0].displayName).toString())


        view.findViewById<Button>(R.id.create_route_button).setOnClickListener {
            findNavController().navigate(R.id.action_driverHomeFragment_to_createRouteFragment)
        }

    }

}


    class MyAdapter(context: Context?, resource: Int) : ArrayAdapter<String?>(context!!, resource) {

//        private val labels = arrayOf("Stadium", "Dembel", "WeloSefer", "Bole")
        val labels = arrayOfNulls<String>(5)
//        private val labels = arrayOf(list)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            labels[0] = "dembel"
            var convertView = convertView
            val holder: MyAdapter.ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row, null)
                holder = ViewHolder(convertView)
                convertView!!.tag = holder
            } else {
                holder = convertView.tag as MyAdapter.ViewHolder
            }
//            holder.mLabel.text = getItem(position)
            holder.mStepsView.setCompletedPosition(position % labels.size)
                .setLabels(labels)
                .setBarColorIndicator(
                    context.resources.getColor(android.R.color.darker_gray)
                )
                .setProgressColorIndicator(context.resources.getColor(R.color.orange))
                .setLabelColorIndicator(context.resources.getColor(R.color.orange))
                .drawView()
            return convertView
        }

        internal class ViewHolder(view: View?) {
            //            var mLabel: TextView
            var mStepsView: StepsView

            init {
//                mLabel = view!!.findViewById<View>(R.id.label) as TextView
                mStepsView = view?.findViewById<View>(R.id.stepsView) as StepsView
//                mLabel.rotation = (-45).toFloat()
            }
        }

    }
