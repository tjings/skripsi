package umn.ac.id.skripsijosh.ui.shop

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.Reflection.getPackageName
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.pojo.ShopItem
import umn.ac.id.skripsijosh.pojo.UserInventory

class ShopAdapter (private val dataSet:  MutableList<ShopItem>,
                   private val inventory: MutableList<String>,
                   private val userLevel: Int,
                   private val activity: Activity): RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    var onItemClick: ((ShopItem) -> Unit)? = null

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val clShopItem: ConstraintLayout = itemView.findViewById(R.id.clShopItem)
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvItemDesc: TextView = itemView.findViewById(R.id.tvItemDesc)
        val tvEarned: TextView = itemView.findViewById(R.id.tvEarned)
        val tvInsufficientLv: TextView = itemView.findViewById(R.id.tvInsufficientLv)
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
        inventory.forEach {
            if(it == dataSet[position].itemId) {
                holder.tvEarned.visibility = View.VISIBLE
                holder.itemView.isEnabled = false
                holder.clShopItem.visibility = View.VISIBLE
            }
        }
        if (userLevel < dataSet[position].reqLevel) {
            holder.tvInsufficientLv.text = String.format(activity.getString(R.string.insuff_level), dataSet[position].reqLevel)
            holder.tvInsufficientLv.visibility = View.VISIBLE
            holder.itemView.isEnabled = false
            holder.clShopItem.visibility = View.VISIBLE
        }
        val img: Int = activity.resources.getIdentifier(dataSet[position].itemId, "drawable", activity.packageName)
        holder.ivItemPic.setImageResource(img)
        holder.tvItemName.text = dataSet[position].itemName
        holder.tvItemDesc.text = dataSet[position].itemDesc
        holder.tvItemPrice.text = dataSet[position].itemPrice.toString()
    }

    override fun getItemCount() = dataSet.size
}