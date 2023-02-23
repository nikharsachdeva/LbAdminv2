package com.laundrybuoy.admin.adapter.unapprovedvendors

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.laundrybuoy.admin.databinding.ItemLayoutUnapprovedVendorsBinding
import com.laundrybuoy.admin.model.unapprovedvendors.UnApprovedVendorsModel.Data.Partner

class UnapprovedVendorsAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<Partner, UnapprovedVendorsAdapter.UnApprovedVendorViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnApprovedVendorViewHolder {
        val binding =
            ItemLayoutUnapprovedVendorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnApprovedVendorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UnApprovedVendorViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class UnApprovedVendorViewHolder(private val binding: ItemLayoutUnapprovedVendorsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(partner: Partner, position: Int) {
            binding.itemUnapprovedVendorName.text=partner.id?:""
            binding.itemUnapprovedVendorName.setOnClickListener {
                onClickListener.onItemClicked(partner = partner)
            }
        }
    }

    interface OnClickInterface {
        fun onItemClicked(partner: Partner)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<Partner>() {
        override fun areItemsTheSame(
            oldItem: Partner,
            newItem: Partner
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Partner,
            newItem: Partner
        ): Boolean {
            return oldItem == newItem
        }

    }
}