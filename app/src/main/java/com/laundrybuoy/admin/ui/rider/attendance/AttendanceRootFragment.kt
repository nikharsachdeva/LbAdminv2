package com.laundrybuoy.admin.ui.rider.attendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.rider.RiderAttendancePagerAdapter
import com.laundrybuoy.admin.adapter.rider.RiderPagerAdapter
import com.laundrybuoy.admin.databinding.FragmentAttendanceRootBinding
import com.laundrybuoy.admin.databinding.FragmentRiderRootBinding
import com.laundrybuoy.admin.ui.FilterFragment
import com.laundrybuoy.admin.ui.rider.RiderAttendanceFragment
import com.laundrybuoy.admin.ui.rider.RiderRootFragment
import com.laundrybuoy.admin.viewmodel.OrdersViewModel
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import java.util.*


class AttendanceRootFragment : BaseBottomSheet() {

    private var _binding: FragmentAttendanceRootBinding? = null
    private val binding get() = _binding!!
    var riderIdReceived: String? = null
    lateinit var riderViewModel: RiderViewModel
    var latestPayload = JsonObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        riderIdReceived = arguments?.getString(RIDER_ID)
        riderViewModel = ViewModelProvider(requireActivity()).get(RiderViewModel::class.java)

        initFullHeight()
        initTabLayout()
        onClick()
        fetchRiderAttendance(riderIdReceived)

    }

    private fun fetchRiderAttendance(riderIdReceived: String?) {
        val cal: Calendar = Calendar.getInstance()
        var month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        month = month.plus(1)
        val attendancePayload = JsonObject()
        attendancePayload.addProperty("month", month)
        attendancePayload.addProperty("year", year)
        attendancePayload.addProperty("riderId", riderIdReceived)
        latestPayload = attendancePayload
        riderViewModel.getRiderAttendance(attendancePayload)
    }

    private fun onClick() {
        binding.backFromMarkAttIv.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun initFullHeight() {
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                setupFullHeight(bottomSheet)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

    }

    private fun setupFullHeight(bottomSheet: View) {

        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): AttendanceRootFragment {
            val viewFragment = AttendanceRootFragment()
            val args = Bundle()
            args.putString(RIDER_ID, riderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAttendanceRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initTabLayout() {
        val attendanceTabLayout = binding.attendanceRootTabLayout
        val attendanceViewPager = binding.attendanceRootViewPager

        attendanceTabLayout.addTab(attendanceTabLayout.newTab().setText("Missed"))
        attendanceTabLayout.addTab(attendanceTabLayout.newTab().setText("Submitted"))
        attendanceTabLayout.addTab(attendanceTabLayout.newTab().setText("Approved"))
        attendanceTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = RiderAttendancePagerAdapter(
            requireContext(), childFragmentManager,
            attendanceTabLayout.tabCount,
            riderIdReceived
        )
        attendanceViewPager.adapter = adapter
        attendanceViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                attendanceTabLayout
            )
        )
        attendanceViewPager.offscreenPageLimit = 3
        attendanceTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                attendanceViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(true)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setBottomNav()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}