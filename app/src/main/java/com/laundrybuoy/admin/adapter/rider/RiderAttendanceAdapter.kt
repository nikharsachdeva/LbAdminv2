package com.laundrybuoy.admin.adapter.rider

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemRiderAttendanceBinding
import com.laundrybuoy.admin.model.rider.RiderAttendanceModel.Data

class RiderAttendanceAdapter :
    ListAdapter<Data, RiderAttendanceAdapter.RiderAttendanceViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiderAttendanceViewHolder {
        val binding =
            ItemRiderAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RiderAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiderAttendanceViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class RiderAttendanceViewHolder(val binding: ItemRiderAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(attendance: Data, position: Int) {

            binding.rowItemAttendanceTv.text =
                attendance.date?.toString()

            when (attendance.value) {
                "present" -> {
                    binding.rowItemAttendanceLl.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#1030b856"))
                    binding.rowItemAttendanceTv.setTextColor(ColorStateList.valueOf(Color.parseColor(
                        "#30b856")))
                }
                "absent" -> {
                    binding.rowItemAttendanceLl.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#10fc2254"))
                    binding.rowItemAttendanceTv.setTextColor(ColorStateList.valueOf(Color.parseColor(
                        "#fc2254")))
                }
                "default" -> {
                    binding.rowItemAttendanceLl.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#f0f4fa"))
                    binding.rowItemAttendanceTv.setTextColor(ColorStateList.valueOf(Color.parseColor(
                        "#000000")))
                }
            }
        }
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(
            oldItem: Data,
            newItem: Data,
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: Data,
            newItem: Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}