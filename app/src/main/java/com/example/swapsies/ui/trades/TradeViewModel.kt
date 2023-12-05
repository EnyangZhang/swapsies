package com.example.swapsies.ui.trades

import androidx.lifecycle.ViewModel
import com.example.swapsies.enum.TradeDetailScreen

class TradeViewModel : ViewModel() {
    var mostRecentScreen: TradeDetailScreen = TradeDetailScreen.OFFERED
}