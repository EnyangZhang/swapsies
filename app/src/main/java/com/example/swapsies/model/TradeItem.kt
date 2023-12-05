package com.example.swapsies.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class TradeItem(
    @DocumentId
    val id: String = "",
    val email: String = "",
    val name: String ="",
    val description: String ="",
    val location: String ="",
    val uuid: String = "",
    @ServerTimestamp
    val dateListed: Date? = null,
    val tradeOffers: List<String>? = emptyList(),
    var firestoreImageUrl: String? = "") : Parcelable {

    override fun toString(): String {
        return "$name - $description"
    }
}


