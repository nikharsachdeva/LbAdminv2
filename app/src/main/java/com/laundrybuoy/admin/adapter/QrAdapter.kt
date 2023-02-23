package com.laundrybuoy.admin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.laundrybuoy.admin.databinding.ItemUploadedDocsBinding
import com.laundrybuoy.admin.model.UploadedDocsModel.UploadedDocsModelItem
import com.laundrybuoy.admin.model.profile.GetQrResponse
import com.laundrybuoy.admin.utils.loadImageWithGlide
import java.time.Instant

class QrAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<GetQrResponse.Data, QrAdapter.UploadImageViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImageViewHolder {
        val binding =
            ItemUploadedDocsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UploadImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UploadImageViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class UploadImageViewHolder(private val binding: ItemUploadedDocsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: GetQrResponse.Data, position: Int) {
            binding.itemDocImgIv.loadImageWithGlide(doc.qrImage ?: "")
            binding.itemDocNameTv.text = doc.name
            binding.itemDocStsTv.text = doc.description

            binding.itemDocDelIv.setOnClickListener {
                onClickListener.onDeleteClicked(position = adapterPosition, doc = doc)
            }

            binding.itemDocImgCv.setOnClickListener {
                onClickListener.onImageClicked(doc = doc)
            }
        }
    }

    interface OnClickInterface {
        fun onImageClicked(doc: GetQrResponse.Data)
        fun onDeleteClicked(position: Int, doc: GetQrResponse.Data)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<GetQrResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: GetQrResponse.Data,
            newItem: GetQrResponse.Data,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetQrResponse.Data,
            newItem: GetQrResponse.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}