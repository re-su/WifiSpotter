package com.example.wifispotter.viewadapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.wifispotter.placeholder.WifiSpotPlaceholder.PlaceholderItem
import com.example.firebasetest.databinding.FragmentItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class WifiSpotRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<WifiSpotRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.ssid.text = item.ssid
        holder.password.text = "Password: " + item.password
        holder.latitude.text = "Latitude: " + item.latitude.toString()
        holder.longitude.text = "Longitude: " + item.longitude.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ssid: TextView = binding.ssid
        val password: TextView = binding.password
        val latitude: TextView = binding.latitude
        val longitude: TextView = binding.longitude


        override fun toString(): String {
            return super.toString() + " '" + ssid.text + "'"
        }
    }

}