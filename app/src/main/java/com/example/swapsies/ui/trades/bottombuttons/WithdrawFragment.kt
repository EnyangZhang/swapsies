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

class WithdrawFragment(val trade: Trade = Trade()) : Fragment() {

    private lateinit var viewModel : TradeDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(TradeDetailViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_withdraw, container, false)
        root.findViewById<Button>(R.id.withdraw_button).setOnClickListener {
            withdrawTrade()
        }
        return root
    }

    private fun withdrawTrade(){
        Toast.makeText(requireContext(), R.string.trade_withdrew, Toast.LENGTH_SHORT).show()
        viewModel.withdrawTrade(trade)
        findNavController().navigate(TradeDetailFragmentDirections.actionTradeDetailFragmentToNavigationTrades())
    }
}