package com.example.swapsies.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.swapsies.R
import com.example.swapsies.model.Trade
import com.example.swapsies.model.TradeItem
import com.example.swapsies.model.TradeStatus
import com.example.swapsies.repository.TradeRepository
import com.example.swapsies.ui.trades.TradeDetailFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.swapsies.util.getDaysSincePosted
import org.w3c.dom.Text


class ViewMyTradeItemFragment : Fragment() {

    private lateinit var viewModel: DetailFragmentViewModel;
    private val args by navArgs<ViewMyTradeItemFragmentArgs>()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(DetailFragmentViewModel::class.java)

        val listedItem = args.currentTradeItem
        mAuth = FirebaseAuth.getInstance()

        val root = inflater.inflate(R.layout.fragment_view_my_trade_item, container, false)

        val nameLabel = root.findViewById<TextView>(R.id.item_name_detail_text)
        nameLabel.text = listedItem.name
        val descriptionLabel = root.findViewById<TextView>(R.id.item_description_detail)
        descriptionLabel.text = listedItem.description
        val locationLabel = root.findViewById<TextView>(R.id.item_location_detail)
        locationLabel.text = listedItem.location
        val listedLabel = root.findViewById<TextView>(R.id.item_listed_detail)
        listedLabel.text = getDaysSincePosted(listedItem, requireContext())
        val image: ImageView = root.findViewById(R.id.item_image_detail)
        Glide.with(requireContext()).load(listedItem.firestoreImageUrl)
            .fallback(R.drawable.ic_baseline_cloud_upload_24).into(image)


        val deleteButton = root.findViewById<Button>(R.id.delete_btn)

        deleteButton.setOnClickListener() {
            try {
                CoroutineScope(IO).launch {
                    deleteTrade(listedItem)
                    deleteTradeItem(listedItem)
                }
                backToHome()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.delete_success),
                    Toast.LENGTH_LONG
                ).show()
            } catch (err: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_message),
                    Toast.LENGTH_LONG
                ).show()

            }
        }

        return root
    }

    fun deleteTradeItem(listedItem: TradeItem) {
        viewModel.deleteTradeItem(listedItem)

    }

    suspend fun deleteTrade(listedItem: TradeItem) {
        viewModel.deleteTrades(listedItem)
    }

    fun backToHome(){
        val action = ViewMyTradeItemFragmentDirections.actionViewMyTradeItemFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}
