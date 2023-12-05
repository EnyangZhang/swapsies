package com.example.swapsies.ui.trades.bottombuttons

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.swapsies.R
import com.example.swapsies.model.Trade
import com.example.swapsies.ui.trades.TradeDetailFragment
import com.example.swapsies.ui.trades.TradeDetailViewModel
import com.google.firebase.auth.FirebaseAuth


class ContactFragment(val trade: Trade=Trade()) : Fragment() {

    private lateinit var viewModel : TradeDetailViewModel
    private lateinit var mAuth: FirebaseAuth
    private val emailClientsOnly = "message/rfc822"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(TradeDetailViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        val root = inflater.inflate(R.layout.fragment_contact, container, false)
        root.findViewById<Button>(R.id.contact_button).setOnClickListener {
            sendEmail()
        }
        return root
    }

    private fun sendEmail(){
        val email = if (mAuth.currentUser?.email == trade.listedItem.email){
            trade.offerItem.email
        } else {
            trade.listedItem.email
        }
        val subject = requireContext().getString(R.string.trade_meet)
        val message = requireContext().getString(R.string.trade_meet_detail, trade.listedItem.name, trade.offerItem.name)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = emailClientsOnly
        startActivity(Intent.createChooser(intent, requireContext().getString(R.string.choose_email_client)))
    }


}