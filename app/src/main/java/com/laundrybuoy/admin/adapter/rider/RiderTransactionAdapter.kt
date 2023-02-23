package com.laundrybuoy.admin.adapter.rider

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.adapter.rider.Const.CREDIT
import com.laundrybuoy.admin.adapter.rider.Const.DEBIT
import com.laundrybuoy.admin.databinding.RowItemCreditBinding
import com.laundrybuoy.admin.databinding.RowItemDebitBinding
import com.laundrybuoy.admin.databinding.RowItemNullTransactionBinding
import com.laundrybuoy.admin.model.rider.RiderTransactionModel.Date.Txn
import com.laundrybuoy.admin.utils.*

class RiderTransactionAdapter(val onClickListener: OnClickInterface) :
    ListAdapter<Txn, RecyclerView.ViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun getItemCount(): Int = currentList.size

    inner class NullTransactionViewHolder(private val rowItemNull: RowItemNullTransactionBinding) :
        RecyclerView.ViewHolder(rowItemNull.root) {
        fun bind(transaction: Txn) {
        }
    }

    inner class DebitViewHolder(private val rowItemDebit: RowItemDebitBinding) :
        RecyclerView.ViewHolder(rowItemDebit.root) {
        fun bind(transaction: Txn) {
            rowItemDebit.transactionCategoryTv.text = transaction.category.toString()
            rowItemDebit.transactionAmtTv.text = "₹" + transaction.amount.toString()
            rowItemDebit.transactionForTv.text = transaction.txnFor.toString().capitalize()
            rowItemDebit.transactionTimeTv.text =
                transaction.date.toString().getFormattedDate()
            rowItemDebit.transactionCurrentBalTv.text =
                "₹" + transaction.curBal.toString()
            if (transaction.description?.isNotEmpty()!!) {
                rowItemDebit.transactionDescTv.text = transaction.description
                rowItemDebit.transactionDescTv.makeViewVisible()
            }

            if (transaction.isApproved == true) {
                rowItemDebit.transactionStatusTv.text = "Approved"
                rowItemDebit.transactionStatusTv.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))
                rowItemDebit.transactionStatusTv.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
            } else {
                rowItemDebit.transactionStatusTv.text = "UnApproved"
                rowItemDebit.transactionStatusTv.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                rowItemDebit.transactionStatusTv.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))
            }

            if(transaction.txnFor=="claim settlement"){
                rowItemDebit.searchLinkedClaimIv.makeViewVisible()
            }else{
                rowItemDebit.searchLinkedClaimIv.makeViewInVisible()
            }

            rowItemDebit.searchLinkedClaimIv.setOnClickListener {
                onClickListener.onSearchClicked(transaction)
            }

            itemView.setOnClickListener {
                onClickListener.onTransactionClicked(transaction)
            }
        }
    }

    inner class CreditViewHolder(private val rowItemCredit: RowItemCreditBinding) :
        RecyclerView.ViewHolder(rowItemCredit.root) {
        fun bind(transaction: Txn) {

            rowItemCredit.transactionCategoryTv.text = transaction.category.toString()
            rowItemCredit.transactionForTv.text = transaction.txnFor.toString().capitalize()
            rowItemCredit.transactionAmtTv.text = "₹" + transaction.amount.toString()
            rowItemCredit.transactionTimeTv.text =
                transaction.date.toString().getFormattedDate()
            rowItemCredit.transactionCurrentBalTv.text =
                "₹" + transaction.curBal.toString()
            if (transaction.description?.isNotEmpty() == true) {
                rowItemCredit.transactionDescTv.text = transaction.description
                rowItemCredit.transactionDescTv.makeViewVisible()
            }

            if (transaction.isApproved == true) {
                rowItemCredit.transactionStatusTv.text = "Approved"
                rowItemCredit.transactionStatusTv.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#1030b856"))
                rowItemCredit.transactionStatusTv.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#30b856"
                        )
                    )
                )
            } else {
                rowItemCredit.transactionStatusTv.text = "UnApproved"
                rowItemCredit.transactionStatusTv.setTextColor(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            "#fc2254"
                        )
                    )
                )
                rowItemCredit.transactionStatusTv.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#10fc2254"))
            }

            if(transaction.txnFor=="claim"){
                rowItemCredit.claimDottedV.makeViewVisible()
                rowItemCredit.claimActionsLl.makeViewVisible()

                if(transaction.isApproved==true){
                    rowItemCredit.approveClaimBtn.makeButtonDisabled()
                    rowItemCredit.rejectClaimBtn.makeButtonDisabled()
                }else{
                    rowItemCredit.approveClaimBtn.makeButtonEnabled()
                    rowItemCredit.rejectClaimBtn.makeButtonEnabled()
                }

            }else{
                rowItemCredit.claimDottedV.makeViewGone()
                rowItemCredit.claimActionsLl.makeViewGone()
            }

            rowItemCredit.approveClaimBtn.setOnClickListener {
                onClickListener.onClaimApproveClicked(transaction)
            }

            rowItemCredit.rejectClaimBtn.setOnClickListener {
                onClickListener.onClaimRejectClicked(transaction)
            }

            itemView.setOnClickListener {
                onClickListener.onTransactionClicked(transaction)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {

        return if(currentList[position].txnFor?.contains("settlement")==true){
            DEBIT
        }else{
            CREDIT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            DEBIT -> {
                val view =
                    RowItemDebitBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return DebitViewHolder(view)
            }
            CREDIT -> {
                val view =
                    RowItemCreditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CreditViewHolder(view)
            }

            else -> {
                val view =
                    RowItemNullTransactionBinding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false)
                return NullTransactionViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DEBIT) {
            (holder as DebitViewHolder).bind(currentList[position]!!)
        } else if (getItemViewType(position) == CREDIT) {
            (holder as CreditViewHolder).bind(currentList[position]!!)
        }
    }

    interface OnClickInterface {
        fun onTransactionClicked(transaction: Txn)
        fun onClaimApproveClicked(transaction: Txn)
        fun onClaimRejectClicked(transaction: Txn)
        fun onSearchClicked(transaction: Txn)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Txn>() {
        override fun areItemsTheSame(
            oldItem: Txn,
            newItem: Txn
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Txn,
            newItem: Txn
        ): Boolean {
            return oldItem == newItem
        }

    }
}

private object Const {
    const val DEBIT = 0
    const val CREDIT = 1
}