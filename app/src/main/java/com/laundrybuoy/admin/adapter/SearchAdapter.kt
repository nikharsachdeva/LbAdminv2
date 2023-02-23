package com.laundrybuoy.admin.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.ItemHomeFiltersBinding
import com.laundrybuoy.admin.databinding.ItemHomeFiltersNewBinding
import com.laundrybuoy.admin.databinding.ItemSearchBinding
import com.laundrybuoy.admin.model.SearchModel.SearchModelItem
import com.laundrybuoy.admin.model.order.SearchResultModel

class SearchAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<SearchResultModel.Data, SearchAdapter.SearchViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val filter = getItem(position)
        filter?.let {
            holder.bind(it, position)
        }

    }

    inner class SearchViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchItem: SearchResultModel.Data, position: Int) {

            searchItem.role.let {
                when {
                    it.equals("order") -> {
                        binding.itemSearchIconIv.setColorFilter(Color.parseColor("#00b7fd"));
                        binding.itemSearchIconIv.setImageResource(R.drawable.testing_icon)
                        binding.itemSearchIconLl.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#1000b7fd"))
                        binding.itemSearchTypeTv.text = "Order"
                        binding.itemSearchValueTv.setTextColor(ColorStateList.valueOf(Color.parseColor("#00b7fd")))
                    }
                    it.equals("customer") -> {
                        binding.itemSearchIconIv.setColorFilter(Color.parseColor("#fd2dbf"));
                        binding.itemSearchIconLl.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#10fd2dbf"))
                        binding.itemSearchTypeTv.text = "Customer"
                        binding.itemSearchIconIv.setImageResource(R.drawable.user_icon)
                        binding.itemSearchValueTv.setTextColor(ColorStateList.valueOf(Color.parseColor("#fd2dbf")))

                    }
                    it.equals("partner") -> {
                        binding.itemSearchIconIv.setColorFilter(Color.parseColor("#ff6b01"));
                        binding.itemSearchIconLl.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#10ff6b01"))
                        binding.itemSearchTypeTv.text = "Partner"
                        binding.itemSearchIconIv.setImageResource(R.drawable.washing_machine)
                        binding.itemSearchValueTv.setTextColor(ColorStateList.valueOf(Color.parseColor("#ff6b01")))

                    }
                    it.equals("delivery") -> {
                        binding.itemSearchIconIv.setColorFilter(Color.parseColor("#561fff"));
                        binding.itemSearchIconLl.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#10561fff"))
                        binding.itemSearchTypeTv.text = "Delivery"
                        binding.itemSearchIconIv.setImageResource(R.drawable.delivery_truck)
                        binding.itemSearchValueTv.setTextColor(ColorStateList.valueOf(Color.parseColor("#561fff")))

                    }
                }
            }

            binding.itemSearchValueTv.text=searchItem.name.toString()

            binding.itemSearchParentRl.setOnClickListener {
                onClickListener.onSearchClicked(searchItem = searchItem)
            }


        }
    }

    interface OnClickInterface {
        fun onSearchClicked(searchItem: SearchResultModel.Data)
    }


    class ComparatorDiffUtil : DiffUtil.ItemCallback<SearchResultModel.Data>() {
        override fun areItemsTheSame(
            oldItem: SearchResultModel.Data,
            newItem: SearchResultModel.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SearchResultModel.Data,
            newItem: SearchResultModel.Data
        ): Boolean {
            return oldItem == newItem
        }

    }
}