package com.example.firebasetest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasetest.databinding.FragmentMapsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager


class MapsFragment : Fragment() {
    private val MY_PERMISSION_REQUEST_ACCESS_FINELOCATION = 101
    var reference = FirebaseDatabase
        .getInstance("https://mapsapplication1-fb977-default-rtdb.firebaseio.com/")
        .getReference("points")

    lateinit var clusterManager: ClusterManager<MyItem>
    lateinit var map: GoogleMap

    lateinit var binding: FragmentMapsBinding


    inner class MyItem(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String,
        type: WifiIconType
    ) : ClusterItem {

        private val position: LatLng
        private val title: String
        private val snippet: String
        private val type: WifiIconType

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String? {
            return title
        }

        override fun getSnippet(): String? {
            return snippet
        }

        fun getType(): WifiIconType? {
            return type
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
            this.type = type
        }
    }


    val spotsListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.value != null) {
                clusterManager.clearItems()
                Log.d("onChangeMaps", "onChangeMaps")
                for(wifiPointSnapshot in snapshot.children){
                    val wifiPoint : WifiPoint? = wifiPointSnapshot.getValue(WifiPoint::class.java)

                    var wifiIconType: WifiIconType

                    if(Firebase.auth.currentUser != null && wifiPoint?.userIdentifier.equals(
                            Firebase.auth.currentUser!!.email)) {
                        wifiIconType = WifiIconType.MY_SPOT
                    } else {
                        wifiIconType = WifiIconType.DEFAULT
                    }

                    wifiPoint?.latitude?.let { Log.d("123", it) }

                    if(wifiPoint != null) {
                        val item = MyItem(
                            wifiPoint.latitude.toDouble(),
                            wifiPoint.longitude.toDouble(),
                            wifiPoint.ssid.trim(),
                            wifiPoint.password.trim(),
                            wifiIconType
                        )

                        clusterManager.addItem(item)
                    }
                }
                clusterManager.cluster()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    private var locationPermission: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        checkLocationPermission()
    }

    fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //  TODO: Prompt with explanation!

                //Prompt the user once explanation has been shown
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_REQUEST_ACCESS_FINELOCATION
                )
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_REQUEST_ACCESS_FINELOCATION
                )
            }
            false
        } else {
            true
        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        map = googleMap

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }

        Log.d("callback", "callback")

        clusterManager = ClusterManager(context, googleMap)
        clusterManager.renderer = CustomClusterRenderer(context, googleMap, clusterManager)

        reference.addValueEventListener(spotsListener)

        googleMap.setOnCameraIdleListener(clusterManager)
    }

    fun goToAdd() {
        var user = Firebase.auth.currentUser

        if(user != null)
            findNavController().navigate(R.id.action_mapsFragment_to_addFragment2)
        else {
            Snackbar.make(requireView(), "You need to be logged in", Snackbar.LENGTH_LONG).setAction("Login"){
                signIn()
            }.show()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == MY_PERMISSION_REQUEST_ACCESS_FINELOCATION){
            val indexOf = permissions.indexOf(Manifest.permission.ACCESS_FINE_LOCATION)
            if(indexOf != -1 && grantResults[indexOf] != PackageManager.PERMISSION_GRANTED){
                Snackbar.make(requireView(), getString(R.string.location_permission_warning), Snackbar.LENGTH_INDEFINITE).setAction(getString(
                                    R.string.retry)){
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_REQUEST_ACCESS_FINELOCATION)
                }.show()
            } else {
                Snackbar.make(requireView(), getString(R.string.permission_granted), Snackbar.LENGTH_SHORT).show()
                map.isMyLocationEnabled = true
            }
        }
    }

    private fun signIn() {
        val signInIntent = MainActivity.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        var auth = Firebase.auth
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    binding.userSpots.visibility = View.VISIBLE
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == MainActivity.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("MapsFragment", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("MapsFragment", "Google sign in failed", e)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity2: AppCompatActivity = activity as AppCompatActivity
        activity2.supportActionBar?.show()
        activity2.supportActionBar?.title = ""

        Log.d("onCreateView", "onCreateView")

        // Inflate the layout for this fragment
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        if(Firebase.auth.currentUser == null)
            binding.userSpots.visibility = View.INVISIBLE

        binding.addButton.setOnClickListener { goToAdd() }
    }

    override fun onDestroyView() {
        reference?.removeEventListener(spotsListener)
        clusterManager.clearItems()
        clusterManager.cluster()

        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item1)
            findNavController().navigate(R.id.action_mapsFragment_to_accountFragment)
        return true
    }

}