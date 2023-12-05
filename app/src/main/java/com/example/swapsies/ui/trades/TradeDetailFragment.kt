package com.example.swapsies.ui.trades

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.swapsies.R
import com.example.swapsies.enum.TradeDetailScreen
import com.example.swapsies.model.Trade
import com.example.swapsies.ui.trades.bottombuttons.ContactFragment
import com.example.swapsies.ui.trades.bottombuttons.DecisionFragment
import com.example.swapsies.ui.trades.bottombuttons.WithdrawFragment
import com.google.firebase.auth.FirebaseAuth


class TradeDetailFragment() : Fragment() {

    private val args by navArgs<TradeDetailFragmentArgs>()
    private lateinit var viewModel: TradeDetailViewModel
    private lateinit var trade: Trade



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_trade_detail, container, false)
        viewModel = ViewModelProvider(this).get(TradeDetailViewModel::class.java)
        trade = args.currentTrade
        val tradeDetailScreen = args.tradeDetailScreen
        // Inflate the layout for this fragment

        val offerItemName = root.findViewById<TextView>(R.id.offer_item_name)
        offerItemName.text = trade.offerItem.name
        val offerItemDescription = root.findViewById<TextView>(R.id.offer_item_description)
        offerItemDescription.text = trade.offerItem.description
        val offerItemLocation = root.findViewById<TextView>(R.id.offer_item_location)
        offerItemLocation.text = trade.offerItem.location
        val offerItemImage = root.findViewById<ImageView>(R.id.offer_item_image)
        Glide.with(requireContext()).load(trade.offerItem.firestoreImageUrl).into(offerItemImage)

        val listedItemName = root.findViewById<TextView>(R.id.listed_item_name)
        listedItemName.text = trade.listedItem.name
        val listedItemDescription = root.findViewById<TextView>(R.id.listed_item_description)
        listedItemDescription.text = trade.listedItem.description
        val listedItemLocation = root.findViewById<TextView>(R.id.listed_item_location)
        listedItemLocation.text = trade.listedItem.location
        val listedItemImage = root.findViewById<ImageView>(R.id.listed_item_image)
        Glide.with(requireContext()).load(trade.listedItem.firestoreImageUrl).into(listedItemImage)


        val actionFragment = when(tradeDetailScreen) {
            TradeDetailScreen.OFFERING.name -> WithdrawFragment(trade)
            TradeDetailScreen.OFFERED.name -> DecisionFragment(trade)
            TradeDetailScreen.COMPLETED.name -> ContactFragment(trade)
            else -> WithdrawFragment(trade)
        }

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.action_fragment_container, actionFragment)
            commit()
        }


        return root
    }

}