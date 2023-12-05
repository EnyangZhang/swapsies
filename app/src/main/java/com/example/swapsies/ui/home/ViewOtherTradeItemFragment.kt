package com.example.swapsies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.swapsies.R
import com.example.swapsies.model.Trade
import com.example.swapsies.model.TradeItem
import com.example.swapsies.model.TradeStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.swapsies.util.getDaysSincePosted


class ViewOtherTradeItemFragment : Fragment() {

    private lateinit var viewModel: DetailFragmentViewModel;
    private val args by navArgs<ViewOtherTradeItemFragmentArgs>()
    private lateinit var mAuth: FirebaseAuth

    private var tradeItemPosition: Int = -1
    private lateinit var tradeItemDropdown: AutoCompleteTextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(DetailFragmentViewModel::class.java)


        val listedItem = args.currentTradeItem
        mAuth = FirebaseAuth.getInstance()

        val root = inflater.inflate(R.layout.fragment_view_other_trade_item, container, false)

        val askToTradeButton = root.findViewById<Button>(R.id.ask_to_trade_btn)
        val nameLabel = root.findViewById<TextView>(R.id.item_name_detail_text)
        nameLabel.text = listedItem.name
        val descriptionLabel = root.findViewById<TextView>(R.id.item_description_detail)
        descriptionLabel.text = listedItem.description
        val locationLabel = root.findViewById<TextView>(R.id.location_view)
        locationLabel.text = listedItem.location
        val listedLabel = root.findViewById<TextView>(R.id.item_listed_detail)
        listedLabel.text = getDaysSincePosted(listedItem, requireContext())
        val image: ImageView = root.findViewById(R.id.item_image_detail)
        Glide.with(requireContext()).load(listedItem.firestoreImageUrl)
            .fallback(R.drawable.ic_baseline_cloud_upload_24).into(image)

        tradeItemDropdown = root.findViewById(R.id.auto_complete_text_view)

        CoroutineScope(IO).launch {
            getUserTradeItems(mAuth.currentUser!!.uid)
        }

        val adapter = ArrayAdapter<TradeItem>(
            requireContext(), // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            viewModel.userTradeItems // Array
        )
        tradeItemDropdown.setAdapter(adapter)
        tradeItemDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                tradeItemPosition = position
            }

        askToTradeButton.setOnClickListener() {
            try {
                if (tradeItemPosition == -1) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.missing_trade_item_ask_to_trade),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val offerItem = viewModel.userTradeItems[tradeItemPosition]
                    val trade =
                        Trade("", offerItem, listedItem, TradeStatus.PENDING.toString(),
                            listOf(offerItem.uuid, listedItem.uuid), null)
                    viewModel.addTrade(trade)
                    viewModel.sendOfferNotification(trade)
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(
                            R.string.trade_successful,
                            offerItem.name,
                            listedItem.name
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
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

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//        }
//    }

    private suspend fun updateText(tradeItem: List<TradeItem>) {
        withContext(Main) {
            val itemNames = viewModel.userTradeItems.map { it.name }
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.user_trade_item_dropdown, itemNames)
            tradeItemDropdown.setAdapter(arrayAdapter)
        }
    }

    private suspend fun getUserTradeItems(userId: String) {
        val tradeItems = viewModel.getUserTradeItems(userId)
        viewModel.userTradeItems = tradeItems
        updateText(tradeItems)
    }
}
