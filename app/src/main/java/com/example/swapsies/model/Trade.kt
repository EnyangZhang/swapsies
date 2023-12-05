package com.example.swapsies.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Trade(
    @DocumentId
    val id: String = "",
    val offerItem: TradeItem = TradeItem(),
    val listedItem: TradeItem = TradeItem(),
    val status: String = TradeStatus.PENDING.toString(),
    val participants: List<String> = emptyList(),
    @ServerTimestamp
    val dateCreated: Date? = null
) : Parcelable