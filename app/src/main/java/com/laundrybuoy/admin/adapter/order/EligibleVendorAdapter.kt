package com.laundrybuoy.admin.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.laundrybuoy.admin.databinding.ItemEligibleVendorsBinding
import com.laundrybuoy.admin.model.order.EligibleVendorsModel.Data
import com.laundrybuoy.admin.utils.getPincodeTags
import com.laundrybuoy.admin.utils.getServicesTags
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible

class EligibleVendorAdapter(
    val onClickListener: OnClickInterface,
) :
    ListAdapter<Data, EligibleVendorAdapter.EligibleViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EligibleViewHolder {
        val binding =
            ItemEligibleVendorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EligibleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EligibleViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class EligibleViewHolder(private val binding: ItemEligibleVendorsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: Data, position: Int) {
            val addressString = doc.workAddress?.line1 + " " +
                    doc.workAddress?.landmark + " " +
                    doc.workAddress?.city + " " +
                    doc.workAddress?.pin

            binding.itemVendorCnameTv.text = doc.name
            binding.itemVendorAddressTv.text = addressString

            binding.assignVendorBtn.setOnClickListener {
                onClickListener.onVendorClicked(doc)
            }

            val pinAdapter = OrderTagsAdapter()
            val serviceAdapter = OrderTagsAdapter()

            binding.vendorPinTagsRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.vendorPinTagsRv.adapter = pinAdapter

            binding.vendorServiceTagsRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.vendorServiceTagsRv.adapter = serviceAdapter

            pinAdapter.submitList(doc.workingPincode.getPincodeTags())
            serviceAdapter.submitList(doc.servicesOffered.getServicesTags())

        }
    }

    interface OnClickInterface {
        fun onVendorClicked(doc: Data)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(
            oldItem: Data,
            newItem: Data,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Data,
            newItem: Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}