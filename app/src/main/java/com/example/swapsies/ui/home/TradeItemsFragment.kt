package com.example.swapsies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapsies.R
import com.example.swapsies.TradeItemAdapter
import com.example.swapsies.model.TradeItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TradeItemsFragment(var showUsersItems: Boolean? = null) : Fragment() {


    private val tradeItemsCollectionName = "tradeItems"
    private val dateFieldName = "dateListed"
    private val userIdFieldName = "uuid"

    private val mAuth = FirebaseAuth.getInstance()
    private val currentUserId = mAuth.currentUser!!.uid
    private var shouldShowUsersItems = showUsersItems


    private lateinit var viewModel: HomeViewModel
    var db = Firebase.firestore


    private lateinit var tradeItemAdapter: TradeItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trade_items, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        if(shouldShowUsersItems == null){
            shouldShowUsersItems = viewModel.showUsersItems
        } else {
            viewModel.showUsersItems = shouldShowUsersItems!!
        }

        var query = createQuery(shouldShowUsersItems!!)
        val options = FirestoreRecyclerOptions.Builder<TradeItem>()
            .setQuery(query, TradeItem::class.java)
            .setLifecycleOwner(this)
            .build()
        tradeItemAdapter = TradeItemAdapter(options, shouldShowUsersItems!!)
        recyclerView.adapter = tradeItemAdapter

        return root
    }

    override fun onStart() {
        super.onStart()
        tradeItemAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        tradeItemAdapter.stopListening()
    }

    private fun createQuery(shouldShowUsersItems: Boolean): Query {
        return if (shouldShowUsersItems) {
            db
                .collection(tradeItemsCollectionName)
                .whereEqualTo(userIdFieldName, currentUserId)
        } else {
            db
                .collection(tradeItemsCollectionName)
                .whereNotEqualTo(userIdFieldName, currentUserId)
                .orderBy(userIdFieldName)
                .orderBy(dateFieldName, Query.Direction.DESCENDING)
        }
    }

}