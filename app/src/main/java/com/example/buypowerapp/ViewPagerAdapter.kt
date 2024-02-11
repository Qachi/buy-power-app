package com.example.buypowerapp

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.buypowerapp.ui.MainActivity

class ViewPagerAdapter(private val items: ArrayList<Fragment>, activity: MainActivity) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
}