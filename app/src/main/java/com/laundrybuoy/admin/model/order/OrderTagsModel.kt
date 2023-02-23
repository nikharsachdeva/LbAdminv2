package com.laundrybuoy.admin.model.order


class OrderTagsModel : ArrayList<OrderTagsModel.OrderTagsItem>() {
    data class OrderTagsItem(
        val tagName: String?,
        val tagHex: String?

    )
}