package com.laundrybuoy.admin.adapter.vendor

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemVendorFiguresBinding
import com.laundrybuoy.admin.model.vendor.VendorFiguresModel.VendorFiltersModelItem

class VendorFilterAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<VendorFiltersModelItem, VendorFilterAdapter.VendorFilterViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorFilterViewHolder {
        val binding =
            ItemVendorFiguresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VendorFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorFilterViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class VendorFilterViewHolder(val binding: ItemVendorFiguresBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filter: VendorFiltersModelItem, position: Int) {
            binding.rowItemVendorFigureRl.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#10" + filter.hexCode))

            binding.rowItemVendorFigureTv.text = filter.filterFigure.toString() ?: ""
            binding.rowItemVendorTitleTv.text = filter.filterName ?: ""
            binding.rowItemVendorFigureTv.setTextColor(ColorStateList.valueOf(Color.parseColor("#"+filter.hexCode)))

            binding.rowItemVendorFigureRl.setOnClickListener {
                onClickListener.onFilterClicked(filter = filter)
            }


        }
    }

    interface OnClickInterface {
        fun onFilterClicked(filter: VendorFiltersModelItem)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<VendorFiltersModelItem>() {
        override fun areItemsTheSame(
            oldItem: VendorFiltersModelItem,
            newItem: VendorFiltersModelItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: VendorFiltersModelItem,
            newItem: VendorFiltersModelItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}