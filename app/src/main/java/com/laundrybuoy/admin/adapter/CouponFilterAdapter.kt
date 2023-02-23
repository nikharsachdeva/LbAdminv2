package com.laundrybuoy.admin.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemOrderTagsBinding
import com.laundrybuoy.admin.model.splash.SplashDataModel

class CouponFilterAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<String, CouponFilterAdapter.OrderTagViewHolder>(
        ComparatorDiffUtil()
    ) {

    private var checkedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderTagViewHolder {
        val binding =
            ItemOrderTagsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return OrderTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderTagViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class OrderTagViewHolder(val binding: ItemOrderTagsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filter: String, position: Int) {


            if(checkedPosition==-1){
                binding.isSelected=false
            }else{
                binding.isSelected = checkedPosition==adapterPosition
            }

            binding.rowItemAllFilterRl.setOnClickListener {
                binding.isSelected=true
                if(checkedPosition!=adapterPosition){
                    notifyItemChanged(checkedPosition)
                    checkedPosition=adapterPosition
                }
                onClickListener.onFilterSelected(filter)
            }

            binding.itemOrderTag.text = filter.capitalize()?:""

        }
    }

    interface OnClickInterface {
        fun onFilterSelected(filter: String)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem== newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

    }
}