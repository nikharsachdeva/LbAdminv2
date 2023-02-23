package com.laundrybuoy.admin.adapter.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemSelectServiceFilterBinding
import com.laundrybuoy.admin.databinding.ItemSelectStatusFilterBinding
import com.laundrybuoy.admin.model.vendor.VendorServices

class FilterServiceAdapter(
    val context: Context,
    var serviceList: MutableList<VendorServices.VendorServicesItem>
) :
    RecyclerView.Adapter<FilterServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding =
            ItemSelectServiceFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(serviceList[position], position)
    }

    inner class ServiceViewHolder(private val binding: ItemSelectServiceFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: VendorServices.VendorServicesItem, position: Int) {
            binding.isSelected = doc.isSelected==true
            binding.rowItemServiceNameTv.text = doc.serviceName ?: "Null"

            binding.rowItemServiceContainerCv.setOnClickListener {
                doc.isSelected=!doc.isSelected
                binding.isSelected = doc.isSelected==true
            }
        }
    }

    fun getSelectedList() = serviceList.filter { p -> p.isSelected }

    fun setEmployees(statusList: MutableList<VendorServices.VendorServicesItem>){
        this.serviceList= mutableListOf()
        this.serviceList=statusList
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}