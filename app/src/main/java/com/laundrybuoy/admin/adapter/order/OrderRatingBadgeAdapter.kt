package com.laundrybuoy.admin.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemRatingBadgeBinding
import com.laundrybuoy.admin.model.order.OrderDetailResponse.Data.Rating.Badge
import com.laundrybuoy.admin.utils.loadImageWithGlide

class OrderRatingBadgeAdapter :
    ListAdapter<Badge, OrderRatingBadgeAdapter.BadgesViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgesViewHolder {
        val binding =
            RowItemRatingBadgeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return BadgesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BadgesViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class BadgesViewHolder(val binding: RowItemRatingBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(badge: Badge, position: Int) {
            binding.isSelected = badge.isAwarded==true

            binding.itemBadgeTitle.text = badge.badgeId?.name?:""
            binding.itemBadgeIcon.loadImageWithGlide(badge.badgeId?.image?:"")
        }
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<Badge>() {
        override fun areItemsTheSame(
            oldItem: Badge,
            newItem: Badge
        ): Boolean {
            return oldItem.badgeId?.id == newItem.badgeId?.id
        }

        override fun areContentsTheSame(
            oldItem: Badge,
            newItem: Badge
        ): Boolean {
            return oldItem == newItem
        }

    }
}