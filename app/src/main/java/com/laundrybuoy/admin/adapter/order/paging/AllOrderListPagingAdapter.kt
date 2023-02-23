package com.laundrybuoy.admin.adapter.order.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.OrderTagsAdapter
import com.laundrybuoy.admin.model.order.AllOrderListModel.Data.Partner
import com.laundrybuoy.admin.utils.getFormattedDate
import com.laundrybuoy.admin.utils.getTags

class AllOrderListPagingAdapter(private val onClickListener: OnClickInterface) :
    PagingDataAdapter<Partner, AllOrderListPagingAdapter.PartnerOrderViewHolder>(
        COMPARATOR
    ) {

    class PartnerOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderCName: TextView = itemView.findViewById(R.id.item_order_cname_tv)
        val orderCAddress: TextView = itemView.findViewById(R.id.item_order_address_tv)
        val orderDateTime: TextView = itemView.findViewById(R.id.item_order_date_tv)
        val orderBillStatus: TextView = itemView.findViewById(R.id.item_order_bill_status)
        val orderServiceName: TextView = itemView.findViewById(R.id.item_order_service_tv)
        val orderTagsRv: RecyclerView = itemView.findViewById(R.id.orderTags_rv)
        val orderClickable: RelativeLayout = itemView.findViewById(R.id.row_item_order_clickable)
    }

    override fun onBindViewHolder(holder: PartnerOrderViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            val addressString = item?.deliveryAddress?.line1 + " " +
                    item?.deliveryAddress?.landmark + " " +
                    item?.deliveryAddress?.city + " " +
                    item?.deliveryAddress?.pin
            holder.orderCAddress.text = addressString
            holder.orderCName.text = item?.userId?.name ?: ""
            holder.orderDateTime.text = item?.orderDate?.getFormattedDate() ?: ""

            if (item?.items.isNullOrEmpty()) {
                holder.orderBillStatus.text = "Bill not generated"
            } else {
                var totalQty = 0
                item?.items?.map {
                    totalQty += it?.quantity ?: 0
                }
                holder.orderBillStatus.text = "$totalQty Items"
            }

            holder.orderServiceName.text=item?.serviceId?.serviceName

            val tagAdapter = OrderTagsAdapter()
            holder.orderTagsRv.layoutManager =
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.orderTagsRv.adapter = tagAdapter
            tagAdapter.submitList(item?.getTags())
            holder.orderClickable.setOnClickListener {
                onClickListener.onSelected(item, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerOrderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_order_list, parent, false)

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





















