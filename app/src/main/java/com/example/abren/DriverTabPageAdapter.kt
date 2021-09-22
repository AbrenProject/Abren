package com.example.abren

import AcceptedRidersFragment
import RequestedRidersFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DriverTabPageAdapter (activity: FragmentActivity, private val tabCount: Int): FragmentStateAdapter(activity){

    override fun getItemCount(): Int =tabCount

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0 -> NearbyRidersFragment()
            1 -> RequestedRidersFragment()
            2 -> AcceptedRidersFragment()
            else -> NearbyRidersFragment()
        }
    }
}
