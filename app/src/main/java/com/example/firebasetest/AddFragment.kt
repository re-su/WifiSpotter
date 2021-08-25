package com.example.firebasetest

import android.Manifest
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasetest.databinding.FragmentAddBinding
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.random.Random


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)
        val activity2: AppCompatActivity = activity as AppCompatActivity
        activity2.supportActionBar?.hide()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPositionButton.setOnClickListener {
            saveToDatabase()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveToDatabase(){
        val firebase: FirebaseDatabase = FirebaseDatabase.getInstance("https://mapsapplication1-fb977-default-rtdb.firebaseio.com/")
        val pointsReference = firebase.getReference("points")

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
        val formatted = current.format(formatter)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                var user = Firebase.auth.currentUser

                Log.d("Location", location?.latitude.toString() + " " + location?.longitude.toString())

                val password = binding.password.toString()

                val wifiPoint = WifiPoint(
                    binding.ssid.text.toString().trim(),
                    binding.password.text.toString().trim(),
                    Random.nextDouble(-84.0, 84.0).toString(),
                    Random.nextDouble(-180.0, 180.0).toString(),
                    //location?.latitude.toString(),
                    //location?.longitude.toString(),
                    user?.email.toString()
                )
                pointsReference.child(user?.uid + formatted.toString()).setValue(wifiPoint)
            }

        Snackbar.make(requireView(), getString(R.string.wifiSpotAdded), Snackbar.LENGTH_SHORT).show()
        findNavController().popBackStack()

    }

}