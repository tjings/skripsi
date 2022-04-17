package com.example.skripsijosh.ui.challenge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsijosh.R
import com.example.skripsijosh.pojo.ChallengeDetails

class ChallengeAdapter (private val dataSet: MutableList<ChallengeDetails>) : RecyclerView.Adapter<ChallengeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val challengeName: TextView = itemView.findViewById(R.id.tvMedalName)
        val challengeDesc: TextView = itemView.findViewById(R.id.tvMedalDesc)
        val challengePict: ImageView = itemView.findViewById(R.id.ivMedal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_unlocked_medals, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.challengeName.text = dataSet[position].name
        holder.challengeDesc.text = dataSet[position].desc
    }

    override fun getItemCount() = dataSet.size
}