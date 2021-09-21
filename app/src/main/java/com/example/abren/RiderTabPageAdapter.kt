package com.example.abren

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RiderTabPageAdapter (activity:FragmentActivity, private val tabCount: Int): FragmentStateAdapter(activity){

    override fun getItemCount(): Int =tabCount

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0 -> NearbyDriversFragment()
            1 -> NearbyDriversListScreenFragment()
            2-> RequestedDriversScreenFragment()
            3 -> AcceptedRequestScreenFragment()
            else -> NearbyDriversFragment()
        }
    }
}