package com.laundrybuoy.admin.adapter.rider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemMarkAttendanceBinding
import com.laundrybuoy.admin.model.rider.RiderAttendanceModel.Data

class MissedAttendanceAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<Data, MissedAttendanceAdapter.MarkAttendanceViewHolder>(
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
            binding.itemAttendanceStatus.text="Missed"
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