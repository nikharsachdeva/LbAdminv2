package com.laundrybuoy.admin.retrofit

import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.NotificationResponse
import com.laundrybuoy.admin.model.auth.RequestOtpResponse
import com.laundrybuoy.admin.model.auth.VerifyOtpResponse
import com.laundrybuoy.admin.model.customer.CustomerTransactionModel
import com.laundrybuoy.admin.model.filter.ServicesModel
import com.laundrybuoy.admin.model.home.AdminGraphResponse
import com.laundrybuoy.admin.model.home.HomeFiltersModel
import com.laundrybuoy.admin.model.map.MapCoordinatesModel
import com.laundrybuoy.admin.model.order.*
import com.laundrybuoy.admin.model.profile.*
import com.laundrybuoy.admin.model.rider.*
import com.laundrybuoy.admin.model.splash.SplashDataModel
import com.laundrybuoy.admin.model.transaction.TransactionResponse
import com.laundrybuoy.admin.model.unapprovedvendors.UnApprovedVendorsModel
import com.laundrybuoy.admin.model.vendor.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AdminAPI {


    @POST("/auth/getMobile")
    suspend fun requestOTP(@Body payload: JsonObject): Response<RequestOtpResponse>

    @POST("/auth/verify")
    suspend fun verifyOTP(@Body payload: JsonObject): Response<VerifyOtpResponse>

    @GET("/admin/unverifiedPartners")
    suspend fun getUnapprovedVendors(@Query("page") pageNo: Long): Response<UnApprovedVendorsModel>

    @GET("/admin/getAll/{searchable}")
    suspend fun searchAll(@Path("searchable") searchable: String): Response<SearchResultModel>

    @POST("/partners/getAttendance")
    suspend fun getAttendance(@Body payload: JsonObject): Response<VendorAttendanceModelNew>

    @POST("/admin/globalSearch/{filter}")
    suspend fun getAllItems(
        @Path("filter") orderId: String,
        @Query("page") page: Int,
        @Query("search") search: String? = null,
        @Query("status") filterSelected: String? = null,
    ): Response<AllListingResponse>

    @GET("/admin/orderDetails/{orderId}")
    suspend fun getOrderDetails(@Path("orderId") orderId: String): Response<OrderDetailResponse>

    @GET("/admin/getSplashData")
    suspend fun getSplashData(): Response<SplashDataModel>

    @POST("/admin/getConcern")
    suspend fun getConcern(@Body concernPayload: JsonObject): Response<GetConcernResponse>

    @POST("/admin/getNotes")
    suspend fun getNotes(@Body notesPayload: JsonObject): Response<GetNotesResponse>

    @POST("admin/getValidPartner")
    suspend fun getEligibleVendors(@Body orderPayload: JsonObject): Response<EligibleVendorsModel>

    @POST("/admin/updateRider")
    suspend fun updateRider(@Body orderPayload: JsonObject): Response<GeneralResponse>

    @POST("/admin/updatePartner")
    suspend fun updateVendor(@Body orderPayload: JsonObject): Response<GeneralResponse>

    @POST("/admin/getPartnerBalance/{filter}")
    suspend fun getVendorTransactionMetadata(
        @Path("filter") orderId: String,
        @Body partnerPayload: JsonObject,
    ): Response<TransactionResponse>

    @POST("/admin/getPartnerBalance/{filter}")
    suspend fun getVendorTransaction(
        @Path("filter") orderId: String,
        @Query("page") page: Int,
        @Body partnerPayload: JsonObject,
    ): Response<TransactionResponse>

    @POST("/admin/getPartnerGraph")
    suspend fun getPartnerGraphData(
        @Body partnerPayload: JsonObject,
    ): Response<VendorOrderGraphModel>

    @POST("/admin/getPartnerOrders")
    suspend fun getVendorOrder(
        @Query("page") page: Int,
        @Body payload: JsonObject,
    ): Response<VendorOrderListingModel>

    @POST("/admin/settlePartnerBalance")
    suspend fun settlePartner(
        @Body partnerPayload: VendorSettlePayload,
    ): Response<VendorSettleResponse>

    @POST("/admin/getAllProfiles")
    suspend fun getVendorProfile(
        @Body vendorPayload: JsonObject,
    ): Response<VendorProfileModel>

    @POST("/admin/partnerOrders")
    suspend fun getVendorFigures(
        @Body vendorPayload: JsonObject,
    ): Response<VendorFiguresModel>

    @POST("/admin/updateAllProfiles/partner")
    suspend fun updateVendorWorkAddress(
        @Query("userId") userId: String,
        @Body payload: VendorWorkAddressModel,
    ): Response<GeneralResponse>

    @GET("/admin/getServices")
    suspend fun fetchServices(): Response<ServicesModel>

    @Multipart
    @POST("/admin/uploadPhotos")
    suspend fun uploadDocsNew2(@Part image: MultipartBody.Part): Response<GeneralResponse>

    /*

@PUT("/admin/updateAllProfiles/partner")
suspend fun updateServices(
    @Query("userId") userId: String,
    @Body payload: ServicesOfferedPayload): Response<GeneralResponse>

@PUT("/admin/updateAllProfiles/partner")
suspend fun updatePincode(
    @Query("userId") userId: String,
    @Body payload: WorkingPincodePayload): Response<GeneralResponse>

 */

    @POST("/admin/updateAllProfiles/partner")
    suspend fun updateVendorNumber(
        @Query("userId") userId: String,
        @Body payload: JsonObject,
    ): Response<GeneralResponse>

    @POST("/admin/updateAllProfiles/partner")
    suspend fun updateVendor(
        @Query("userId") userId: String,
        @Body payload: JsonObject,
    ): Response<GeneralResponse>

    @POST("/admin/getAllProfiles")
    suspend fun getRiderProfile(
        @Body riderPayload: JsonObject,
    ): Response<RiderProfileModel>

    @POST("/admin/updateAllProfiles/rider")
    suspend fun updateRider(
        @Query("userId") userId: String,
        @Body payload: JsonObject,
    ): Response<GeneralResponse>

    @POST("/admin/getRiderAttendance")
    suspend fun getRiderAttendance(
        @Body payload: JsonObject,
    ): Response<RiderAttendanceModel>

    @GET("/admin/getAllCoordinates/{role}")
    suspend fun getCoordinates(
        @Path("role") role: String,
    ): Response<MapCoordinatesModel>

    @POST("/admin/getBalance")
    suspend fun getRiderTransaction(
        @Body payload: JsonObject,
    ): Response<RiderTransactionModel>

    @POST("/admin/approveClaim")
    suspend fun approveClaim(
        @Body payload: JsonObject,
    ): Response<RiderClaimApproved>

    @POST("/admin/adminGraphData")
    suspend fun getAdminGraph(
        @Body payload: JsonObject,
    ): Response<AdminGraphResponse>


    @POST("/admin/getPartnerRatings")
    suspend fun getPartnerRating(
        @Body payload: JsonObject,
    ): Response<VendorRatingResponse>

    @POST("/admin/getRiderRatings")
    suspend fun getRiderRating(
        @Body payload: JsonObject,
    ): Response<VendorRatingResponse>

    @POST("/admin/getOrders")
    suspend fun getHomeFigures(
        @Body payload: JsonObject,
    ): Response<HomeFiltersModel>

    @POST("/admin/getRiderHomeOrders")
    suspend fun getRiderHomeFigures(
        @Body payload: JsonObject,
    ): Response<RiderHomeFigures>

    @POST("/admin/RiderHomeOrdersData")
    suspend fun getRiderOrderListing(
        @Query("page") page: Int,
        @Body payload: JsonObject,
    ): Response<AllOrderListModel>

    @POST("/admin/partnerOrdersDetails")
    suspend fun getPartnerOrderListing(
        @Query("page") page: Int,
        @Body payload: JsonObject,
    ): Response<AllOrderListModel>

    @POST("/admin/getOrdersList")
    suspend fun getHomeOrderListing(
        @Query("page") page: Int,
        @Body payload: JsonObject,
    ): Response<AllOrderListModel>

    @POST("/admin/approveRiderAttendance")
    suspend fun markRiderAttendance(
        @Body payload: ApproveRiderAttendance,
    ): Response<GeneralResponse>

    @POST("/admin/getRiderGraph")
    suspend fun getRiderChartData(
        @Body payload: JsonObject,
    ): Response<RiderGraphResponse>

    @POST("/admin/getRiderOrders")
    suspend fun getRiderOrderByMonth(
        @Query("page") page: Int,
        @Body payload: JsonObject,
    ): Response<VendorOrderListingModel>

    @GET("/admin/getNotification/{filter}")
    suspend fun getNotifications(
        @Path("filter") filter: String,
        @Query("page") page: Int)
            : Response<NotificationResponse>

    @PUT("/admin/readAllNotifications/{filter}")
    suspend fun readAllNotifications(
        @Path("filter") filter: String)
            : Response<GeneralResponse>

    @DELETE("/admin/deleteNotifications/{filter}")
    suspend fun deleteAllNotifications(
        @Path("filter") filter: String)
            : Response<GeneralResponse>

    @GET("/admin/getQR")
    suspend fun getAllQrCodes(
    ): Response<GetQrResponse>

    @POST("/admin/addQRs")
    suspend fun addQrCodes(@Body payload: AddQrPayload): Response<AddQrResponse>

    @DELETE("/admin/deleteQR/{qrId}")
    suspend fun deleteQr(
        @Path("qrId") qrId: String)
            : Response<DeleteQrResponse>

    @GET("/admin/getCoupons/{filter}")
    suspend fun getAllCoupons(
        @Path("filter") qrId: String
    )
            : Response<GetCouponsResponse>

    @POST("/admin/creteCoupon")
    suspend fun addCoupon(@Body payload: AddCouponPayload): Response<AddCouponResponse>

    @POST("/admin/toggleCoupon")
    suspend fun disableCoupon(
        @Body payload: JsonObject)
            : Response<DeleteCouponResponse>

    @GET("/admin/getPackages/{filter}")
    suspend fun getPackages(
        @Path("filter") filterId: String
    ): Response<GetPackagesResponse>

    @POST("/admin/togglePackage")
    suspend fun disablePackage(
        @Body payload: JsonObject)
            : Response<DeleteCouponResponse>

    @GET("/admin/getSubscription/{filter}")
    suspend fun getSubscription(
        @Path("filter") filterId: String
    ): Response<GetSubscriptionResponse>

    @POST("/admin/toggleSubscription")
    suspend fun disableSubscription(
        @Body payload: JsonObject)
            : Response<DeleteCouponResponse>

    @POST("/admin/postSubscription")
    suspend fun addSubscription(@Body payload: AddSubscriptionPayload): Response<AddSubscriptionResponse>

    @POST("/admin/createPackage")
    suspend fun addPackage(@Body payload: AddPackagePayload): Response<AddPackageResponse>

    @POST("/admin/getPartnerRankings")
    suspend fun getPartnerRankings(@Body payload: LeaderboardPayload): Response<LeaderboardResponse>

    @POST("/admin/getRiderRanking")
    suspend fun getRiderRankings(@Body payload: LeaderboardPayload): Response<LeaderboardResponse>

    @POST("/admin/getUserCoinHistory")
    suspend fun getCustomerCoins(
        @Query("page") page: Int,
        @Body payload: JsonObject,
    ): Response<CustomerTransactionModel>


}