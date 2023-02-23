package com.laundrybuoy.admin.model.unapprovedvendors


import com.google.gson.annotations.SerializedName

class OnboardingFilterModel : ArrayList<OnboardingFilterModel.OnboardingFilterModelItem>(){
    data class OnboardingFilterModelItem(
        @SerializedName("backgroundHex")
        val backgroundHex: String?,
        @SerializedName("title")
        val title: String?
    )
}