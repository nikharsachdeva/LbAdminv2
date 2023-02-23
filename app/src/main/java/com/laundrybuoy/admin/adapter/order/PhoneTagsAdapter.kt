package com.laundrybuoy.admin.adapter.order

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemPhoneTagsBinding
import com.laundrybuoy.admin.model.order.OrderTagsModel

class PhoneTagsAdapter(val onClickListener : PhoneClickListener) :
    ListAdapter<OrderTagsModel.OrderTagsItem, PhoneTagsAdapter.PhoneTagViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneTagViewHolder {
        val binding =
            ItemPhoneTagsBinding.inflate(
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

    interface PhoneClickListener{
        fun onPhoneClick(number : String)
    }

    inner class PhoneTagViewHolder(val binding: ItemPhoneTagsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: OrderTagsModel.OrderTagsItem, position: Int) {

            binding.itemOrderTag.text = service.tagName ?: ""
            binding.itemOrderTag.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#10" + service.tagHex))
            binding.itemOrderTag.setTextColor(
                ColorStateList.valueOf(
                    Color.parseColor(
                        "#"+service.tagHex
                    )
                )
            )

            binding.root.setOnClickListener {
                onClickListener.onPhoneClick(service.tagName?:"")
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