package com.example.swapsies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.swapsies.R
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {

    private lateinit var tradeItemPagerAdapter: TradeItemPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tradeItemPagerAdapter = TradeItemPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = tradeItemPagerAdapter
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }
}

class TradeItemPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        return if(i == 0) {
            TradeItemsFragment(showUsersItems = false)
        } else {
            TradeItemsFragment(showUsersItems = true)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0){
            "Listed items"
        } else {
            "My items"
        }
    }
}
