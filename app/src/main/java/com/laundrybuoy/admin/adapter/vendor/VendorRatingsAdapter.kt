package com.laundrybuoy.admin.adapter.vendor

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.laundrybuoy.admin.databinding.ItemVendorRatingBinding
import com.laundrybuoy.admin.model.vendor.VendorRatingResponse
import com.laundrybuoy.admin.model.vendor.VendorRatingResponse.Data.Rating
import com.laundrybuoy.admin.utils.getNumberStringToInt
import kotlin.math.roundToInt

class VendorRatingsAdapter :
    ListAdapter<Rating, VendorRatingsAdapter.VendorRatingViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorRatingViewHolder {
        val binding =
            ItemVendorRatingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return VendorRatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorRatingViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class VendorRatingViewHolder(val binding: ItemVendorRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rating: Rating, position: Int) {
            val totalFigObj = currentList.find {
                it.name=="totalRatings"
            }
            var totalRatings = 0.0
            if(totalFigObj!=null){
                totalRatings = totalFigObj.figure?:0.0
            }

            binding.vendorRatingTypeTv.text="${rating.name?.getNumberStringToInt()} stars"

            val progress = ((rating.figure)?.toFloat()!! / (totalRatings)?.toFloat()!!) * 100
            binding.vendorRatingPercentTv.text=rating.figure.toInt().toString()
            binding.vendorRatingProgress.setProgressCompat(progress.toInt(),true)
        }
    }

    class ComparatorDiffUtil :
        DiffUtil.ItemCallback<Rating>() {
        override fun areItemsTheSame(
            oldItem: Rating,
            newItem: Rating
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Rating,
            newItem: Rating
        ): Boolean {
            return oldItem == newItem
        }

    }
}