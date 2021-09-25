package com.example.abren

import android.os.Bundle
import android.content.Context
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.anton46.stepsview.StepsView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DriverHomeFragment : Fragment() {

    private val views = arrayOf("View 1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_driver_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mListView = view.findViewById<View>(R.id.list_listView) as ListView
        val adapter = MyAdapter(requireContext(),0)
        adapter.addAll(*views)
        mListView.adapter = adapter

//        view.findViewById<Button>(R.id.create_route_button).setOnClickListener{
//            findNavController().navigate(R.id.action_driverHomeFragment_to_createRouteFragment)
//        }
    }

    class MyAdapter(context: Context?, resource: Int) :
        ArrayAdapter<String?>(context!!, resource) {
        private val labels = arrayOf("5kilo", "Stadium", "Dembel", "WeloSefer", "Bole")
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
                    context.resources.getColor(android.R.color.darker_gray))
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
                mStepsView = view?.findViewById<View>(R.id.routeView) as StepsView
//                mLabel.rotation = (-45).toFloat()
            }
        }

    }
}