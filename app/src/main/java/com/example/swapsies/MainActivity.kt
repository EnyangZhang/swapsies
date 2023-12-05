package com.example.swapsies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val shouldShowSplashScreen = sp.getBoolean(getString(R.string.splash_screen_key), true)

        if(shouldShowSplashScreen){
            Handler().postDelayed({
                navigateToMainApp(user)
            }, 2000)
        } else {
            navigateToMainApp(user)
        }

    }

    private fun navigateToMainApp(user: FirebaseUser?){
        if(user != null){
            val dashboardIntent = Intent(this, DashboardActivity::class.java)
            startActivity(dashboardIntent)
        } else {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
        finish()
    }
}