package com.laundrybuoy.admin.adapter.all

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemOrderTagsBinding
import com.laundrybuoy.admin.model.splash.SplashDataModel

class AllFilterAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<SplashDataModel.SplashData.FilterObj, AllFilterAdapter.OrderTagViewHolder>(
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

        fun bind(service: SplashDataModel.SplashData.FilterObj, position: Int) {


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
                onClickListener.onFilterSelected(service)
            }

            binding.itemOrderTag.text = service.filterName?:""

        }
    }

    interface OnClickInterface {
        fun onFilterSelected(filter: SplashDataModel.SplashData.FilterObj)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<SplashDataModel.SplashData.FilterObj>() {
        override fun areItemsTheSame(
            oldItem: SplashDataModel.SplashData.FilterObj,
            newItem: SplashDataModel.SplashData.FilterObj
        ): Boolean {
            return oldItem.filterQuery == newItem.filterQuery
        }

        override fun areContentsTheSame(
            oldItem: SplashDataModel.SplashData.FilterObj,
            newItem: SplashDataModel.SplashData.FilterObj
        ): Boolean {
            return oldItem == newItem
        }

    }
}