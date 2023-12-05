package com.example.swapsies.ui.additem

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.swapsies.R
import com.example.swapsies.model.TradeItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class AddItemFragment : Fragment() {

    private lateinit var viewModel: AddItemViewModel
    private lateinit var imageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var mAuth: FirebaseAuth

    private val FILE_NAME = "photo.jpg"
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var photoFile: File
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(AddItemViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        mAuth = FirebaseAuth.getInstance()
        photoFile = getPhotoFile()

        val root = inflater.inflate(R.layout.fragment_additem, container, false)
        val addItemButton = root.findViewById<Button>(R.id.add_item_button)
        nameEditText = root.findViewById(R.id.item_name_edit_text)
        descriptionEditText = root.findViewById(R.id.item_description_edit_text)

        imageView = root.findViewById(R.id.item_image_view)

        imageView.setOnClickListener {
            dispatchTakePictureIntent()
        }

        addItemButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            if (checkInput(name, description) && viewModel.localImageFile != null) {
                getLocationAndCreateTradeItem()
            } else {
                Toast.makeText(requireContext(), getString(R.string.required_fields), Toast.LENGTH_LONG).show()
            }
        }

        viewModel.localImageFile?.let { file ->
            val image = BitmapFactory.decodeFile(file.absolutePath)
            imageView.setImageBitmap(image)
        }

        return root
    }

    private fun createTradeItem(location: String){
        val name = nameEditText.text.toString()
        val description = descriptionEditText.text.toString()
        viewModel.name.value = name
        viewModel.description.value = description
        val tradeItem = TradeItem("", mAuth.currentUser!!.email!!, name, description, location, mAuth.currentUser!!.uid, null, null,null)
        viewModel.addTradeItem(tradeItem)
        Toast.makeText(requireContext(), getString(R.string.upload_success), Toast.LENGTH_LONG).show()
        clearInput()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileProvider = FileProvider.getUriForFile(this.requireContext(), "com.example.swapsies.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun clearInput(){
        nameEditText.text.clear()
        descriptionEditText.text.clear()
        imageView.setImageResource(R.drawable.ic_baseline_add_a_photo_24)
        viewModel.localImageFile = null
    }

    private fun checkInput(name: String,
                           description: String): Boolean {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) && imageView.drawable != null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val image = BitmapFactory.decodeFile(photoFile.absolutePath)
            imageView.setImageBitmap(image)
            viewModel.localImageFile = photoFile
        }
    }

    private fun getPhotoFile(): File {
        val storageDirectory = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(FILE_NAME, ".jpg", storageDirectory)
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askLocationPermission()
        }
    }

    private fun getLocationAndCreateTradeItem() {
        try {
            val locationTask = fusedLocationProviderClient.lastLocation
            locationTask.addOnCompleteListener{
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    createTradeItem("Unknown location")
                } else {
                    createTradeItem(getGeocodeFromLocation(it))
                }
            }
        } catch (e: SecurityException) {
            createTradeItem("Unknown location")
        }
    }

    private fun askLocationPermission(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    private fun getGeocodeFromLocation(location: Task<Location>): String{
        if(location.result == null){
            return "Unknown location"
        }
        val geoCoder = Geocoder(requireContext())
        val decodedLocation = geoCoder.getFromLocation(
            location.result.latitude,
            location.result.longitude,
            1
        ).first()
        return "${decodedLocation.locality}, ${decodedLocation.countryName}"
    }
}