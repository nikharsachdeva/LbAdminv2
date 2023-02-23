package com.laundrybuoy.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemPackagePointBinding

class PackagePointerAdapter() :
    ListAdapter<String, PackagePointerAdapter.CouponsViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponsViewHolder {
        val binding =
            RowItemPackagePointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponsViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class CouponsViewHolder(private val binding: RowItemPackagePointBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(point: String, position: Int) {
            binding.pointerTv.text = point ?: ""
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean {
            return oldItem == newItem
        }

    }
}