package com.laundrybuoy.admin.adapter.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemHomeFiltersNewBinding
import com.laundrybuoy.admin.model.home.HomeFiltersModel.Data

class HomeFilterAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<Data, HomeFilterAdapter.HomeFilterViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFilterViewHolder {
        val binding =
            ItemHomeFiltersNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeFilterViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class HomeFilterViewHolder(val binding: ItemHomeFiltersNewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filter: Data, position: Int) {
            binding.homeFilterIconLl.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#10" + filter.hexCode))

            binding.homeFilterIconIv.setColorFilter(Color.parseColor("#"+filter.hexCode));

            binding.homeFilterTitleTv.text = filter.filterName ?: ""
            binding.homeFilterFigTv.text = filter.filterFigure.toString() ?: ""
            binding.homeFilterFigTv.setTextColor(ColorStateList.valueOf(Color.parseColor("#"+filter.hexCode)))

            binding.itemFilerRl.setOnClickListener {
                onClickListener.onFilterClicked(filter = filter)
            }


        }
    }

    interface OnClickInterface {
        fun onFilterClicked(filter: Data)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(
            oldItem: Data,
            newItem: Data
        ): Boolean {
            return oldItem.filterName == newItem.filterName
        }

        override fun areContentsTheSame(
            oldItem: Data,
            newItem: Data
        ): Boolean {
            return oldItem == newItem
        }

    }
}