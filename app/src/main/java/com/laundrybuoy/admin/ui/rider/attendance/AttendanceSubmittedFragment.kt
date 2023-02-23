package com.laundrybuoy.admin.ui.rider.attendance

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.rider.MissedAttendanceAdapter
import com.laundrybuoy.admin.adapter.rider.SubmittedAttendanceAdapter
import com.laundrybuoy.admin.databinding.FragmentAttendanceRootBinding
import com.laundrybuoy.admin.databinding.FragmentAttendanceSubmittedBinding
import com.laundrybuoy.admin.model.rider.RiderAttendanceModel
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.RiderViewModel


class AttendanceSubmittedFragment : BaseFragment() {

    private var _binding: FragmentAttendanceSubmittedBinding? = null
    private val binding get() = _binding!!
    var riderIdReceived: String? = null
    lateinit var riderViewModel: RiderViewModel
    private lateinit var submittedAttendanceAdapter: SubmittedAttendanceAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): AttendanceSubmittedFragment {
            val viewFragment = AttendanceSubmittedFragment()
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
        _binding = FragmentAttendanceSubmittedBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        riderIdReceived = arguments?.getString(RIDER_ID)
        riderViewModel = ViewModelProvider(requireActivity()).get(RiderViewModel::class.java)

        initObserver()
        init()
    }


    private fun init() {
        initMarkAttendanceRv()
    }

    private fun initMarkAttendanceRv() {
        submittedAttendanceAdapter =
            SubmittedAttendanceAdapter(object : SubmittedAttendanceAdapter.OnClickInterface {
                override fun onCheckBoxSelected(attendanceItem: RiderAttendanceModel.Data) {
//                    riderViewModel.selectedAttendanceDate(attendanceItem)
                }

            })

        binding.submittedAttendanceRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.submittedAttendanceRv.adapter = submittedAttendanceAdapter

    }

    private fun initObserver() {
        riderViewModel.riderAttendanceLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Success -> {
                    if (it.data?.data?.isNotEmpty() == true) {
                        setCalendarData(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setCalendarData(data: List<RiderAttendanceModel.Data>) {
        val submittedAttendance = data.filter {
            it.status=="submitted" && (it.value=="absent" || it.value=="present")
        }
        submittedAttendanceAdapter.submitList(submittedAttendance)

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