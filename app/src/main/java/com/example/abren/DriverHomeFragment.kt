package com.example.abren

import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.anton46.stepsview.StepsView
import androidx.navigation.fragment.findNavController
import com.example.abren.viewmodel.RouteViewModel

class DriverHomeFragment : Fragment() {

    lateinit var sharedPrefs: SharedPreferences
    private val PREF_NAME = "route_names"

    private val routeViewModel: RouteViewModel by activityViewModels()
    private val views = arrayOf("View 1")

    private lateinit var startingLocation:com.example.abren.models.Location
    private lateinit var destinationLocation:com.example.abren.models.Location
    private lateinit var wayPointLocations:ArrayList<com.example.abren.models.Location>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_driver_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val PREF_START_NAME = "startPreference"
        val PREF_MIDDLE_NAME1 = "middlePreference1"
        val PREF_MIDDLE_NAME2 = "middlePreference2"
        val PREF_DEST_NAME = "destPreference"

        routeViewModel.selectedRoute.observe(viewLifecycleOwner, Observer { route->
            Log.d("Calling on listRoute",route.toString())
        })
//        val locationNameArray:Array<String> =
//            arrayOf(arrayOf(startingLocation.displayName,destinationLocation.displayName,wayPointLocations[0].displayName).toString())

        val mListView = view.findViewById<View>(R.id.list_listView) as ListView

        if(routeViewModel.createdRouteLiveData?.value != null) {

            routeViewModel.createdRouteLiveData?.observe(viewLifecycleOwner, Observer { route ->
                startingLocation = route.startingLocation!!
                destinationLocation = route.destinationLocation!!
                wayPointLocations = route.waypointLocations
                Log.d("driver Home , Starting Location name =", startingLocation.displayName.toString())
                Log.d("driver Home , Starting Location name =", startingLocation.name.toString())

                val startingName = startingLocation.name.toString().substringBefore(",")
                val destinationName = destinationLocation.name.toString().substringBefore(",")
                val waypointName1 = wayPointLocations[0].name.toString().substringBefore(",")
                val waypointName2 = wayPointLocations[1].name.toString().substringBefore(",")

//                for (i in wayPointLocations.indices){
//                   var name1 = wayPointLocations[i].name.toString().substringBefore(",")
//                }

                sharedPrefs = requireContext().getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
                sharedPrefs.edit().putString(PREF_START_NAME, startingName).apply()
                sharedPrefs.edit().putString(PREF_MIDDLE_NAME1, waypointName1).apply()
                sharedPrefs.edit().putString(PREF_MIDDLE_NAME2, waypointName2).apply()
                sharedPrefs.edit().putString(PREF_DEST_NAME, destinationName).apply()

                val namesPref1 = sharedPrefs.getString(PREF_START_NAME,"entoto")
                val namesPref2 = sharedPrefs.getString(PREF_MIDDLE_NAME1,"6 Kilo")
                val namesPref3 = sharedPrefs.getString(PREF_MIDDLE_NAME2,"4 kilo")
                val namesPref4 = sharedPrefs.getString(PREF_DEST_NAME,"stadium")

                val adapter = MyAdapter(requireContext(), 0,namesPref1!!,namesPref2!!,namesPref3!!,namesPref4!!)
                adapter.addAll(*views)
                mListView.adapter = adapter
            })

        }

        view.findViewById<Button>(R.id.create_route_button).setOnClickListener{
            findNavController().navigate(R.id.action_driverHomeFragment_to_createRouteFragment)
        }
    }



    class MyAdapter(context: Context?, resource: Int, start:String,mid1:String,mid2:String,end:String) : ArrayAdapter<String?>(context!!, resource) {

//        private val labels = arrayOf("Stadium", "Dembel", "WeloSefer", "Bole")
        private val labels = arrayOf(start,mid1,mid2,end)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: MyAdapter.ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row, null)
                holder = MyAdapter.ViewHolder(convertView)
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
}