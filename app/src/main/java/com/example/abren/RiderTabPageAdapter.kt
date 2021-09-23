package com.example.abren

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RiderTabPageAdapter(fragment: Fragment, private val tabCount: Int) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> NearbyDriversListScreenFragment()
            1 -> RequestedDriversScreenFragment()
            2 -> AcceptedRequestScreenFragment()
            else -> NearbyDriversListScreenFragment()
        }
    }
}