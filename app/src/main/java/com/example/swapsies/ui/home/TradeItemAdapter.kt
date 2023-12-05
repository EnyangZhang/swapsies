package com.example.swapsies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swapsies.model.Trade
import com.example.swapsies.model.TradeItem
import com.example.swapsies.ui.home.HomeFragmentDirections
import com.example.swapsies.ui.home.TradeItemsFragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import com.example.swapsies.util.getDaysSincePosted


class TradeItemAdapter(options: FirestoreRecyclerOptions<TradeItem>, private val showUsersItems: Boolean):
    FirestoreRecyclerAdapter<TradeItem, TradeItemAdapter.TradeItemViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellforRow = layoutInflater.inflate(R.layout.single_trade_item_view, parent, false)
        return TradeItemViewHolder(cellforRow)
    }


    override fun onBindViewHolder(holder: TradeItemViewHolder, position: Int, model: TradeItem) {
        holder.name.text = model.name
        holder.date.text = getDaysSincePosted(model, holder.date.context)
        holder.location.text = model.location
        Glide.with(holder.image.context)
            .load(model.firestoreImageUrl)
            .fallback(R.drawable.ic_baseline_cloud_upload_24)
            .into(holder.image)

        holder.cardView.setOnClickListener(){
            val action = if (showUsersItems) {
                HomeFragmentDirections.actionHomeFragmentToViewMyTradeItemFragment(model)
            } else {
                HomeFragmentDirections.actionHomeFragmentToViewOtherTradeItemFragment(model)
            }
            holder.cardView.findNavController().navigate(action)
        }
    }

    inner class TradeItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.name)
        val date: TextView = view.findViewById(R.id.date_listed)
        val image: ImageView = view.findViewById(R.id.item_image)
        val cardView: CardView = view.findViewById(R.id.card_view)
        val location: TextView = view.findViewById(R.id.location)
    }
}



