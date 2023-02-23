package com.laundrybuoy.admin.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemBillingItemsBinding
import com.laundrybuoy.admin.model.order.OrderDetailResponse.Data.Bill.Item

class OrderItemAdapter() :
    ListAdapter<Item, OrderItemAdapter.InventoryListViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryListViewHolder {
        val binding =
            RowItemBillingItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryListViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class InventoryListViewHolder(private val binding: RowItemBillingItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: Item, position: Int) {
            binding.itemInventoryName.text = doc.itemName
            binding.itemInventoryQty.text = doc.quantity.toString()
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem == newItem
        }

    }
}