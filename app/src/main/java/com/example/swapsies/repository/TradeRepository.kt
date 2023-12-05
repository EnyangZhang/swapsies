package com.example.swapsies.repository

import android.util.Log
import com.example.swapsies.model.Trade
import com.example.swapsies.model.TradeItem
import com.example.swapsies.model.TradeStatus
import com.example.swapsies.notifications.PushNotification
import com.example.swapsies.notifications.RetrofitInstance
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class TradeRepository {

    companion object {
        private val instance: TradeRepository = TradeRepository()

        fun getInstance(): TradeRepository {
            return instance
        }
    }

    val tag = "TradeRepository"
    private val db = FirebaseFirestore.getInstance()
    private val tradeCollection = "trades"
    private val TAG = "TradesRepository"
    private val statusFieldName = "status"

    fun addTrades(trade: Trade){
        db.collection(tradeCollection)
            .add(trade)
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(tag, Gson().toJson(response))
            } else {
                Log.e(tag, response.errorBody().toString())
            }
        } catch (e: Exception){
            Log.e(tag, e.toString())
        }
    }

    fun withdrawTrade(trade: Trade) {
        db.collection(tradeCollection).document(trade.id).delete()
    }

    fun acceptTrade(trade: Trade) {
        db.collection(tradeCollection).document(trade.id).update(statusFieldName, TradeStatus.ACCEPTED)
    }

    fun declineTrade(trade: Trade) {
        db.collection(tradeCollection).document(trade.id).delete()
    }

    suspend fun deleteTrades(tradeItem: TradeItem){
        val collection = db.collection(tradeCollection).get().await()

        for (document in collection){
                val trade = document.toObject(Trade::class.java)
                if (trade.listedItem.firestoreImageUrl == tradeItem.firestoreImageUrl || trade.offerItem.firestoreImageUrl == tradeItem.firestoreImageUrl){
                    db.collection(tradeCollection).document(trade.id).delete()
            }
        }
    }
}