package com.example.wifispotter.activities

import android.content.Context
import android.net.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.firebasetest.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var cm: ConnectivityManager

    val SHA1 = "BD:7B:09:FA:4E:79:0B:8E:D4:88:6F:28:89:75:EE:33:82:8C:B9:31"
    val KEY = "634083435961-celofnaso4parkd2thv1egi7h8lrbql0.apps.googleusercontent.com"

    companion object {
        lateinit var googleSignInClient: GoogleSignInClient
        val RC_SIGN_IN = 123
    }



    private lateinit var auth: FirebaseAuth
    private val TAG = "MainActivity.kt"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("onCreateActivity", "onCreateActivity")

        cm = getSystemService(ConnectivityManager::class.java)

        if(cm.activeNetwork == null){
            Snackbar.make(findViewById(R.id.fragmentContainerView), "No internet connection", Snackbar.LENGTH_LONG).show()
        }

        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network : Network) {
                Log.e(TAG, "The default network is now: " + network)
                Snackbar.make(findViewById(R.id.fragmentContainerView), "Connected", Snackbar.LENGTH_LONG).show()
            }

            override fun onLost(network : Network) {
                Log.e(TAG, "The application no longer has a default network. The last default network was " + network)
                Snackbar.make(findViewById(R.id.fragmentContainerView), "Connection lost", Snackbar.LENGTH_LONG).show()
            }
        })

        auth = Firebase.auth

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(KEY)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        navController = findNavController(R.id.fragmentContainerView)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return false
    }





    fun goToMaps(view: View) {
        //navController.navigate(R.id.action_usersFragment_to_mapsFragment)
    }

    fun zoomOutClick(view: View) {}
    fun moveToMyLocation(view: View) {

    }

    fun goToMySpots(view: View) {
        navController.navigate(R.id.action_mapsFragment_to_itemFragment)
    }

}

