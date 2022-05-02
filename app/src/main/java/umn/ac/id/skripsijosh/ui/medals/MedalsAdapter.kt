package umn.ac.id.skripsijosh.ui.medals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.pojo.UserMedal

class MedalsAdapter(private val dataSet: MutableList<UserMedal>) : RecyclerView.Adapter<MedalsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val medalName: TextView = itemView.findViewById(R.id.tvMedalName)
        val medalDesc: TextView = itemView.findViewById(R.id.tvMedalDesc)
        val medalPict: ImageView = itemView.findViewById(R.id.ivMedal)
//        init {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.unlocked_medal_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.medalName.text = dataSet[position].name
        holder.medalDesc.text = dataSet[position].desc
    }

    override fun getItemCount() = dataSet.size

}