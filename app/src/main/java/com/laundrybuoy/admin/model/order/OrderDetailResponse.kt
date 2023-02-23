package com.laundrybuoy.admin.model.order


import com.google.gson.annotations.SerializedName

data class OrderDetailResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("bill")
        val bill: Bill?,
        @SerializedName("orderDetails")
        val orderDetails: OrderDetails?,
        @SerializedName("orderHistory")
        val orderHistory: List<OrderHistory?>?,
        @SerializedName("partnerPayment")
        val partnerPayment: PartnerPayment?,
        @SerializedName("ratings")
        val ratings: List<Rating?>?
    ) {
        data class Bill(
            @SerializedName("actualDeliveryCost")
            val actualDeliveryCost: Double?,
            @SerializedName("billedCloths")
            val billedCloths: Double?,
            @SerializedName("billedWeight")
            val billedWeight: Double?,
            @SerializedName("coinDiscount")
            val coinDiscount: Double?,
            @SerializedName("coinUsed")
            val coinUsed: Double?,
            @SerializedName("couponDiscount")
            val couponDiscount: Double?,
            @SerializedName("couponStatus")
            val couponStatus: CouponStatus?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("deliveryCost")
            val deliveryCost: Double?,
            @SerializedName("grossTotal")
            val grossTotal: Double?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("items")
            val items: List<Item?>?,
            @SerializedName("netTotal")
            val netTotal: Double?,
            @SerializedName("orderId")
            val orderId: String?,
            @SerializedName("otp")
            val otp: Int?,
            @SerializedName("paymentType")
            val paymentType: String?,
            @SerializedName("remainingCoins")
            val remainingCoins: Double?,
            @SerializedName("serviceCost")
            val serviceCost: Double?,
            @SerializedName("subscriptionStatus")
            val subscriptionStatus: SubscriptionStatus?,
            @SerializedName("totalCloths")
            val totalCloths: Double?,
            @SerializedName("totalWeight")
            val totalWeight: Double?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("usedSubscriptionBalance")
            val usedSubscriptionBalance: Double?,
            @SerializedName("__v")
            val v: Int?
        ) {
            data class CouponStatus(
                @SerializedName("id")
                val id: String?,
                @SerializedName("message")
                val message: String?,
                @SerializedName("name")
                val name: String?
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

            data class SubscriptionStatus(
                @SerializedName("id")
                val id: String?,
                @SerializedName("message")
                val message: String?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("subBalance")
                val subBalance: Double?,
                @SerializedName("subType")
                val subType: String?,
                @SerializedName("usrSub")
                val usrSub: String?
            )
        }

        data class OrderDetails(
            @SerializedName("adminCreated")
            val adminCreated: Boolean?,
            @SerializedName("approxCloths")
            val approxCloths: String?,
            @SerializedName("billId")
            val billId: String?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("deliveredOn")
            val deliveredOn: String?,
            @SerializedName("deliveryAddress")
            val deliveryAddress: DeliveryAddress?,
            @SerializedName("deliveryDate")
            val deliveryDate: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isDelivered")
            val isDelivered: Boolean?,
            @SerializedName("isPrime")
            val isPrime: Boolean?,
            @SerializedName("items")
            val items: List<Item?>?,
            @SerializedName("nextAction")
            val nextAction: String?,
            @SerializedName("ordNum")
            val ordNum: String?,
            @SerializedName("orderDate")
            val orderDate: String?,
            @SerializedName("orderStatus")
            val orderStatus: Int?,
            @SerializedName("otp")
            val otp: Int?,
            @SerializedName("package")
            val packageX: Boolean?,
            @SerializedName("partnerId")
            val partnerId: PartnerId?,
            @SerializedName("pickupDate")
            val pickupDate: String?,
            @SerializedName("ratings")
            val ratings: List<String?>?,
            @SerializedName("riderId")
            val riderId: RiderId?,
            @SerializedName("role")
            val role: String?,
            @SerializedName("serviceId")
            val serviceId: ServiceId?,
            @SerializedName("tagIds")
            val tagIds: List<Int?>?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("userId")
            val userId: UserId?,
            @SerializedName("userName")
            val userName: String?,
            @SerializedName("__v")
            val v: Int?
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
            )

            data class RiderId(
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
            )

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

        data class OrderHistory(
            @SerializedName("action")
            val action: String?,
            @SerializedName("actualTime")
            val actualTime: String?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("expectedTime")
            val expectedTime: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isLate")
            val isLate: Boolean?,
            @SerializedName("ordNum")
            val ordNum: String?,
            @SerializedName("orderId")
            val orderId: String?,
            @SerializedName("orderStatus")
            val orderStatus: Int?,
            @SerializedName("partnerId")
            val partnerId: PartnerId?,
            @SerializedName("riderId")
            val riderId: RiderId?,
            @SerializedName("role")
            val role: String?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("userId")
            val userId: UserId?,
            @SerializedName("__v")
            val v: Int?
        ) {
            data class PartnerId(
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
            )

            data class RiderId(
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
            )

            data class UserId(
                @SerializedName("_id")
                val id: String?,
                @SerializedName("mobile")
                val mobile: String?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("profile")
                val profile: String?,
                @SerializedName("tagId")
                val tagId: Int?
            )
        }

        data class PartnerPayment(
            @SerializedName("amount")
            val amount: Double?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("cur_bal")
            val curBal: Int?,
            @SerializedName("date")
            val date: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isApproved")
            val isApproved: Boolean?,
            @SerializedName("isCompleted")
            val isCompleted: Boolean?,
            @SerializedName("isSetteled")
            val isSetteled: Boolean?,
            @SerializedName("items")
            val items: List<Item?>?,
            @SerializedName("ordNum")
            val ordNum: String?,
            @SerializedName("orderId")
            val orderId: String?,
            @SerializedName("partnerId")
            val partnerId: String?,
            @SerializedName("profile")
            val profile: String?,
            @SerializedName("serviceCost")
            val serviceCost: Double?,
            @SerializedName("totalCloths")
            val totalCloths: Double?,
            @SerializedName("totalWeight")
            val totalWeight: Double?,
            @SerializedName("txn_for")
            val txnFor: String?,
            @SerializedName("txn_type")
            val txnType: String?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("settlementDate")
            val settlementDate: String?,
            @SerializedName("__v")
            val v: Int?
        ) {
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
        }

        data class Rating(
            @SerializedName("badges")
            val badges: List<Badge?>?,
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isRated")
            val isRated: Boolean?,
            @SerializedName("orderId")
            val orderId: String?,
            @SerializedName("partnerId")
            val partnerId: String?,
            @SerializedName("rating")
            val rating: Int?,
            @SerializedName("rating_for")
            val ratingFor: String?,
            @SerializedName("riderId")
            val riderId: String?,
            @SerializedName("updatedAt")
            val updatedAt: String?,
            @SerializedName("__v")
            val v: Int?
        ) {
            data class Badge(
                @SerializedName("badgeId")
                val badgeId: BadgeId?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("isAwarded")
                val isAwarded: Boolean?
            ) {
                data class BadgeId(
                    @SerializedName("badgeFor")
                    val badgeFor: String?,
                    @SerializedName("createdAt")
                    val createdAt: String?,
                    @SerializedName("_id")
                    val id: String?,
                    @SerializedName("image")
                    val image: String?,
                    @SerializedName("updatedAt")
                    val updatedAt: String?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("__v")
                    val v: Int?
                )
            }
        }
    }
}