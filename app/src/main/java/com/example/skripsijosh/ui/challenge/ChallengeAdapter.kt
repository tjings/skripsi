package com.example.skripsijosh.ui.challenge

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsijosh.R
import com.example.skripsijosh.pojo.ChallengeDetails
import com.example.skripsijosh.pojo.UserStreak

class ChallengeAdapter (private val dataSet: MutableList<ChallengeDetails>,
                        private val listener:  ChallengeListener,
                        private val context: Context,
                        private val userStreak: Int,
                        private val userWater: Int) : RecyclerView.Adapter<ChallengeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvChallengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
        val tvChallengeDesc: TextView = itemView.findViewById(R.id.tvChallengeDesc)
        val tvProgress: TextView = itemView.findViewById(R.id.tvProgress)
        val progressBarChallenge: ProgressBar = itemView.findViewById(R.id.progressBarChallenge)
        val ivChallenge: ImageView = itemView.findViewById(R.id.ivChallenge)
        val tvCompleted: TextView = itemView.findViewById(R.id.tvCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.challenge_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachProgress = if (userStreak > 0 && userWater > 0) {
            if (dataSet[position].streakNeeded == 0) {
                userWater / dataSet[position].waterNeeded * 100
            } else {
                userStreak / dataSet[position].streakNeeded * 100
            }
        } else {0}
        holder.tvChallengeName.text = dataSet[position].name
        holder.tvChallengeDesc.text = dataSet[position].desc
        holder.tvProgress.text = if (dataSet[position].streakNeeded == 0) {
            TextUtils.concat(String.format(context.getString(R.string.challenge_progress), userWater, dataSet[position].waterNeeded) + " mL")
        } else {
            TextUtils.concat(String.format(context.getString(R.string.challenge_progress), userStreak, dataSet[position].streakNeeded) + " Streak")
        }
        if (eachProgress >= 100) {
            holder.tvCompleted.visibility = View.VISIBLE
            listener.onProgressCompleted(dataSet[position].medalGot)
        }
        holder.progressBarChallenge.progress = eachProgress
    }

    override fun getItemCount() = dataSet.size

    interface ChallengeListener {
        fun onProgressCompleted(medalGot: Int)
    }
}