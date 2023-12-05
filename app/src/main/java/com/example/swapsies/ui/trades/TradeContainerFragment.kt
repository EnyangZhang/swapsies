package com.example.swapsies.ui.trades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.swapsies.R
import com.example.swapsies.enum.TradeDetailScreen
import com.google.android.material.tabs.TabLayout

//Holds Three child fragments, trades i've offered, trades i've been offered, and accepted trades
class TradeContainerFragment : Fragment() {

    private lateinit var tradeCollectionAdapter: TradePagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_trade_container, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tradeCollectionAdapter = TradePagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = tradeCollectionAdapter
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }
}

class TradePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 3

    override fun getItem(i: Int): Fragment {
        return when (i) {
            0 -> TradeFragment(TradeDetailScreen.OFFERED)
            1 -> TradeFragment(TradeDetailScreen.OFFERING)
            2 -> TradeFragment(TradeDetailScreen.COMPLETED)
            else -> TradeFragment(TradeDetailScreen.OFFERING)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Offered"
            1 -> "Offering"
            2 -> "Completed"
            else -> ""
        }
    }
}
