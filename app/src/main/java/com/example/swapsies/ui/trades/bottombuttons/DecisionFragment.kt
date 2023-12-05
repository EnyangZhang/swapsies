package com.example.swapsies.ui.trades.bottombuttons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.swapsies.R
import com.example.swapsies.model.Trade
import com.example.swapsies.ui.trades.TradeDetailFragment
import com.example.swapsies.ui.trades.TradeDetailFragmentDirections
import com.example.swapsies.ui.trades.TradeDetailViewModel

class DecisionFragment(val trade: Trade=Trade()) : Fragment() {

    private lateinit var viewModel : TradeDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(TradeDetailViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_decision, container, false)
        root.findViewById<Button>(R.id.accept_trade_button).setOnClickListener {
            acceptTrade()
        }
        root.findViewById<Button>(R.id.decline_trade_button).setOnClickListener {
            declineTrade()
        }
        return root
    }

    private fun acceptTrade(){
        Toast.makeText(requireContext(), R.string.trade_accepted, Toast.LENGTH_SHORT).show()
        viewModel.acceptTrade(trade)
        findNavController().navigate(TradeDetailFragmentDirections.actionTradeDetailFragmentToNavigationTrades())
    }

    private fun declineTrade(){
        Toast.makeText(requireContext(), R.string.trade_declined, Toast.LENGTH_SHORT).show()
        viewModel.declineTrade(trade)
        findNavController().navigate(TradeDetailFragmentDirections.actionTradeDetailFragmentToNavigationTrades())
    }

}