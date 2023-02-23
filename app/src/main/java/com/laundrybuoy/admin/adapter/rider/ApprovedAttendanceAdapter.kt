package com.laundrybuoy.admin.adapter.rider

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemMarkAttendanceBinding
import com.laundrybuoy.admin.model.rider.RiderAttendanceModel.Data

class ApprovedAttendanceAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<Data, ApprovedAttendanceAdapter.MarkAttendanceViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkAttendanceViewHolder {
        val binding =
            ItemMarkAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MarkAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkAttendanceViewHolder, position: Int) {

        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class MarkAttendanceViewHolder(private val binding: ItemMarkAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(att: Data, position: Int) {
            binding.isSelected = att.isSelected
            binding.itemAttendanceCb.isChecked = att.isSelected
            binding.itemAttendanceStatus.text=att.value?.capitalize()
            if(att?.value=="present"){
                binding.itemAttendanceStatus.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
                binding.itemAttendanceStatus.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))
            }else if(att?.value=="absent"){
                binding.itemAttendanceStatus.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                binding.itemAttendanceStatus.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))
            }
            binding.itemAttendanceDate.text="${att.date}"
            binding.itemAttendanceCb.setOnCheckedChangeListener { compoundButton, b ->
                att.isSelected = !att.isSelected!!
                binding.isSelected = att.isSelected
                onClickListener.onCheckBoxSelected(att)
            }
        }
    }

    interface OnClickInterface {
        fun onCheckBoxSelected(order: Data)
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(
            oldItem: Data,
            newItem: Data
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: Data,
            newItem: Data
        ): Boolean {
            return oldItem == newItem
        }

    }

}