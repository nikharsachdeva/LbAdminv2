package com.laundrybuoy.admin.model.vendor


import com.google.gson.annotations.SerializedName

data class DocsPayload(
    @SerializedName("photoId")
    val photoId: List<String?>?
)