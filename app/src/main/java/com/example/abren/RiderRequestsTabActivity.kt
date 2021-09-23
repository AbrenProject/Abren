package com.example.abren


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_nearby_drivers.*


class RiderRequestsTabActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_nearby_drivers)
        setUpTabBar()
        Log.d("IN", "In rider request tab activity")
    }

    private fun setUpTabBar()
    {
//        val adapter = RiderTabPageAdapter(this, tabLayout.tabCount)
//        viewPager.adapter = adapter
//
//        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()
//        {
//            override fun onPageSelected(position: Int) {
//                tabLayout.selectTab(tabLayout.getTabAt(position))
//            }
//        })
//
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
//        {
//            override fun onTabSelected(tab: TabLayout.Tab)
//            {
//                viewPager.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//        })
    }
}