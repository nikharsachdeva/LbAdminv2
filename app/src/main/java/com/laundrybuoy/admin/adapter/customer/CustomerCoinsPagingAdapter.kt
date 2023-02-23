package com.laundrybuoy.admin.adapter.customer

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.model.customer.CustomerTransactionModel.Data.Partner
import com.laundrybuoy.admin.utils.getFormattedDateWithTime

class CustomerCoinsPagingAdapter(private val onClickListener: OnClickInterface) :
    PagingDataAdapter<Partner, CustomerCoinsPagingAdapter.PartnerOrderViewHolder>(
        COMPARATOR
    ) {

    class PartnerOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinTranRoot: RelativeLayout = itemView.findViewById(R.id.coin_transaction_root_rl)
        val coinTranIv: ImageView = itemView.findViewById(R.id.coin_transaction_iv)
        val coinTranHeading: TextView = itemView.findViewById(R.id.coin_transaction_heading_tv)
        val coinTranSubHeading: TextView = itemView.findViewById(R.id.coin_transaction_subhead_tv)
        val coinTranValue: TextView = itemView.findViewById(R.id.coin_transaction_value_tv)
        val coinTranClosingBal: TextView = itemView.findViewById(R.id.coin_closing_bal_tv)
    }

    override fun onBindViewHolder(holder: PartnerOrderViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            if (item.orderId != null) {
                holder.coinTranHeading.text = "Paid for #${item?.orderId?.ordNum}"
                holder.coinTranValue.text = "-${item.balanceUsed}"

                holder.coinTranValue.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                holder.coinTranValue.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))

            } else {
                holder.coinTranHeading.text = "Received"
                holder.coinTranValue.text = "+${item.balanceUsed}"

                holder.coinTranValue.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
                holder.coinTranValue.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))

            }
            holder.coinTranSubHeading.text = item.transactionDate?.getFormattedDateWithTime()
            holder.coinTranClosingBal.text = "Coins closing balance : ${item.balanceCurrent}"

            holder.coinTranRoot.setOnClickListener {
                onClickListener.onSelected(item, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerOrderViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_customer_coins, parent, false)

        return PartnerOrderViewHolder(view)
    }

    interface OnClickInterface {
        fun onSelected(order: Partner, position: Int)
    }

    companion object {
        private val COMPARATOR =
            object : DiffUtil.ItemCallback<Partner>() {
                override fun areItemsTheSame(
                    oldItem: Partner,
                    newItem: Partner,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Partner,
                    newItem: Partner,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}





















