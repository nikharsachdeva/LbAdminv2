package com.laundrybuoy.admin

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

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

    /*
    fun <T> handleNetworkError(error: NetworkResult.Error<T>, view: View) {
        if (error.message.equals(
                "Please make sure you have an active internet connection!",
                true
            )
        ) {
            getMainActivity()!!.addFragment(
                true,
                getMainActivity()!!.getVisibleFrame(),
                NoInternetFragment()
            )
        } else {
            view.showSnackBar(error.message!!)
        }
    }

    fun handleLoading(){
        //TODO loading
    }

     */
}