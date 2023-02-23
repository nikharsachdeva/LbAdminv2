package com.laundrybuoy.admin.adapter.vendor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemSelectServiceBinding
import com.laundrybuoy.admin.model.filter.ServicesModel

class SelectServicesAdapter(val context: Context, var serviceList: ArrayList<ServicesModel.Data>, val onClickListener:OnClickInterface) :
    RecyclerView.Adapter<SelectServicesAdapter.SelectServicesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectServicesViewHolder {
        val binding =
            ItemSelectServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectServicesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectServicesViewHolder, position: Int) {
        holder.bind(serviceList[position], position)

    }

    inner class SelectServicesViewHolder(private val binding: ItemSelectServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: ServicesModel.Data, position: Int) {

            binding.rowItemServiceNameTv.text = doc.serviceName ?: "Null"
            binding.isSelected = doc.isSelected
            binding.rowItemServiceContainerCv.setOnClickListener {
                doc.isSelected = !doc.isSelected
                binding.isSelected = doc.isSelected
                notifyDataSetChanged()
                onClickListener.onServiceClicked(serviceList)
            }

        }
    }

    fun getAll(): ArrayList<ServicesModel.Data> {
        return serviceList
    }

    fun addAll(serviceList: ArrayList<ServicesModel.Data>) {
        serviceList.clear()
        for (i in 0 until serviceList.size) {
            addSingle(serviceList[i])
        }
        notifyDataSetChanged()
    }

    private fun addSingle(data: ServicesModel.Data) {
        serviceList.add(data)
    }

    fun getSelected(): ArrayList<ServicesModel.Data>? {
        val selected: ArrayList<ServicesModel.Data> = ArrayList()
        for (i in 0 until serviceList.size) {
            if (serviceList[i].isSelected) {
                selected.add(serviceList[i])
            }
        }
        return selected
    }

    interface OnClickInterface {
        fun onServiceClicked(serviceList: ArrayList<ServicesModel.Data>)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}