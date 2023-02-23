package com.laundrybuoy.admin.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["isSelected"])
fun setViewSelection(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}