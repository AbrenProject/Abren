package com.example.abren.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import com.example.abren.models.Location


class LocationSuggestionAdapter(context: Context, @LayoutRes private val layoutResource: Int) : ArrayAdapter<Location>(context, layoutResource),  Filterable {
    private var data: MutableList<Location> = ArrayList()

    fun setData(list: List<Location>?) {
        data.clear()
        data.addAll(list!!)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Location {
        return data[position]
    }

    fun getObject(position: Int): Location {
        return data[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = data
                    filterResults.count = data.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}