package com.laundrybuoy.admin.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.laundrybuoy.admin.model.auth.RequestOtpResponse
import com.laundrybuoy.admin.model.auth.VerifyOtpResponse
import com.laundrybuoy.admin.model.filter.ServicesModel
import com.laundrybuoy.admin.repository.AuthRepository
import com.laundrybuoy.admin.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel(){

}