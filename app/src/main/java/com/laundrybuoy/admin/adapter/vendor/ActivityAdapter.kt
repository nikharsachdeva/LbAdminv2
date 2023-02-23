package com.laundrybuoy.admin.adapter.vendor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemActivityBinding
import com.laundrybuoy.admin.model.vendor.VendorActivityModel.VendorActivityModelItem

class ActivityAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<VendorActivityModelItem, ActivityAdapter.ActivityViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding =
            ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class ActivityViewHolder(val binding: ItemActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: VendorActivityModelItem, position: Int) {

            binding.rowItemActivityId.text = activity.activityId ?: ""
            binding.rowItemActivityDateTime.text = activity.activityDate ?: ""
            binding.rowItemActivityOperationTitle.text = activity.activityOperation ?: ""
            binding.rowItemActivityOperationDesc.text = activity.activityDesc ?: ""

            binding.rowItemActivityRootRl.setOnClickListener {
                onClickListener.onActivityClicked(activity = activity)
            }


        }
    }

    interface OnClickInterface {
        fun onActivityClicked(activity: VendorActivityModelItem)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<VendorActivityModelItem>() {
        override fun areItemsTheSame(
            oldItem: VendorActivityModelItem,
            newItem: VendorActivityModelItem
        ): Boolean {
            return oldItem.activityId == newItem.activityId
        }

        override fun areContentsTheSame(
            oldItem: VendorActivityModelItem,
            newItem: VendorActivityModelItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}