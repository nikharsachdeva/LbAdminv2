package com.laundrybuoy.admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.laundrybuoy.admin.databinding.ItemSelectStatusFilterBinding
import com.laundrybuoy.admin.model.vendor.VendorServices

class SingleAdapter(
    val context: Context,
    var statusList: MutableList<VendorServices.VendorServicesItem>
) :
    RecyclerView.Adapter<SingleAdapter.SingleViewHolder>() {

    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleAdapter.SingleViewHolder {
        val binding =
            ItemSelectStatusFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SingleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleAdapter.SingleViewHolder, position: Int) {
        holder.bind(statusList[position], position)
    }

    inner class SingleViewHolder(private val binding: ItemSelectStatusFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: VendorServices.VendorServicesItem, position: Int) {
            if(checkedPosition==-1){
                binding.isStatusSelected=false
            }else{
                binding.isStatusSelected = checkedPosition==adapterPosition
            }
            binding.rowItemStatusNameTv.text = doc.serviceName ?: "Null"
            binding.rowItemStatusContainerCv.setOnClickListener {
                binding.isStatusSelected=true
                if(checkedPosition!=adapterPosition){
                    notifyItemChanged(checkedPosition)
                    checkedPosition=adapterPosition
                }
            }
        }
    }

    fun getSelectedItem():VendorServices.VendorServicesItem?{
        return if(checkedPosition!=-1){
            statusList[checkedPosition]
        } else{
            null
        }
    }

    fun setEmployees(statusList: MutableList<VendorServices.VendorServicesItem>){
        this.statusList= mutableListOf()
        this.statusList=statusList
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return statusList.size
    }

}