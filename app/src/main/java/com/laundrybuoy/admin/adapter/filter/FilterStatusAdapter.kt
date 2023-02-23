package com.laundrybuoy.admin.adapter.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemSelectStatusFilterBinding
import com.laundrybuoy.admin.model.vendor.VendorServices

class FilterStatusAdapter(
    val context: Context,
    var statusList: MutableList<VendorServices.VendorServicesItem>
) :
    RecyclerView.Adapter<FilterStatusAdapter.StatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding =
            ItemSelectStatusFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(statusList[position], position)
    }

    inner class StatusViewHolder(private val binding: ItemSelectStatusFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: VendorServices.VendorServicesItem, position: Int) {
            binding.isStatusSelected = doc.isSelected==true
            binding.rowItemStatusNameTv.text = doc.serviceName ?: "Null"

            binding.rowItemStatusContainerCv.setOnClickListener {
                doc.isSelected=!doc.isSelected
                binding.isStatusSelected = doc.isSelected==true
            }
        }
    }

    fun getSelectedList() = statusList.filter { p -> p.isSelected }

    fun setEmployees(statusList: MutableList<VendorServices.VendorServicesItem>){
        this.statusList= mutableListOf()
        this.statusList=statusList
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return statusList.size
    }

}