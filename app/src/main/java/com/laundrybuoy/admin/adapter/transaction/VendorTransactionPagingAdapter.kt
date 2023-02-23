package com.laundrybuoy.admin.adapter.transaction

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.all.AllItemPagingAdapter
import com.laundrybuoy.admin.model.profile.AllListingResponse
import com.laundrybuoy.admin.model.transaction.TransactionResponse.Data.Result.Partner
import com.laundrybuoy.admin.utils.getFormattedDateWithTime
import com.laundrybuoy.admin.utils.makeViewGone
import com.laundrybuoy.admin.utils.makeViewVisible

class VendorTransactionPagingAdapter(private val onClickListener: OnClickInterface) :
    PagingDataAdapter<Partner, VendorTransactionPagingAdapter.TransactionViewHolder>(
        COMPARATOR
    ) {

    private var selectedTransactionList: MutableList<Partner> = arrayListOf()
    private var transactionList: MutableList<Partner> = arrayListOf()

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionStatus: TextView = itemView.findViewById(R.id.row_transaction_status)
        val transactionSettlementDate: TextView = itemView.findViewById(R.id.row_transaction_date)
        val transactionOrderNo: TextView = itemView.findViewById(R.id.row_item_transaction_title)
        val transactionDate: TextView = itemView.findViewById(R.id.row_item_transaction_date)
        val transactionValue: TextView = itemView.findViewById(R.id.row_item_transaction_value)
        val transactionRoot: LinearLayout = itemView.findViewById(R.id.row_item_transaction_root)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            if (item.isSetteled == true) {
                holder.transactionStatus.text = "Settled"
                holder.transactionStatus.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
                holder.transactionStatus.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))

                holder.transactionSettlementDate.text =
                    item.settlementDate?.getFormattedDateWithTime()
                holder.transactionSettlementDate.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10ff8935"))
                holder.transactionSettlementDate.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#ff8935"
                        )
                    )
                )
                holder.transactionValue.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
                holder.transactionSettlementDate.makeViewVisible()
            } else {
                holder.transactionStatus.text = "UnSettled"
                holder.transactionStatus.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                holder.transactionStatus.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))
                holder.transactionValue.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                holder.transactionSettlementDate.makeViewGone()

                holder.transactionRoot.setOnClickListener {
                    if (selectedTransactionList.contains(item)) {
                        selectedTransactionList.remove(item)
                        unhighlightView(holder)
                    } else {
                        selectedTransactionList.add(item)
                        highlightView(holder)
                    }

                    onClickListener.onSelected(selectedTransactionList)
                }

            }
            holder.transactionValue.text = "₹${item.amount}"
            holder.transactionOrderNo.text = "for ${item.ordNum}"
            holder.transactionDate.text = "on ${item.date?.getFormattedDateWithTime()}"


            if (selectedTransactionList.contains(item)) {
                highlightView(holder);
            } else {
                unhighlightView(holder);
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_transaction, parent, false)

        return TransactionViewHolder(view)
    }

    private fun highlightView(holder: TransactionViewHolder) {
        holder.itemView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#6ECFBD"))
    }

    private fun unhighlightView(holder: TransactionViewHolder) {
        holder.itemView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#303B58"))
    }

    fun addAll(items: MutableList<Partner>) {
        clearAll(false)
        transactionList = items
        notifyDataSetChanged()
    }

    private fun clearAll(isNotify: Boolean) {
        transactionList.clear()
        selectedTransactionList.clear()
        if (isNotify) notifyDataSetChanged()
    }

    fun clearSelected() {
        selectedTransactionList.clear()
        onClickListener.onSelected(selectedTransactionList)
        notifyDataSetChanged()
    }

    fun selectAll() {
        selectedTransactionList.clear()
        val unSettledItems = transactionList.filter {
            it.isSetteled==false
        }
        selectedTransactionList.addAll(unSettledItems)
        onClickListener.onSelected(selectedTransactionList)
        notifyDataSetChanged()
    }

    fun getSelected(): MutableList<Partner> {
        return selectedTransactionList
    }


    interface OnClickInterface {
        fun onSelected(selectedList : MutableList<Partner>)
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





















