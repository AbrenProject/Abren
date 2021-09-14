package com.example.abren

import AcceptedFragment
import RequestedFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPageAdapter (activity:FragmentActivity, private val tabCount: Int): FragmentStateAdapter(activity){

    override fun getItemCount(): Int =tabCount

    override fun createFragment(position: Int): Fragment {
        TODO("Not yet implemented")

        return when(position){
            0 -> NearbyRidersFragment()
            1 -> AcceptedFragment()
            2 -> RequestedFragment()
            else -> NearbyRidersFragment()
        }
    }
}