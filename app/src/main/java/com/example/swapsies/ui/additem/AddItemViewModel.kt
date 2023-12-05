package com.example.swapsies.ui.additem

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapsies.model.TradeItem
import com.example.swapsies.repository.TradeItemRepository
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*


class AddItemViewModel() : ViewModel() {

    private val repository = TradeItemRepository.getInstance()

    fun addTradeItem(tradeItem: TradeItem) = repository.addTradeItem(tradeItem, localImageFile)

    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    var localImageFile: File? = null

}