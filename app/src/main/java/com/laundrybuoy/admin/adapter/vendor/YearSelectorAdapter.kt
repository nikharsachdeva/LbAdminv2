package com.laundrybuoy.admin.adapter.vendor

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemPhoneTagsBinding
import com.laundrybuoy.admin.databinding.ItemYearTagsBinding
import com.laundrybuoy.admin.model.order.OrderTagsModel

class YearSelectorAdapter(val onClickListener : YearClickListener) :
    ListAdapter<OrderTagsModel.OrderTagsItem, YearSelectorAdapter.PhoneTagViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneTagViewHolder {
        val binding =
            ItemYearTagsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PhoneTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhoneTagViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    interface YearClickListener{
        fun onYearClick(year : String)
    }

    inner class PhoneTagViewHolder(val binding: ItemYearTagsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: OrderTagsModel.OrderTagsItem, position: Int) {

            binding.itemYearTag.text = service.tagName ?: ""

            binding.root.setOnClickListener {
                onClickListener.onYearClick(service.tagName?:"")
            }

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