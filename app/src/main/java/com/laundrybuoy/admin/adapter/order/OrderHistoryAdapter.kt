package com.laundrybuoy.admin.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemOrderHistoryBinding
import com.laundrybuoy.admin.model.order.OrderDetailResponse.Data.OrderHistory
import com.laundrybuoy.admin.utils.getFormattedDate
import com.laundrybuoy.admin.utils.getOrderStatus

class OrderHistoryAdapter() :
    ListAdapter<OrderHistory, OrderHistoryAdapter.InventoryListViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryListViewHolder {
        val binding =
            RowItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryListViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class InventoryListViewHolder(private val binding: RowItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: OrderHistory, position: Int) {
            var actionBy: String? = null
            binding.actionDescTv.text = doc.action
            binding.actionDateTv.text = doc.createdAt.toString().getFormattedDate()
            if(doc.userId!=null){
                actionBy = doc.userId.name+" (User)"
            }
            if(doc.partnerId!=null){
                actionBy = doc.partnerId.name+" (Partner)"
            }
            if(doc.riderId!=null){
                actionBy = doc.riderId.name+" (Rider)"
            }
            binding.actionByTv.text = actionBy
            binding.orderStatusTv.text=doc.orderStatus?.getOrderStatus()
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<OrderHistory>() {
        override fun areItemsTheSame(
            oldItem: OrderHistory,
            newItem: OrderHistory
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OrderHistory,
            newItem: OrderHistory
        ): Boolean {
            return oldItem == newItem
        }

    }
}