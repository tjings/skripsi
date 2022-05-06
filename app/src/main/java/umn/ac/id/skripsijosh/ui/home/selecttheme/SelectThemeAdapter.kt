package umn.ac.id.skripsijosh.ui.home.selecttheme

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import umn.ac.id.skripsijosh.R

class SelectThemeAdapter (private val dataSet: MutableList<String>,
                            private val activity: Activity) : RecyclerView.Adapter<SelectThemeAdapter.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val ivTheme : ImageView = itemView.findViewById(R.id.ivTheme)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dataSet[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.theme_owned, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img: Int = activity.resources.getIdentifier(dataSet[position], "drawable", activity.packageName)
        holder.ivTheme.setImageResource(img)
    }

    override fun getItemCount(): Int = dataSet.size
}