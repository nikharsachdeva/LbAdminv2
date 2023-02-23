package com.laundrybuoy.admin.model


import com.google.gson.annotations.SerializedName

class UploadedDocsModel : ArrayList<UploadedDocsModel.UploadedDocsModelItem>(){

    data class UploadedDocsModelItem(
        @SerializedName("_id")
        val id: String?,
        @SerializedName("image")
        val image: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("status")
        val status: String?=null
    )
}