package com.laundrybuoy.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.ItemLeaderboardBinding
import com.laundrybuoy.admin.model.profile.LeaderboardResponse
import com.laundrybuoy.admin.utils.loadImageWithGlide

class LeaderboardAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<LeaderboardResponse.Data, LeaderboardAdapter.LeaderboardViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding =
            ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class LeaderboardViewHolder(private val binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: LeaderboardResponse.Data, position: Int) {

            binding.leaderboardNameTv.text=doc.name
            binding.leaderboardPointsTv.text=doc.points.toString()
            binding.leaderboardImgIv.loadImageWithGlide(doc.profile!!)
            binding.leaderboardPosTv.text=position.plus(4).toString()

            binding.leaderboardRootRl.setOnClickListener {
                onClickListener.onLeaderboardClicked(doc = doc)
            }
        }
    }

    interface OnClickInterface {
        fun onLeaderboardClicked(doc: LeaderboardResponse.Data)
    }



    class ComparatorDiffUtil : DiffUtil.ItemCallback<LeaderboardResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: LeaderboardResponse.Data,
            newItem: LeaderboardResponse.Data
        ): Boolean {
            return oldItem.riderId == newItem.riderId
        }

        override fun areContentsTheSame(
            oldItem: LeaderboardResponse.Data,
            newItem: LeaderboardResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

    }
}