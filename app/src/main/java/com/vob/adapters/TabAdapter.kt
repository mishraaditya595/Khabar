package com.vob.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vob.ui.fragments.HomeFragment
import com.vob.ui.fragments.TrendingNewsFragment

class TabAdapter(
    private val context: Context, fm: FragmentManager, internal var totalTabs: Int ): FragmentPagerAdapter(fm)
{

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return TrendingNewsFragment() }
            1 -> {}
            2 -> {}
            3 -> {}
            4 -> {}
            5 -> {}
            6 -> {}
            7 -> {}
        }
        return TrendingNewsFragment()
    }

    override fun getCount(): Int {
       return totalTabs
    }
}