package com.example.swapsies.ui.trades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapsies.R
import com.example.swapsies.enum.TradeDetailScreen
import com.example.swapsies.model.Trade
import com.example.swapsies.model.TradeStatus
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TradeFragment(private val tradeDetailScreen: TradeDetailScreen? = null): Fragment() {
    private val db = Firebase.firestore

    private val tradeCollectionName = "trades"
    private val listedItemUserIdFieldname = "listedItem.uuid"
    private val offerItemUserIdFieldname = "offerItem.uuid"
    private val statusFieldName = "status"
    private val participantsFieldName = "participants"

    private val mAuth = FirebaseAuth.getInstance()
    private val currentUserId = mAuth.currentUser!!.uid

    private var screenToShow: TradeDetailScreen? = tradeDetailScreen

    private lateinit var tradeAdapter: TradeAdapter
    private lateinit var viewModel: TradeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_trade, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(TradeViewModel::class.java)

        if(screenToShow == null){
            screenToShow = viewModel.mostRecentScreen
        } else {
            viewModel.mostRecentScreen = screenToShow!!
        }

        val query = createQuery(screenToShow!!)

        val options = FirestoreRecyclerOptions.Builder<Trade>()
            .setQuery(query, Trade::class.java)
            .setLifecycleOwner(this)
            .build()

        tradeAdapter = TradeAdapter(options, screenToShow!!)
        recyclerView.adapter = tradeAdapter

        return root
    }

    override fun onStart() {
        super.onStart()
        tradeAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        tradeAdapter.stopListening()
    }

    private fun createQuery(tradeDetailScreen: TradeDetailScreen): Query {
        return when (tradeDetailScreen) {
            TradeDetailScreen.OFFERED -> {
                db
                    .collection(tradeCollectionName)
                    .whereEqualTo(listedItemUserIdFieldname, currentUserId)
                    .whereEqualTo(statusFieldName, TradeStatus.PENDING)

            }
            TradeDetailScreen.OFFERING -> {
                db
                    .collection(tradeCollectionName)
                    .whereEqualTo(offerItemUserIdFieldname, currentUserId)
                    .whereEqualTo(statusFieldName, TradeStatus.PENDING)
            }
            else -> {
                db
                    .collection(tradeCollectionName)
                    .whereEqualTo(statusFieldName, TradeStatus.ACCEPTED)
                    .whereArrayContains(participantsFieldName, currentUserId)
            }
        }
    }
}