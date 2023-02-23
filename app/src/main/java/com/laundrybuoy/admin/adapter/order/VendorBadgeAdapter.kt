package com.laundrybuoy.admin.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemRatingBadgeBinding
import com.laundrybuoy.admin.model.order.RatingModel
import com.laundrybuoy.admin.utils.loadImageWithGlide

class VendorBadgeAdapter() :
    ListAdapter<RatingModel.Rider.Badge, VendorBadgeAdapter.ScheduledViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledViewHolder {
        val binding =
            RowItemRatingBadgeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ScheduledViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduledViewHolder, position: Int) {

        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class ScheduledViewHolder(private val binding: RowItemRatingBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rating: RatingModel.Rider.Badge, position: Int) {

            binding.isSelected = rating.isSelected==true

            binding.itemBadgeTitle.text = rating.badgeName.toString()
            binding.itemBadgeIcon.loadImageWithGlide(rating.badgeIcon.toString())

        }
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<RatingModel.Rider.Badge>() {
        override fun areItemsTheSame(
            oldItem: RatingModel.Rider.Badge,
            newItem: RatingModel.Rider.Badge
        ): Boolean {
            return oldItem.badgeName == newItem.badgeName
        }

        override fun areContentsTheSame(
            oldItem: RatingModel.Rider.Badge,
            newItem: RatingModel.Rider.Badge
        ): Boolean {
            return oldItem == newItem
        }

    }
}