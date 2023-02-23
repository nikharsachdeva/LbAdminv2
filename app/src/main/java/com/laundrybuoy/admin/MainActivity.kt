package com.laundrybuoy.admin

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.laundrybuoy.admin.databinding.ActivityMainBinding
import com.laundrybuoy.admin.model.home.AdminGraphResponse
import com.laundrybuoy.admin.model.rider.RiderGraphResponse
import com.laundrybuoy.admin.model.vendor.VendorOrderGraphModel
import com.laundrybuoy.admin.ui.*
import com.laundrybuoy.admin.ui.order.WasteFragment
import com.laundrybuoy.admin.ui.order.OrdersFragment
import com.laundrybuoy.admin.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ParentActivity(), View.OnClickListener {

    //Ritvi
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        manageClicks()
        //add Splash Screen
        addFragment(true, 1, SplashFragment())
    }

    private fun manageClicks() {
        binding.bottomNavLayout.ivTabHome?.setOnClickListener(this)
        binding.bottomNavLayout.ivTabMap?.setOnClickListener(this)
        binding.bottomNavLayout.ivTabSearch?.setOnClickListener(this)
        binding.bottomNavLayout.ivTabSetting?.setOnClickListener(this)
        binding.bottomNavLayout.ivTabOrder?.setOnClickListener(this)
    }

    fun showSnackBar(message: String?) {
        hideKeyboard()
        Snackbar.make(binding.rlMainView, message!!, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivTabHome, R.id.ivTabMap, R.id.ivTabSearch, R.id.ivTabSetting, R.id.ivTabOrder -> handleBottomNavTabClicks(
                view
            )
        }
    }

    private fun handleBottomNavTabClicks(view: View) {
        when (view.id) {
            R.id.ivTabHome -> {
                if (binding.rlFragmentContainer1.visibility == View.VISIBLE) {
                    if (fragmentStack1.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }
                binding.rlFragmentContainer1.visibility = View.VISIBLE
                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.BOLD
                )

                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.black))
                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    ), PorterDuff.Mode.SRC_IN
                )
                binding.rlFragmentContainer2.visibility = View.GONE
                binding.rlFragmentContainer3.visibility = View.GONE
                binding.rlFragmentContainer4.visibility = View.GONE
                binding.rlFragmentContainer5.visibility = View.GONE

                binding.bottomNavLayout.tvTabMap.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabMap.setTypeface(
                    binding.bottomNavLayout.tvTabMap.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabMap.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )
                binding.bottomNavLayout.tvTabSearch.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabSearch.setTypeface(
                    binding.bottomNavLayout.tvTabSearch.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabSearch.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabSetting.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabSetting.setTypeface(
                    binding.bottomNavLayout.tvTabSetting.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabSetting.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )

                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 1, OrdersFragment())
                }
                updateFragments()


            }
            R.id.ivTabMap -> {
                if (binding.rlFragmentContainer2.visibility == View.VISIBLE) {
                    if (fragmentStack2.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }
                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
                binding.rlFragmentContainer2.visibility = View.VISIBLE
                binding.bottomNavLayout.tvTabMap.setTextColor(resources.getColor(R.color.black))
                binding.bottomNavLayout.tvTabMap.setTypeface(
                    binding.bottomNavLayout.tvTabMap.typeface,
                    Typeface.BOLD
                )

                binding.bottomNavLayout.ivTabMap.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    ), PorterDuff.Mode.SRC_IN
                )
                binding.rlFragmentContainer1.visibility = View.GONE
                binding.rlFragmentContainer3.visibility = View.GONE
                binding.rlFragmentContainer4.visibility = View.GONE
                binding.rlFragmentContainer5.visibility = View.GONE

                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabSearch.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabSearch.setTypeface(
                    binding.bottomNavLayout.tvTabSearch.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabSearch.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabSetting.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabSetting.setTypeface(
                    binding.bottomNavLayout.tvTabSetting.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabSetting.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )

                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 2, MapFragment())
                }

                updateFragments()


            }
            R.id.ivTabSearch -> {

                if (binding.rlFragmentContainer4.visibility == View.VISIBLE) {
                    if (fragmentStack4.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }
                binding.rlFragmentContainer4.visibility = View.VISIBLE
                binding.bottomNavLayout.tvTabSearch.setTypeface(
                    binding.bottomNavLayout.tvTabSearch.typeface,
                    Typeface.BOLD
                )

                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))


                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )
                binding.rlFragmentContainer2.visibility = View.GONE
                binding.rlFragmentContainer1.visibility = View.GONE
                binding.rlFragmentContainer3.visibility = View.GONE
                binding.rlFragmentContainer5.visibility = View.GONE

                binding.bottomNavLayout.tvTabMap.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabMap.setTypeface(
                    binding.bottomNavLayout.tvTabMap.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabMap.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.bottomNavLayout.tvTabSearch.setTextColor(resources.getColor(R.color.black))
                binding.bottomNavLayout.ivTabSearch.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabSetting.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabSetting.setTypeface(
                    binding.bottomNavLayout.tvTabSetting.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabSetting.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )

                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 3, SearchBottomSheetFragment())
                }

                updateFragments()


            }
            R.id.ivTabOrder -> {

                if (binding.rlFragmentContainer3.visibility == View.VISIBLE) {
                    if (fragmentStack3.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }

                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
                binding.rlFragmentContainer3.visibility = View.VISIBLE
                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.black))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.BOLD
                )

                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.rlFragmentContainer2.visibility = View.GONE
                binding.rlFragmentContainer5.visibility = View.GONE
                binding.rlFragmentContainer4.visibility = View.GONE
                binding.rlFragmentContainer1.visibility = View.GONE


                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.bottomNavLayout.tvTabMap.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabMap.setTypeface(
                    binding.bottomNavLayout.tvTabMap.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabMap.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabSearch.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabSearch.setTypeface(
                    binding.bottomNavLayout.tvTabSearch.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabSearch.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabSetting.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabSetting.setTypeface(
                    binding.bottomNavLayout.tvTabSetting.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabSetting.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 3, MapFragment())
                }

                updateFragments()


            }
            R.id.ivTabSetting -> {

                if (binding.rlFragmentContainer5.visibility == View.VISIBLE) {
                    if (fragmentStack5.size > 1) {
                        removeCurrentFragment()
                    }
                    return
                }
                binding.rlFragmentContainer5.visibility = View.VISIBLE
                binding.bottomNavLayout.ivTabHome.setImageDrawable(resources.getDrawable(R.drawable.ic_home))

                binding.bottomNavLayout.tvTabSetting.setTextColor(resources.getColor(R.color.black))
                binding.bottomNavLayout.tvTabSetting.setTypeface(
                    binding.bottomNavLayout.tvTabSetting.typeface,
                    Typeface.BOLD
                )

                binding.bottomNavLayout.ivTabSetting.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabOrder.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.rlFragmentContainer2.visibility = View.GONE
                binding.rlFragmentContainer3.visibility = View.GONE
                binding.rlFragmentContainer1.visibility = View.GONE
                binding.rlFragmentContainer4.visibility = View.GONE

                binding.bottomNavLayout.tvTabHome.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabHome.setTypeface(
                    binding.bottomNavLayout.tvTabHome.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabHome.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )


                binding.bottomNavLayout.tvTabMap.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabMap.setTypeface(
                    binding.bottomNavLayout.tvTabMap.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabMap.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabSearch.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabSearch.setTypeface(
                    binding.bottomNavLayout.tvTabSearch.typeface,
                    Typeface.NORMAL
                )

                binding.bottomNavLayout.ivTabSearch.setColorFilter(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    ), PorterDuff.Mode.SRC_IN
                )

                binding.bottomNavLayout.tvTabOrder.setTextColor(resources.getColor(R.color.white))
                binding.bottomNavLayout.tvTabOrder.setTypeface(
                    binding.bottomNavLayout.tvTabOrder.typeface,
                    Typeface.NORMAL
                )

                if (getVisibleFrameStakList()!!.size == 0) {
                    addFragment(false, 5, ProfileFragment())
                }

                updateFragments()


            }
        }
    }

    fun setBottomNavigationVisibility(isToShow: Boolean) {
        if (isToShow) {
            binding.bottomNavLayout.bottomnavRootContainer.visibility = View.VISIBLE
        } else {
            binding.bottomNavLayout.bottomnavRootContainer.visibility = View.GONE
        }
    }

    fun updateFragments() {
        val fragment1: Fragment?
        val fragment2: Fragment?
        val fragment3: Fragment?
        val fragment4: Fragment?
        val fragment5: Fragment?
        val fm = supportFragmentManager
        val pos = getVisibleFrame()
        if (fragmentStack1.size > 0) {
            fragment1 = fm.findFragmentByTag(fragmentStack1[fragmentStack1.size - 1])
            if (fragment1 != null) {
                (fragment1 as BaseFragment).setFragmentContainerVisible(pos == 1)
                fragment1.onHiddenChanged(false)
            }
        }
        if (fragmentStack2.size > 0) {
            fragment2 = fm.findFragmentByTag(fragmentStack2[fragmentStack2.size - 1])
            if (fragment2 != null) {
                (fragment2 as BaseFragment).setFragmentContainerVisible(pos == 2)
                fragment2.onHiddenChanged(false)
            }
        }
        if (fragmentStack3.size > 0) {
            fragment3 = fm.findFragmentByTag(fragmentStack3[fragmentStack3.size - 1])
            if (fragment3 != null) {
                (fragment3 as BaseFragment).setFragmentContainerVisible(pos == 3)
                fragment3.onHiddenChanged(false)
            }
        }
        if (fragmentStack4.size > 0) {
            fragment4 = fm.findFragmentByTag(fragmentStack4[fragmentStack4.size - 1])
            if (fragment4 != null) {
                (fragment4 as BaseFragment).setFragmentContainerVisible(pos == 4)
                fragment4.onHiddenChanged(false)
            }
        }
        if (fragmentStack5.size > 0) {
            fragment5 = fm.findFragmentByTag(fragmentStack5[fragmentStack5.size - 1])
            if (fragment5 != null) {
                (fragment5 as BaseFragment).setFragmentContainerVisible(pos == 5)
                fragment5.onHiddenChanged(false)
            }
        }
    }

    fun clickTabPosAt(pos: Int, isToClearStack: Boolean) {
        when (pos) {
            1 -> {
                if (isToClearStack) {
                    clearBackStack(1)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabHome)
            }
            2 -> {
                if (isToClearStack) {
                    clearBackStack(2)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabMap)
            }
            3 -> {
                if (isToClearStack) {
                    clearBackStack(3)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabOrder)
            }
            4 -> {
                if (isToClearStack) {
                    clearBackStack(4)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabSearch)
            }
            5 -> {
                if (isToClearStack) {
                    clearBackStack(5)
                }
                handleBottomNavTabClicks(binding.bottomNavLayout.ivTabSetting)
            }
        }
    }

    override fun onBackPressed() {
        if (getVisibleFrameStakList()!!.size == 1 && getVisibleFrame() != 1) {
            clearBackStack(2)
            clearBackStack(3)
            clearBackStack(4)
            clearBackStack(5)
            handleBottomNavTabClicks(binding.bottomNavLayout.ivTabHome)
        } else if (getVisibleFrameStakList()!!.size == 1 && getVisibleFrame() == 1) {
            clearBackStack(0)
            finish()
        } else {
            removeCurrentFragment()

        }
    }

    fun getFirstAndLastMonthWise(monthAsInt: Int) {
        val calendar = GregorianCalendar.getInstance()
        calendar.set(2024, monthAsInt, 24)
        Log.d("calendarTime-->", "getFirstAndLastMonthWise: " + calendar.time)
    }

    fun getAllMonthForGraphPartner(): MutableList<VendorOrderGraphModel.Data> {
        var listOfMonths: MutableList<VendorOrderGraphModel.Data> = arrayListOf()

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "January",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "February",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "March",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "April",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "May",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "June",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "July",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "August",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "September",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "October",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "November",
                orders = 0.0,
                revenue = 0.0
            )
        )

        listOfMonths.add(
            VendorOrderGraphModel.Data(
                month = "December",
                orders = 0.0,
                revenue = 0.0
            )
        )

        return listOfMonths
    }

    fun getAllMonthForGraphRider(): MutableList<RiderGraphResponse.Data> {
        var listOfMonths: MutableList<RiderGraphResponse.Data> = arrayListOf()

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "January",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "February",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "March",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "April",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "May",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "June",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "July",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "August",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "September",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "October",
                picked = 0,
                delivered = 0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "November",
                picked = 0,
                delivered =0
            )
        )

        listOfMonths.add(
            RiderGraphResponse.Data(
                month = "December",
                picked = 0,
                delivered = 0
            )
        )

        return listOfMonths
    }

    fun getAllMonthForAdminGraph(): MutableList<AdminGraphResponse.Data> {
        var listOfMonths: MutableList<AdminGraphResponse.Data> = arrayListOf()

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "January",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "February",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "March",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "April",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "May",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "June",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "July",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "August",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "September",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "October",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "November",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        listOfMonths.add(
            AdminGraphResponse.Data(
                month = "December",
                orderRevenue = 0.0,
                ordersDelivered = 0,
                ordersReceived = 0,
                packageRevenue = 0.0,
                subscriptionRevenue = 0.0,
                totalOrders = 0
            )
        )

        return listOfMonths
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        return calendar[Calendar.MONTH] + 1
    }

    fun getCurrentDay(): Int {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        return calendar[Calendar.DAY_OF_MONTH]
    }

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        return calendar[Calendar.YEAR]
    }

    fun getMonthString(month: Int): String {
        val result = when (month) {
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "Jun"
            6 -> "Jul"
            7 -> "Aug"
            8 -> "Sept"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> {
                "Apr"
            }
        }
        return result
    }
}