package com.laundrybuoy.admin.adapter.vendor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemOrderBreakupBinding
import com.laundrybuoy.admin.model.vendor.VendorOrderBreakupModel.VendorOrderBreakupModelItem

class VendorOrderBreakupAdapter :
    ListAdapter<VendorOrderBreakupModelItem, VendorOrderBreakupAdapter.VendorBreakupViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorBreakupViewHolder {
        val binding =
            ItemOrderBreakupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return VendorBreakupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorBreakupViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class VendorBreakupViewHolder(val binding: ItemOrderBreakupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderBreakup: VendorOrderBreakupModelItem, position: Int) {

            binding.rowItemBreakupTitle.text=orderBreakup.title
            binding.rowItemBreakupScore.text="${orderBreakup.score+"/"+orderBreakup.total}"
            binding.rowItemBreakupProgress.setIndicatorColor(Color.parseColor("#"+orderBreakup.hex))
            binding.rowItemBreakupProgress.trackColor = (Color.parseColor("#10"+orderBreakup.hex))

            val progress = ((orderBreakup.score)?.toFloat()!! / (orderBreakup.total)?.toFloat()!!) * 100
            binding.rowItemBreakupProgress.setProgressCompat(progress.toInt(),true)
        }
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<VendorOrderBreakupModelItem>() {
        override fun areItemsTheSame(
            oldItem: VendorOrderBreakupModelItem,
            newItem: VendorOrderBreakupModelItem
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: VendorOrderBreakupModelItem,
            newItem: VendorOrderBreakupModelItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}