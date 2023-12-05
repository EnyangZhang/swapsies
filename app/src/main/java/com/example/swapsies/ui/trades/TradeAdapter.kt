package com.example.swapsies.ui.trades

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swapsies.R
import com.example.swapsies.enum.TradeDetailScreen
import com.example.swapsies.model.Trade
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TradeAdapter(options: FirestoreRecyclerOptions<Trade>, val tradeDetailScreen: TradeDetailScreen):
FirestoreRecyclerAdapter<Trade, TradeViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellforRow = layoutInflater.inflate(R.layout.trade_view, parent, false)
        return TradeViewHolder(cellforRow)
    }

    override fun onBindViewHolder(holder: TradeViewHolder, position: Int, model: Trade) {
        holder.offeredItemName.text = model.offerItem.name
        Glide.with(holder.offeredItemName.context).load(model.offerItem.firestoreImageUrl).into(holder.offeredItemImage)
        holder.postedItemName.text = model.listedItem.name
        Glide.with(holder.postedItemImage.context).load(model.listedItem.firestoreImageUrl).into(holder.postedItemImage)

        holder.cardView.setOnClickListener(){
            val action = TradeContainerFragmentDirections.actionNavigationTradesToTradeDetailFragment(model, tradeDetailScreen.name)
            holder.cardView.findNavController().navigate(action)
        }
    }

}

class TradeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val offeredItemName: TextView = view.findViewById(R.id.offered_item_name)
    val offeredItemImage: ImageView = view.findViewById(R.id.offered_item_image)
    val postedItemName: TextView = view.findViewById(R.id.posted_item_name)
    val postedItemImage: ImageView = view.findViewById(R.id.posted_item_image)
    val cardView: CardView = view.findViewById(R.id.trade_card_view)
}