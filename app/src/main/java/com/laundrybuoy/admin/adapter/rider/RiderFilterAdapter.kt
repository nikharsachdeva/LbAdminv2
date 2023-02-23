package com.laundrybuoy.admin.adapter.rider

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemVendorFiguresBinding
import com.laundrybuoy.admin.model.rider.RiderHomeFigures.Data.Response

class RiderFilterAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<Response, RiderFilterAdapter.RiderFilterViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiderFilterViewHolder {
        val binding =
            ItemVendorFiguresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RiderFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiderFilterViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class RiderFilterViewHolder(val binding: ItemVendorFiguresBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filter: Response, position: Int) {
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
        fun onFilterClicked(filter: Response)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<Response>() {
        override fun areItemsTheSame(
            oldItem: Response,
            newItem: Response
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Response,
            newItem: Response
        ): Boolean {
            return oldItem == newItem
        }

    }
}