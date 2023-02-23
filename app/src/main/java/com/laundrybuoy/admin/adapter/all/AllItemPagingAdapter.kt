package com.laundrybuoy.admin.adapter.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.model.profile.AllListingResponse.Data.Partner
import com.laundrybuoy.admin.utils.loadImageWithGlide

class AllItemPagingAdapter(private val onClickListener: OnClickInterface) :
    PagingDataAdapter<Partner, AllItemPagingAdapter.AllItemViewHolder>(
        COMPARATOR
    ) {

    class AllItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemType: TextView = itemView.findViewById(R.id.item_type_tv)
        val itemProfile: ImageView = itemView.findViewById(R.id.item_profile_iv)
        val itemName: TextView = itemView.findViewById(R.id.item_name_value)
        val itemAddress: TextView = itemView.findViewById(R.id.item_address_value)
        val itemClickable: RelativeLayout = itemView.findViewById(R.id.item_all_clickable)
    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            val addressString = item.address?.line1 + " " +
                    item.address?.landmark + " " +
                    item.address?.city + " " +
                    item.address?.pin

            holder.itemAddress.text = addressString
            holder.itemName.text = item.name
//            holder.itemProfile.loadImageWithGlide(item.p)

            when (item.role) {
                "partner" -> {
                    holder.itemType.text = "Partner"
                }
                "rider" -> {
                    holder.itemType.text = "Rider"
                }
                "order" -> {
                    holder.itemType.text = "Order #${item.id}"
                }
                "user" -> {
                    holder.itemType.text = "User"
                }
            }

            holder.itemClickable.setOnClickListener {
                onClickListener.onSelected(item, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_all_list, parent, false)

        return AllItemViewHolder(view)
    }

    interface OnClickInterface {
        fun onSelected(order: Partner, position: Int)
    }

    companion object {
        private val COMPARATOR =
            object : DiffUtil.ItemCallback<Partner>() {
                override fun areItemsTheSame(
                    oldItem: Partner,
                    newItem: Partner,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Partner,
                    newItem: Partner,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}





















