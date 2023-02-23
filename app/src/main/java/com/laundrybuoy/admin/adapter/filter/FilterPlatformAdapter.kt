package com.laundrybuoy.admin.adapter.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemSelectPlatformFilterBinding
import com.laundrybuoy.admin.databinding.ItemSelectServiceFilterBinding
import com.laundrybuoy.admin.databinding.ItemSelectStatusFilterBinding
import com.laundrybuoy.admin.model.vendor.VendorServices

class FilterPlatformAdapter(
    val context: Context,
    var platformList: MutableList<VendorServices.VendorServicesItem>
) :
    RecyclerView.Adapter<FilterPlatformAdapter.PlatformViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatformViewHolder {
        val binding =
            ItemSelectPlatformFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlatformViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlatformViewHolder, position: Int) {
        holder.bind(platformList[position], position)
    }

    inner class PlatformViewHolder(private val binding: ItemSelectPlatformFilterBinding) :
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

    fun getSelectedList() = platformList.filter { p -> p.isSelected }

    fun setEmployees(statusList: MutableList<VendorServices.VendorServicesItem>){
        this.platformList= mutableListOf()
        this.platformList=statusList
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return platformList.size
    }

}