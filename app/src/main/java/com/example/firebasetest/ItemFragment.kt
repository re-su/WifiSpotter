package com.example.firebasetest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetest.placeholder.PlaceholderContent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity2: AppCompatActivity = activity as AppCompatActivity
        activity2.supportActionBar?.hide()

        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var user = Firebase.auth.currentUser

        val firebase: FirebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.firebaseReference))
        val reference = firebase.getReference("points").orderByChild("userIdentifier").equalTo(user?.email)

        Log.d("q321", PlaceholderContent.ITEMS.size.toString())

        val usersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null) {
                    PlaceholderContent.clearItems()
                    for(wifiPointSnapshot in snapshot.children){
                        val wifiPoint : WifiPoint? = wifiPointSnapshot.getValue(WifiPoint::class.java)

                        wifiPoint?.latitude?.let { Log.d("123", it) }

                        if(wifiPoint != null) {
                            Log.d("Placeholder", "point-added")
                            PlaceholderContent.addItem(
                                PlaceholderContent.PlaceholderItem(
                                    wifiPointSnapshot.key!!,
                                    wifiPoint.password,
                                    wifiPoint.ssid.trim(),
                                    wifiPoint.latitude.toDouble(),
                                    wifiPoint.longitude.toDouble(),
                                )
                            )
                        }
                    }
                }

                if(view is RecyclerView){
                    with(view){
                        layoutManager = LinearLayoutManager(context)
                        adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        reference.addValueEventListener(usersListener)
    }
}