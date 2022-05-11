package umn.ac.id.skripsijosh.ui.leaderboard

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.pojo.UserStreak
import umn.ac.id.skripsijosh.utils.Util

class LeaderboardAdapter(private val leaderboardData: MutableList<UserStreak>, private val context: Context) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLeaderboardName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvLeaderboardRanking: TextView = itemView.findViewById(R.id.tvUserRanking)
        val tvHighestStreak: TextView = itemView.findViewById(R.id.tvHighestStreak)
        val ivLeaderboard: ImageView = itemView.findViewById(R.id.ivLeaderboard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.leaderboard_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(Util.isNotNull(leaderboardData[position].displayPic)) {
            Picasso.get()
                .load(leaderboardData[position].displayPic)
                .fit()
                .centerCrop()
                .into(holder.ivLeaderboard)
        }
        val ranking = position + 1
        holder.tvLeaderboardName.text = leaderboardData[position].userName
        holder.tvHighestStreak.text = String.format(context.getString(R.string.highest_streak_leaderboard), leaderboardData[position].highestStreak)
        holder.tvLeaderboardRanking.text = ranking.toString()
    }

    override fun getItemCount() = leaderboardData.size
}
