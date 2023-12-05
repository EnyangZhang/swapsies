package com.example.swapsies.repository

import android.net.Uri
import android.util.Log
import com.example.swapsies.model.TradeItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

class TradeItemRepository {

    companion object {
        private val instance: TradeItemRepository = TradeItemRepository()

        fun getInstance(): TradeItemRepository {
            return instance
        }
    }

    private val db = FirebaseFirestore.getInstance()
    private val tradeItemsCollection = "tradeItems"
    private val TAG = "TradeItemRepository"

    fun addTradeItem(tradeItem: TradeItem, localImageUri: File?){
        if(localImageUri == null) { return }
        db.collection(tradeItemsCollection)
            .add(tradeItem)
            .addOnSuccessListener {documentReferece ->
                uploadPhotoToFirestore(tradeItem, localImageUri, documentReferece.id)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun getTradeItem(tradeItemId: String) {
        db.collection(tradeItemsCollection)
            .whereEqualTo("id", tradeItemId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    suspend fun getUserTradeItem(userId: String): List<TradeItem> {
        val collection = db.collection(tradeItemsCollection)
            .whereEqualTo("uuid", userId)
            .get().await()
        for (document in collection) {
            Log.d(TAG, "${document.id} => ${document.data}")
        }
        return collection.toObjects(TradeItem::class.java)
    }

    private fun uploadPhotoToFirestore(tradeItem: TradeItem, localImageUri: File, tradeItemId: String) {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReference()
        val imageName = UUID.randomUUID().toString()
        val imageReference = storageReference.child("images/$imageName")
        imageReference.putFile(Uri.fromFile(localImageUri))
            .addOnSuccessListener {
                val downloadUrl = imageReference.downloadUrl
                downloadUrl.addOnSuccessListener {
                    tradeItem.firestoreImageUrl = it.toString()
                    updateTradeItemImageLink(tradeItem, tradeItemId)
                }
            }
            .addOnFailureListener {
                print("failed")
            }
    }

    private fun updateTradeItemImageLink(tradeItem: TradeItem, tradeItemId: String){
        db.collection(tradeItemsCollection).document(tradeItemId)
            .set(tradeItem)
    }


    fun deleteTradeItem (tradeItem: TradeItem){
        db.collection(tradeItemsCollection).document(tradeItem.id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}