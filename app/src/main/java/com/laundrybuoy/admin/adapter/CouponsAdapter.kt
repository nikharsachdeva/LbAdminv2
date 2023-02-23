package com.laundrybuoy.admin.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemCouponBinding
import com.laundrybuoy.admin.model.profile.GetCouponsResponse
import com.laundrybuoy.admin.utils.loadImageWithGlide

class CouponsAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<GetCouponsResponse.Data, CouponsAdapter.CouponsViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponsViewHolder {
        val binding =
            RowItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponsViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class CouponsViewHolder(private val binding: RowItemCouponBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coupon: GetCouponsResponse.Data, position: Int) {
            binding.couponImg.loadImageWithGlide("https://cdn-icons-png.flaticon.com/512/2990/2990564.png")
            binding.couponTitleTv.text = coupon.name ?: ""
            binding.couponDescriptionTv.text = coupon.description ?: ""

            when (coupon.isActive) {
                true -> {
                    binding.couponActiveStsTv.text = "Enabled"
                    binding.couponActiveStsTv.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#30b856"
                            )
                        )
                    )
                    binding.couponActiveStsTv.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#1030b856"))
                }
                false -> {
                    binding.couponActiveStsTv.text = "Disabled"
                    binding.couponActiveStsTv.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#fc2254"
                            )
                        )
                    )
                    binding.couponActiveStsTv.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#10fc2254"))
                }
                else -> {
                    binding.couponActiveStsTv.text = ""
                }
            }

            itemView.setOnClickListener {
                onClickListener.onCouponClicked(coupon = coupon)
            }
        }
    }

    interface OnClickInterface {
        fun onCouponClicked(coupon: GetCouponsResponse.Data)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<GetCouponsResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: GetCouponsResponse.Data,
            newItem: GetCouponsResponse.Data,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetCouponsResponse.Data,
            newItem: GetCouponsResponse.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}