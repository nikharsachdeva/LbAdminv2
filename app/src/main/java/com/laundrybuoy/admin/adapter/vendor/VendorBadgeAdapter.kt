package com.laundrybuoy.admin.adapter.vendor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemBadgeBinding
import com.laundrybuoy.admin.model.vendor.VendorRatingResponse.Data.Badge
import com.laundrybuoy.admin.utils.loadImageWithGlide

class VendorBadgeAdapter :
    ListAdapter<Badge, VendorBadgeAdapter.VendorRatingViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorRatingViewHolder {
        val binding =
            RowItemBadgeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return VendorRatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorRatingViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class VendorRatingViewHolder(val binding: RowItemBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(badge: Badge, position: Int) {
            binding.itemBadgeIconIv.loadImageWithGlide(badge.badge?.image?:"")
            binding.itemBadgeHeadingTv.text = (badge.badge?.name?:"")
            binding.itemBadgeCountTv.text = (badge.count.toString()?:"")
        }
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<Badge>() {
        override fun areItemsTheSame(
            oldItem: Badge,
            newItem: Badge
        ): Boolean {
            return oldItem.badge?.id == newItem.badge?.id
        }

        override fun areContentsTheSame(
            oldItem: Badge,
            newItem: Badge
        ): Boolean {
            return oldItem == newItem
        }

    }
}