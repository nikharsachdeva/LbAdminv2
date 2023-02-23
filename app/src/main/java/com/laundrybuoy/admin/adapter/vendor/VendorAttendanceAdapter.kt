package com.laundrybuoy.admin.adapter.vendor

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemVendorAttendanceBinding
import com.laundrybuoy.admin.model.vendor.VendorAttendanceModel
import com.laundrybuoy.admin.utils.getWeekDayName

class VendorAttendanceAdapter :
    ListAdapter<VendorAttendanceModel.VendorAttendanceModelItem, VendorAttendanceAdapter.VendorAttendanceViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorAttendanceViewHolder {
        val binding =
            ItemVendorAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return VendorAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorAttendanceViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class VendorAttendanceViewHolder(val binding: ItemVendorAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(attendance: VendorAttendanceModel.VendorAttendanceModelItem, position: Int) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.rowItemAttendanceTv.text =
                    attendance.date?.getWeekDayName()?.get(0).toString()
            }
            if (attendance.present == true) {
                binding.rowItemAttendanceLl.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))
                binding.rowItemAttendanceTv.setTextColor(ColorStateList.valueOf(Color.parseColor("#30b856")))
            } else {
                binding.rowItemAttendanceLl.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))
                binding.rowItemAttendanceTv.setTextColor(ColorStateList.valueOf(Color.parseColor("#fc2254")))

            }
        }
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<VendorAttendanceModel.VendorAttendanceModelItem>() {
        override fun areItemsTheSame(
            oldItem: VendorAttendanceModel.VendorAttendanceModelItem,
            newItem: VendorAttendanceModel.VendorAttendanceModelItem
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: VendorAttendanceModel.VendorAttendanceModelItem,
            newItem: VendorAttendanceModel.VendorAttendanceModelItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}