package com.laundrybuoy.admin.adapter.notification

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.model.NotificationResponse.Data.Partner
import com.laundrybuoy.admin.utils.loadImageWithGlide
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible

class NotificationPagingAdapter(private val onClickListener: OnClickInterface) :
    PagingDataAdapter<Partner, NotificationPagingAdapter.NotificationViewHolder>(
        COMPARATOR
    ) {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val notificationClickable: LinearLayout = itemView.findViewById(R.id.row_item_notification_root)
        val rowItemNotificationTitle : TextView = itemView.findViewById(R.id.row_item_notification_title)
        val rowItemNotificationDesc : TextView = itemView.findViewById(R.id.row_item_notification_desc)
        val rowItemNotificationIv : ImageView = itemView.findViewById(R.id.row_item_notification_iv)
        val rowItemNotificationRl : RelativeLayout = itemView.findViewById(R.id.row_item_notification_rl)
        val rowItemNotificationCircleIv : ImageView = itemView.findViewById(R.id.row_item_notification_circle_iv)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            holder.rowItemNotificationTitle.text=item.notificationHeading.toString()
            holder.rowItemNotificationDesc.text=item.notificationContent.toString()
            holder.rowItemNotificationIv.loadImageWithGlide("https://i.ibb.co/52h9H2z/ordericon.png")
//            binding.rowItemNotificationIv.loadImageWithGlide(doc.notificationIcon?:"")
            if(item.isOpened==true){
                holder.rowItemNotificationRl.background=null
                holder.rowItemNotificationCircleIv.makeViewGone()
            }else{
                holder.rowItemNotificationCircleIv.makeViewVisible()
                holder.rowItemNotificationRl.background = ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.rounded_button_4px)
                holder.rowItemNotificationRl.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#f5f5f5"))
            }


            holder.notificationClickable.setOnClickListener {
                onClickListener.onSelected(item, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_notification, parent, false)

        return NotificationViewHolder(view)
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





















