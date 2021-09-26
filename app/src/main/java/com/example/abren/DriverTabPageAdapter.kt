package com.example.abren

import AcceptedRidersFragment
import RequestedRidersFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DriverTabPageAdapter(fragment: Fragment, private val tabCount: Int) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int =tabCount

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0 -> RequestedRidersFragment()
            1 -> AcceptedRidersFragment()
            else -> RequestedRidersFragment()
        }
    }
}
