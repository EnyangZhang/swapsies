package com.example.swapsies.util

import android.content.Context
import com.example.swapsies.R
import com.example.swapsies.model.TradeItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit


fun getDaysSincePosted(tradeItem: TradeItem, context: Context): String{
    if (tradeItem.dateListed == null) { return "" }
    val currentDate = LocalDate.now()
    val listedDate: LocalDate = Instant.ofEpochMilli(tradeItem.dateListed.time!!).atZone(ZoneId.systemDefault()).toLocalDate()
    val daysSincePosted: Int = ChronoUnit.DAYS.between(listedDate, currentDate).toInt()
    return if (daysSincePosted == 0){
        context.resources.getString(R.string.posted_today)
    } else if(daysSincePosted == 1){
        context.resources.getString(R.string.posted_yesterday)
    }
    else { context.resources.getString(R.string.posted_days_ago, daysSincePosted)
    }
}

