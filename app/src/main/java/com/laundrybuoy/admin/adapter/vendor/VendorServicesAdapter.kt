package com.laundrybuoy.admin.adapter.vendor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.adapter.SearchAdapter
import com.laundrybuoy.admin.databinding.ItemVendorServicesBinding
import com.laundrybuoy.admin.model.vendor.VendorServices

class VendorServicesAdapter :
    ListAdapter<VendorServices.VendorServicesItem, VendorServicesAdapter.VendorServicesViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorServicesViewHolder {
        val binding =
            ItemVendorServicesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return VendorServicesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorServicesViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class VendorServicesViewHolder(val binding: ItemVendorServicesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: VendorServices.VendorServicesItem, position: Int) {

            binding.rowItemServiceNameTv.text = service.serviceName ?: ""
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<VendorServices.VendorServicesItem>() {
        override fun areItemsTheSame(
            oldItem: VendorServices.VendorServicesItem,
            newItem: VendorServices.VendorServicesItem
        ): Boolean {
            return oldItem.serviceName == newItem.serviceName
        }

        override fun areContentsTheSame(
            oldItem: VendorServices.VendorServicesItem,
            newItem: VendorServices.VendorServicesItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}