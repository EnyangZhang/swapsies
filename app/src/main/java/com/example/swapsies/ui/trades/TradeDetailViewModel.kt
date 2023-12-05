package com.example.swapsies.ui.trades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapsies.model.Trade
import com.example.swapsies.notifications.NotificationData
import com.example.swapsies.notifications.PushNotification
import com.example.swapsies.repository.TradeRepository

class TradeDetailViewModel : ViewModel() {
    private val tradeRepository = TradeRepository.getInstance();
    fun withdrawTrade(trade: Trade){
        tradeRepository.withdrawTrade(trade)
    }
    fun acceptTrade(trade: Trade){
        tradeRepository.acceptTrade(trade)
        val title = "Your trade offer was accepted!"
        val message = trade.offerItem.name + " for " + trade.listedItem.name
        val notification = PushNotification(NotificationData(title, message), "/topics/" + trade.offerItem.uuid)
        tradeRepository.sendNotification(notification)
    }
    fun declineTrade(trade: Trade){
        tradeRepository.declineTrade(trade)
        val title = "Your trade offer was declined!"
        val message = trade.offerItem.name + " for " + trade.listedItem.name
        val notification = PushNotification(NotificationData(title, message), "/topics/" + trade.offerItem.uuid)
        tradeRepository.sendNotification(notification)
    }

}