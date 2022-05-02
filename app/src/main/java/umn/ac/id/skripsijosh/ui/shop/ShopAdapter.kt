package umn.ac.id.skripsijosh.ui.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.pojo.ShopItem

class ShopAdapter (private val dataSet:  MutableList<ShopItem>): RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    var onItemClick: ((ShopItem) -> Unit)? = null

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvItemDesc: TextView = itemView.findViewById(R.id.tvItemDesc)
        val ivItemPic: ImageView = itemView.findViewById(R.id.ivItemPic)
        val tvItemPrice: TextView = itemView.findViewById(R.id.tvItemPrice)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dataSet[adapterPosition])
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItemName.text = dataSet[position].itemName
        holder.tvItemDesc.text = dataSet[position].itemDesc
        holder.tvItemPrice.text = dataSet[position].itemPrice.toString()
    }

    override fun getItemCount() = dataSet.size
}