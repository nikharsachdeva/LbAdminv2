package com.laundrybuoy.admin.adapter.order

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemOrderTagsBinding
import com.laundrybuoy.admin.databinding.ItemVendorServicesBinding
import com.laundrybuoy.admin.model.order.OrderTagsModel

class OrderTagsAdapter :
    ListAdapter<OrderTagsModel.OrderTagsItem, OrderTagsAdapter.OrderTagViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderTagViewHolder {
        val binding =
            ItemVendorServicesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return OrderTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderTagViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class OrderTagViewHolder(val binding: ItemVendorServicesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: OrderTagsModel.OrderTagsItem, position: Int) {
            binding.rowItemServiceNameTv.text = service.tagName ?: ""
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<OrderTagsModel.OrderTagsItem>() {
        override fun areItemsTheSame(
            oldItem: OrderTagsModel.OrderTagsItem,
            newItem: OrderTagsModel.OrderTagsItem
        ): Boolean {
            return oldItem.tagName == newItem.tagName
        }

        override fun areContentsTheSame(
            oldItem: OrderTagsModel.OrderTagsItem,
            newItem: OrderTagsModel.OrderTagsItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}