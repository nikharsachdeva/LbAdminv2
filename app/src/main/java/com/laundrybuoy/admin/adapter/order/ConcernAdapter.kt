package com.laundrybuoy.admin.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemConcernBinding
import com.laundrybuoy.admin.model.order.GetConcernResponse
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible

class ConcernAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<GetConcernResponse.Data, ConcernAdapter.ConcernViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcernViewHolder {
        val binding =
            RowItemConcernBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConcernViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConcernViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class ConcernViewHolder(private val binding: RowItemConcernBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: GetConcernResponse.Data, position: Int) {
            binding.itemConcernTypeTv.text=doc.concernType.toString()
            binding.itemConcernDescTv.text=doc.concernDescription.toString()
            if(!doc.profile.isNullOrEmpty()){
                binding.itemConcernAttachment.makeViewVisible()
                binding.itemNoteConcernFig.makeViewVisible()
                binding.itemNoteConcernFig.text=doc.profile.size.toString()
            }else{
                binding.itemConcernAttachment.makeViewGone()
                binding.itemNoteConcernFig.makeViewGone()
            }

            binding.itemConcernClickable.setOnClickListener {
                onClickListener.onConcernClicked(doc = doc)
            }
        }
    }

    interface OnClickInterface {
        fun onConcernClicked(doc: GetConcernResponse.Data)
    }



    class ComparatorDiffUtil : DiffUtil.ItemCallback<GetConcernResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: GetConcernResponse.Data,
            newItem: GetConcernResponse.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetConcernResponse.Data,
            newItem: GetConcernResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

    }
}