package com.laundrybuoy.admin.ui.rider.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.rider.MissedAttendanceAdapter
import com.laundrybuoy.admin.databinding.FragmentAttendanceMissedBinding
import com.laundrybuoy.admin.model.rider.RiderAttendanceModel
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.RiderViewModel


class AttendanceMissedFragment : BaseFragment() {

    private var _binding: FragmentAttendanceMissedBinding? = null
    private val binding get() = _binding!!
    var riderIdReceived: String? = null
    lateinit var riderViewModel: RiderViewModel
    private lateinit var missedAttendanceAdapter: MissedAttendanceAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): AttendanceMissedFragment {
            val viewFragment = AttendanceMissedFragment()
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
        _binding = FragmentAttendanceMissedBinding.inflate(inflater, container, false)
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
        missedAttendanceAdapter =
            MissedAttendanceAdapter(object : MissedAttendanceAdapter.OnClickInterface {
                override fun onCheckBoxSelected(attendanceItem: RiderAttendanceModel.Data) {
//                    riderViewModel.selectedAttendanceDate(attendanceItem)
                }

            })

        binding.missedAttendanceRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.missedAttendanceRv.adapter = missedAttendanceAdapter

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
        val currentDay = getMainActivity()?.getCurrentDay()
        val missedList = data.filter {
            (it.date!! < currentDay?.minus(3)!!) && (it.value=="default" || it.value=="default")
        }
        missedAttendanceAdapter.submitList(missedList)
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