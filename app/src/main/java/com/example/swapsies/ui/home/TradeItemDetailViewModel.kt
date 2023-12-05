package com.example.swapsies.ui.home

import androidx.lifecycle.ViewModel
import com.example.swapsies.model.Trade
import com.example.swapsies.model.TradeItem
import com.example.swapsies.notifications.NotificationData
import com.example.swapsies.notifications.PushNotification
import com.example.swapsies.repository.TradeItemRepository
import com.example.swapsies.repository.TradeRepository


class DetailFragmentViewModel: ViewModel() {

    private val tradeRepository = TradeRepository.getInstance();
    private val tradeItemRepository = TradeItemRepository.getInstance();
    var userTradeItems = emptyList<TradeItem>()

    fun addTrade(trade: Trade) = tradeRepository.addTrades(trade)

    fun sendOfferNotification(trade: Trade){
        val title = "You have received a trade offer"
        val message = trade.offerItem.name + " for " + trade.listedItem.name
        val notification = PushNotification(NotificationData(title, message), "/topics/" + trade.listedItem.uuid)
        tradeRepository.sendNotification(notification)
    }

    suspend fun getUserTradeItems(userId: String): List<TradeItem> {
        return tradeItemRepository.getUserTradeItem(userId);
    }

    fun deleteTradeItem(tradeItem: TradeItem) = tradeItemRepository.deleteTradeItem(tradeItem)

    suspend fun deleteTrades(tradeItem: TradeItem) = tradeRepository.deleteTrades(tradeItem)
}