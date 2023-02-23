package com.laundrybuoy.admin.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.databinding.RowItemPackagesBinding
import com.laundrybuoy.admin.model.profile.GetSubscriptionResponse

class SubscriptionAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<GetSubscriptionResponse.Data, SubscriptionAdapter.CouponsViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponsViewHolder {
        val binding =
            RowItemPackagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponsViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }
    }

    inner class CouponsViewHolder(private val binding: RowItemPackagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(packages: GetSubscriptionResponse.Data, position: Int) {
            when (packages.isActive) {
                true -> {
                    binding.packageActiveStsTv.text = "Enabled"
                    binding.packageActiveStsTv.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#30b856"
                            )
                        )
                    )
                    binding.packageActiveStsTv.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#1030b856"))
                    binding.editPackageBtn.text="Disable Package"
                }
                false -> {
                    binding.packageActiveStsTv.text = "Disabled"
                    binding.packageActiveStsTv.setTextColor(
                        ColorStateList.valueOf(
                            Color.parseColor(
                                "#fc2254"
                            )
                        )
                    )
                    binding.packageActiveStsTv.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#10fc2254"))
                    binding.editPackageBtn.text="Enable Package"

                }
                else -> {
                    binding.packageActiveStsTv.text = ""
                }
            }
            binding.packageNameTv.text = packages.name ?: ""
            binding.packageDescTv.text = packages.description ?: ""
            binding.packagePriceTv.text="₹${packages.discountedPrice}"
            binding.packageValidityTv.text="${packages.validity} days"

            val pointerAdapter = PackagePointerAdapter()
            binding.packagePointsRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            binding.packagePointsRv.adapter = pointerAdapter

            pointerAdapter.submitList(packages.details)

            binding.editPackageBtn.setOnClickListener {
                onClickListener.onSubscriptionClicked(packages = packages)
            }
        }
    }

    interface OnClickInterface {
        fun onSubscriptionClicked(packages: GetSubscriptionResponse.Data)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<GetSubscriptionResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: GetSubscriptionResponse.Data,
            newItem: GetSubscriptionResponse.Data,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetSubscriptionResponse.Data,
            newItem: GetSubscriptionResponse.Data,
        ): Boolean {
            return oldItem == newItem
        }

    }
}