package com.laundrybuoy.admin.ui.rider

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.rider.MarkRiderAttendanceAdapter
import com.laundrybuoy.admin.databinding.FragmentRiderAttendanceBinding
import com.laundrybuoy.admin.model.rider.ApproveRiderAttendance
import com.laundrybuoy.admin.model.rider.RiderAttendanceModel
import com.laundrybuoy.admin.ui.rider.attendance.AttendanceRootFragment
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.utils.makeButtonDisabled
import com.laundrybuoy.admin.utils.makeButtonEnabled
import com.laundrybuoy.admin.utils.normalDateToISO
import com.laundrybuoy.admin.viewmodel.RiderViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.naishadhparmar.zcustomcalendar.CustomCalendar
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener
import org.naishadhparmar.zcustomcalendar.Property
import java.time.Month
import java.time.Year
import java.util.*

@AndroidEntryPoint
class RiderAttendanceFragment : BaseBottomSheet(), OnNavigationButtonClickedListener {

    private var _binding: FragmentRiderAttendanceBinding? = null
    private val binding get() = _binding!!

    private val riderViewModel by viewModels<RiderViewModel>()
    private lateinit var customCalendar: CustomCalendar
    private lateinit var markAttendanceAdapter: MarkRiderAttendanceAdapter

    private var latestCalendar: Calendar = Calendar.getInstance()
    var riderIdReceived: String? = null
    var latestPayload = JsonObject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        onClick()
        initObserver()
        initCalendar()
        initMarkAttendanceRv()
        fetchCalendarData()

    }

    private fun onClick() {

        binding.markAttendanceLl.setOnClickListener {
            val calFrag =
                AttendanceRootFragment.newInstance(riderIdReceived ?: "")
            calFrag.isCancelable=true
            calFrag.show(childFragmentManager, "rider_cal_view")
        }


        binding.backFromAttendanceIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.markBtmSht.markAbsentLl.setOnClickListener {
            binding.markBtmSht.isAbsentSelected = true
            binding.markBtmSht.isPresentSelected = false
        }

        binding.markBtmSht.markPresentLl.setOnClickListener {
            binding.markBtmSht.isAbsentSelected = false
            binding.markBtmSht.isPresentSelected = true
        }

        binding.markBtmSht.markAttendanceBtn.setOnClickListener {
            if (binding.markBtmSht.isAbsentSelected == false && binding.markBtmSht.isPresentSelected == false) {
                Toast.makeText(requireContext(), "Please select one operation", Toast.LENGTH_SHORT)
                    .show()
            } else {
                generatePayload()
            }
        }

    }

    private fun generatePayload() {
        val selectedList = riderViewModel.markAttendanceList.value
        val monthSelected = latestCalendar?.get(Calendar.MONTH)?.plus(1)
        val yearSelected = latestCalendar?.get(Calendar.YEAR)
        val status = if ((binding.markBtmSht.isAbsentSelected == true)) {
            "rejected"
        } else {
            "approved"
        }

        val dates = selectedList?.map {
            ("${it.date}/${monthSelected}/${yearSelected}").toString().normalDateToISO()
        }

        var attendancePayload = ApproveRiderAttendance(
            approvalStatus = status,
            dates = dates,
            riderId = riderIdReceived
        )
        riderViewModel.markRiderAttendance(attendancePayload)
    }

    private fun initMarkAttendanceRv() {
        markAttendanceAdapter =
            MarkRiderAttendanceAdapter(object : MarkRiderAttendanceAdapter.OnClickInterface {

                override fun onCheckBoxSelected(attendanceItem: RiderAttendanceModel.Data) {
                    riderViewModel.selectedAttendanceDate(attendanceItem)
                }

            })

        /*
        binding.markAttendanceRv.layoutManager =
            GridLayoutManager(requireContext(), 7)
        binding.markAttendanceRv.adapter = markAttendanceAdapter

         */

    }

    companion object {
        private const val RIDER_ID = "RIDER_ID"

        fun newInstance(
            riderId: String,
        ): RiderAttendanceFragment {
            val viewFragment = RiderAttendanceFragment()
            val args = Bundle()
            args.putString(RIDER_ID, riderId)
            viewFragment.arguments = args
            return viewFragment
        }
    }


    private fun initObserver() {

        riderViewModel.riderAttendanceLiveData.observe(viewLifecycleOwner, Observer {

            binding.calendarShimmerSl.visibility = View.VISIBLE
            binding.calendarShimmerSl.startShimmer()
            binding.activityCalendar.visibility = View.GONE

            when (it) {
                is NetworkResult.Loading -> {
                    binding.calendarShimmerSl.visibility = View.VISIBLE
                    binding.calendarShimmerSl.startShimmer()
                    binding.activityCalendar.visibility = View.GONE
                }
                is NetworkResult.Success -> {
                    if (it.data?.data?.isNotEmpty() == true) {
                        setCalendarData(it.data.data)
                        binding.calendarShimmerSl.visibility = View.GONE
                        binding.calendarShimmerSl.stopShimmer()
                        binding.activityCalendar.visibility = View.VISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    binding.calendarShimmerSl.visibility = View.GONE
                    binding.calendarShimmerSl.stopShimmer()
                    binding.activityCalendar.visibility = View.GONE
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

        riderViewModel.markAttendanceList.observe(viewLifecycleOwner, Observer {
            binding.markBtmSht.isAbsentSelected = false
            binding.markBtmSht.isPresentSelected = false

            if (!it.isNullOrEmpty()) {
                binding.markBtmSht.markAttendanceBtn.makeButtonEnabled()
                floatInAnimation()
                if (it.size == 1) {
                    binding.markBtmSht.totalAttendanceTv.text = ("${it.size} date selected")
                } else {
                    binding.markBtmSht.totalAttendanceTv.text = ("${it.size} dates selected")
                }
            } else {
                binding.markBtmSht.markAttendanceBtn.makeButtonDisabled()
                floatOutAnimation()
                binding.markBtmSht.totalAttendanceTv.text = ("No dates selected.")
            }

        })

        riderViewModel.markRiderAttendanceLiveData.observe(viewLifecycleOwner, Observer {
            binding.markBtmSht.markAttendanceBtn.makeButtonDisabled()
            when (it) {
                is NetworkResult.Loading -> {
                    binding.markBtmSht.markAttendanceBtn.makeButtonDisabled()
                }
                is NetworkResult.Success -> {
                    binding.markBtmSht.markAttendanceBtn.makeButtonEnabled()
                    if (it.data?.success == true) {
                        floatOutAnimation()

                        val attendancePayload = JsonObject()
                        attendancePayload.addProperty("month",
                            latestCalendar.get(Calendar.MONTH).plus(1))
                        attendancePayload.addProperty("year", latestCalendar.get(Calendar.YEAR))
                        attendancePayload.addProperty("riderId", riderIdReceived)
                        latestPayload = attendancePayload
                        riderViewModel.getRiderAttendance(attendancePayload)

                    }
                }
                is NetworkResult.Error -> {
                    binding.markBtmSht.markAttendanceBtn.makeButtonEnabled()

                }
            }

        })

    }

    private fun floatOutAnimation() {
        ObjectAnimator.ofFloat(binding.markBtmSht.bottomSheetMark, "translationY", 700f).apply {
            duration = 200
            start()
        }
    }

    private fun floatInAnimation() {
        ObjectAnimator.ofFloat(binding.markBtmSht.bottomSheetMark, "translationY", -700f).apply {
            duration = 200
            start()
        }

    }


    private fun fetchCalendarData() {

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

    override fun onNavigationButtonClicked(
        whichButton: Int,
        newMonth: Calendar?,
    ): Array<MutableMap<Int, Any>?> {
        latestCalendar = newMonth!!
        val arr: Array<MutableMap<Int, Any>?> = arrayOfNulls<MutableMap<Int, Any>>(2)
        arr[0] = HashMap() //This is the map linking a date to its description

        var month = newMonth?.get(Calendar.MONTH)
        val year = newMonth?.get(Calendar.YEAR)
        month = month?.plus(1)
        val attendancePayload = JsonObject()
        attendancePayload.addProperty("month", month)
        attendancePayload.addProperty("year", year)
        attendancePayload.addProperty("riderId", riderIdReceived)
        latestPayload = attendancePayload
        riderViewModel.getRiderAttendance(attendancePayload)

        riderViewModel.makeAttendanceNull()
        markAttendanceAdapter.submitList(null)
        markAttendanceAdapter.notifyDataSetChanged()

        return arr
    }


    private fun initCalendar() {


        /*
        defaultdefault
        presentsubmitted
        absentsubmitted
        presentapproved
        absentapproved
         */

        val descHashMap: HashMap<Any, Property> = HashMap()

        // for defaultDefaultProperty
        val defaultDefaultProperty = Property()
        defaultDefaultProperty.layoutResource = R.layout.calendar_default_view
        defaultDefaultProperty.dateTextViewResource = R.id.text_view
        descHashMap["defaultdefault"] = defaultDefaultProperty

        // for presentSubmitted
        val presentSubmitted = Property()
        presentSubmitted.layoutResource = R.layout.calendar_unapproved_view_p
        presentSubmitted.dateTextViewResource = R.id.text_view
        descHashMap["presentsubmitted"] = presentSubmitted

        // for absentSubmitted
        val absentSubmitted = Property()
        absentSubmitted.layoutResource = R.layout.calendar_unapproved_view
        absentSubmitted.dateTextViewResource = R.id.text_view
        descHashMap["absentsubmitted"] = absentSubmitted

        // for presentApproved
        val presentApproved = Property()
        presentApproved.layoutResource = R.layout.calendar_present_view
        presentApproved.dateTextViewResource = R.id.text_view
        descHashMap["presentapproved"] = presentApproved

        // for absentApproved
        val absentApproved = Property()
        absentApproved.layoutResource = R.layout.calendar_absent_view
        absentApproved.dateTextViewResource = R.id.text_view
        descHashMap["absentapproved"] = absentApproved

        // for disabled date
        val disabledProperty = Property()
        disabledProperty.layoutResource = R.layout.calendar_disabled_view
        disabledProperty.dateTextViewResource = R.id.text_view
        descHashMap["disabled"] = disabledProperty

        // set desc hashmap on custom calendar
        customCalendar.setMapDescToProp(descHashMap)

        customCalendar.monthYearTextView.setTextAppearance(
            requireContext(),
            R.style.FontForCalendar
        )

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

        customCalendar.setOnDateSelectedListener { view, selectedDate, desc ->
            val sDate = (selectedDate[Calendar.DAY_OF_MONTH]
                .toString() + "/" + (selectedDate[Calendar.MONTH] + 1)
                    + "/" + selectedDate[Calendar.YEAR])

            getMainActivity()?.showSnackBar(sDate)
        }
    }

    private fun setCalendarData(calendarData: List<RiderAttendanceModel.Data>) {
        val dateHashmap: HashMap<Int, Any> = HashMap()
        calendarData.forEachIndexed { index, data ->
            dateHashmap[data?.date!!] = (data.value + data.status) ?: "default"
        }
        Log.d("fuck-->", "setCalendarData: " + Gson().toJson(dateHashmap))
        customCalendar.setDate(latestCalendar, dateHashmap)


        markAttendanceAdapter.submitList(calendarData)

        setFigures(calendarData)
    }

    private fun setFigures(calendarData: List<RiderAttendanceModel.Data>) {
        val currentDay = getMainActivity()?.getCurrentDay()
        var missedAttendance = 0
        var submittedAttendance = 0
        var approvedAttendance = 0

        missedAttendance = calendarData.filter {
            (it.date!! < currentDay?.minus(3)!!) && (it.value=="default" || it.value=="default")
        }.size

        submittedAttendance = calendarData.filter {
            it.status=="submitted" && (it.value=="absent" || it.value=="present")
        }.size

        approvedAttendance = calendarData.filter {
            it.status=="approved"
        }.size

        binding.missedDaysTv.text=missedAttendance.toString()
        binding.submittedDaysTv.text=submittedAttendance.toString()
        binding.approvedDaysTv.text=approvedAttendance.toString()

    }

    private fun init() {
        riderIdReceived = arguments?.getString(RIDER_ID)
        customCalendar = binding.activityCalendar

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiderAttendanceBinding.inflate(inflater, container, false)
        return binding.root
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