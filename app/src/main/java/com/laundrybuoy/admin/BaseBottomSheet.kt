package com.laundrybuoy.admin

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheet : BottomSheetDialogFragment() {

    var mainActivity1: MainActivity? = null
    var isFragmentContainerVisible: Boolean? = false


    fun getMainActivity(): MainActivity? {
        return mainActivity1
    }

    fun setMainActivity(mainActivity: MainActivity?) {
        this.mainActivity1 = mainActivity!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMainActivity(activity as MainActivity?)

    }

    fun isFragmentContainerVisible(): Boolean {
        return isFragmentContainerVisible!!
    }

    fun setFragmentContainerVisible(fragmentContainerVisible: Boolean) {
        isFragmentContainerVisible = fragmentContainerVisible
    }
}