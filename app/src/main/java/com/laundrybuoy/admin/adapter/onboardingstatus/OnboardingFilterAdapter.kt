package com.laundrybuoy.admin.adapter.onboardingstatus

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.ItemOnboardingFiltersBinding
import com.laundrybuoy.admin.model.unapprovedvendors.OnboardingFilterModel.OnboardingFilterModelItem

class OnboardingFilterAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<OnboardingFilterModelItem, OnboardingFilterAdapter.OnboardingFilterViewHolder>(
        ComparatorDiffUtil()
    ) {

    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingFilterViewHolder {
        val binding =
            ItemOnboardingFiltersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingFilterViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {

            if (position == selectedItemPos) {
                holder.selectedBg()
            } else {
                holder.defaultBg()
            }

            holder.bind(it, position)
        }

    }

    inner class OnboardingFilterViewHolder(val binding: ItemOnboardingFiltersBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(filter: OnboardingFilterModelItem, position: Int) {
            binding.itemFilerNameTv.text = filter.title ?: ""
            binding.itemFilerNameTv.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(filter.backgroundHex))

            binding.itemFilerNameRl.setOnClickListener {

                selectedItemPos = adapterPosition

                lastItemSelectedPos = if (lastItemSelectedPos == -1)
                    selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    selectedItemPos
                }
                notifyItemChanged(selectedItemPos)

                onClickListener.onFilterClicked(filter = filter)

            }


        }

        fun defaultBg() {
            binding.itemFilerNameRl.background =
                binding.root.context.getDrawable(R.drawable.filter_unselected)
        }

        fun selectedBg() {
            binding.itemFilerNameRl.background =
                binding.root.context.getDrawable(R.drawable.filter_selected)
        }
    }

    interface OnClickInterface {
        fun onFilterClicked(filter: OnboardingFilterModelItem)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<OnboardingFilterModelItem>() {
        override fun areItemsTheSame(
            oldItem: OnboardingFilterModelItem,
            newItem: OnboardingFilterModelItem
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: OnboardingFilterModelItem,
            newItem: OnboardingFilterModelItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}