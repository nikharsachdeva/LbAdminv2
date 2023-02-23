package com.laundrybuoy.admin.model.order


import com.google.gson.annotations.SerializedName

data class VendorOrderListingModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("endIndex")
        val endIndex: Int?,
        @SerializedName("len")
        val len: Int?,
        @SerializedName("nextPage")
        val nextPage: Int?,
        @SerializedName("partners")
        val partners: List<Partner>,
        @SerializedName("prevPage")
        val prevPage: Int?,
        @SerializedName("startIndex")
        val startIndex: Int?,
        @SerializedName("totalItems")
        val totalItems: Int?,
        @SerializedName("totalPage")
        val totalPage: Int?
    ) {
        data class Partner(
            @SerializedName("_id")
            val id: String?,
            @SerializedName("orderId")
            val orderId: OrderId
        ) {
            data class OrderId(
                @SerializedName("createdAt")
                val createdAt: String?,
                @SerializedName("deliveryAddress")
                val deliveryAddress: DeliveryAddress?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isDelivered")
                val isDelivered: Boolean?,
                @SerializedName("isPrime")
                val isPrime: Boolean?,
                @SerializedName("items")
                val items: List<Item>,
                @SerializedName("ordNum")
                val ordNum: String?,
                @SerializedName("orderDate")
                val orderDate: String?,
                @SerializedName("orderStatus")
                val orderStatus: Int?,
                @SerializedName("package")
                val packageX: Boolean?,
                @SerializedName("partnerId")
                val partnerId: PartnerId?,
                @SerializedName("pickupDate")
                val pickupDate: String?,
                @SerializedName("riderId")
                val riderId: RiderId?,
                @SerializedName("role")
                val role: String?,
                @SerializedName("serviceId")
                val serviceId: ServiceId?,
                @SerializedName("updatedAt")
                val updatedAt: String?,
                @SerializedName("userId")
                val userId: UserId?,
                @SerializedName("userName")
                val userName: String?
            ) {
                data class DeliveryAddress(
                    @SerializedName("city")
                    val city: String?,
                    @SerializedName("landmark")
                    val landmark: String?,
                    @SerializedName("latitude")
                    val latitude: String?,
                    @SerializedName("line1")
                    val line1: String?,
                    @SerializedName("longitude")
                    val longitude: String?,
                    @SerializedName("pin")
                    val pin: String?,
                    @SerializedName("state")
                    val state: String?
                )
                data class Item(
                    @SerializedName("eqCloths")
                    val eqCloths: Double?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("itemId")
                    val itemId: String?,
                    @SerializedName("itemName")
                    val itemName: String?,
                    @SerializedName("quantity")
                    val quantity: Int?
                )
                data class PartnerId(
                    @SerializedName("address")
                    val address: Address?,
                    @SerializedName("altMobile")
                    val altMobile: String?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("mobile")
                    val mobile: String?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("profile")
                    val profile: String?,
                    @SerializedName("workAddress")
                    val workAddress: WorkAddress?
                ) {
                    data class Address(
                        @SerializedName("city")
                        val city: String?,
                        @SerializedName("landmark")
                        val landmark: String?,
                        @SerializedName("latitude")
                        val latitude: String?,
                        @SerializedName("line1")
                        val line1: String?,
                        @SerializedName("longitude")
                        val longitude: String?,
                        @SerializedName("pin")
                        val pin: String?,
                        @SerializedName("state")
                        val state: String?
                    )
                    data class WorkAddress(
                        @SerializedName("city")
                        val city: String?,
                        @SerializedName("landmark")
                        val landmark: String?,
                        @SerializedName("line1")
                        val line1: String?,
                        @SerializedName("longitude")
                        val longitude: String?,
                        @SerializedName("latitude")
                        val latitude: String?,
                        @SerializedName("pin")
                        val pin: String?,
                        @SerializedName("state")
                        val state: String?
                    )
                }

                data class RiderId(
                    @SerializedName("address")
                    val address: Address?,
                    @SerializedName("altMobile")
                    val altMobile: String?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("mobile")
                    val mobile: String?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("profile")
                    val profile: String?
                ) {
                    data class Address(
                        @SerializedName("city")
                        val city: String?,
                        @SerializedName("landmark")
                        val landmark: String?,
                        @SerializedName("latitude")
                        val latitude: String?,
                        @SerializedName("line1")
                        val line1: String?,
                        @SerializedName("longitude")
                        val longitude: String?,
                        @SerializedName("pin")
                        val pin: String?,
                        @SerializedName("state")
                        val state: String?
                    )
                }

                data class ServiceId(
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("serviceImage")
                    val serviceImage: String?,
                    @SerializedName("serviceName")
                    val serviceName: String?
                )

                data class UserId(
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("mobile")
                    val mobile: String?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("profile")
                    val profile: String?
                )
            }
        }
    }
}