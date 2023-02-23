package com.laundrybuoy.admin.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemOrderReviewBinding
import com.laundrybuoy.admin.model.order.OrderDetailResponse.Data.Rating
import com.laundrybuoy.admin.utils.getRatingAnimation
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible
import com.laundrybuoy.admin.utils.ratingForPerson

class OrderRatingAdapter() :
    ListAdapter<Rating, OrderRatingAdapter.OrderRatingViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRatingViewHolder {
        val binding =
            RowItemOrderReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderRatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderRatingViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let {
            holder.bind(it, position)
        }

    }

    inner class OrderRatingViewHolder(private val binding: RowItemOrderReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doc: Rating, position: Int) {
            val rating = (doc.rating ?: 0).toString()

            if (doc.isRated == true && rating != "0") {
                binding.ratingAvailableLl.makeViewVisible()
                binding.ratingUnAvailableLl.makeViewGone()

                binding.vendorRatingFigTv.text = rating
                binding.vendorRatingLottie.setAnimation(rating.getRatingAnimation())

                val badgesAdapter = OrderRatingBadgeAdapter()
                binding.vendorReviewBadgesRv.layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                binding.vendorReviewBadgesRv.adapter = badgesAdapter
                badgesAdapter.submitList(doc.badges)
            } else {
                binding.ratingAvailableLl.makeViewGone()
                binding.ratingUnAvailableLl.makeViewVisible()
                binding.ratingUnAvailableTypeTv.text =
                    "${doc.ratingFor?.ratingForPerson()} Rating Unavailable"
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Rating>() {
        override fun areItemsTheSame(
            oldItem: Rating,
            newItem: Rating,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Rating,
            newItem: Rating,
        ): Boolean {
            return oldItem == newItem
        }

    }
}