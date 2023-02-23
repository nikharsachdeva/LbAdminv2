package com.laundrybuoy.admin.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemNotesBinding
import com.laundrybuoy.admin.model.order.GetNotesResponse
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible

class NotesAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<GetNotesResponse.Data, NotesAdapter.NotesViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding =
            RowItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class NotesViewHolder(private val binding: RowItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: GetNotesResponse.Data, position: Int) {
            binding.itemNoteTv.text=doc.noteDescription.toString()
            if(!doc.profile.isNullOrEmpty()){
                binding.itemNoteAttachment.makeViewVisible()
                binding.itemNoteAttachmentFig.makeViewVisible()
                binding.itemNoteAttachmentFig.text=doc.profile.size.toString()
            }else{
                binding.itemNoteAttachment.makeViewGone()
                binding.itemNoteAttachmentFig.makeViewGone()
            }

            binding.itemNoteTv.setOnClickListener {
                onClickListener.onNoteClicked(doc = doc)
            }
        }
    }

    interface OnClickInterface {
        fun onNoteClicked(doc: GetNotesResponse.Data)
    }



    class ComparatorDiffUtil : DiffUtil.ItemCallback<GetNotesResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: GetNotesResponse.Data,
            newItem: GetNotesResponse.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetNotesResponse.Data,
            newItem: GetNotesResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

    }
}