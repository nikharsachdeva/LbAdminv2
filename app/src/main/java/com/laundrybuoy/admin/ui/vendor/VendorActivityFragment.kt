package com.laundrybuoy.admin.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.vendor.ActivityAdapter
import com.laundrybuoy.admin.databinding.FragmentVendorActivityBinding
import com.laundrybuoy.admin.model.vendor.VendorActivityModel
import com.laundrybuoy.admin.model.vendor.VendorAttendanceModelNew
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.VendorViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.naishadhparmar.zcustomcalendar.CustomCalendar
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener
import org.naishadhparmar.zcustomcalendar.Property
import java.util.*

@AndroidEntryPoint
class VendorActivityFragment : BaseFragment(), OnNavigationButtonClickedListener {

    private var _binding: FragmentVendorActivityBinding? = null
    private val binding get() = _binding!!

    private lateinit var customCalendar: CustomCalendar
    private val vendorViewModel by viewModels<VendorViewModel>()

    private var latestCalendar : Calendar = Calendar.getInstance()
    private lateinit var activityAdapter: ActivityAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()

        initCalendar()
        fetchCalendarData()

        initActivityRv()
    }

    private fun getDummyActivityList(): MutableList<VendorActivityModel.VendorActivityModelItem> {

        val list: MutableList<VendorActivityModel.VendorActivityModelItem> = arrayListOf()
        list.add(
            VendorActivityModel.VendorActivityModelItem(
               activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK3JZZ81KDM",
                activityOperation = "Order Accepted"
            )
        )

        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK3DFG47KDM",
                activityOperation = "Order Accepted"
            )
        )

        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK3BFDERKDM",
                activityOperation = "Order Accepted"
            )
        )

        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK3DF481KDM",
                activityOperation = "Order Accepted"
            )
        )

        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK3DF44D",
                activityOperation = "Order Accepted"
            )
        )


        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BDF4T1KDM",
                activityOperation = "Order Accepted"
            )
        )

        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK3JJGF1KDM",
                activityOperation = "Order Accepted"
            )
        )

        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK3JFD3DM",
                activityOperation = "Order Accepted"
            )
        )

        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK4FDFDKDM",
                activityOperation = "Order Accepted"
            )
        )

        list.add(
            VendorActivityModel.VendorActivityModelItem(
                activityDate = "2022-05-18 04:55PM",
                activityDesc = "Order No #ABD38H1S7 of Mr. Nikhar Sachdeva, For Laundry Accepted.",
                activityId = "#BK3JZSDDM",
                activityOperation = "Order Accepted"
            )
        )


        return list

    }

    private fun initActivityRv() {
        activityAdapter =
            ActivityAdapter(object : ActivityAdapter.OnClickInterface {
                override fun onActivityClicked(activity: VendorActivityModel.VendorActivityModelItem) {
                    getMainActivity()?.showSnackBar("Open Activity : "+activity.activityId)
                }
            })

        val layoutManagerActivity = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.activityRv.layoutManager = layoutManagerActivity
        binding.activityRv.adapter = activityAdapter
    }

    private fun fetchCalendarData() {

        val cal: Calendar = Calendar.getInstance()
        var month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        month = month.plus(1)
        val attendancePayload = JsonObject()
        attendancePayload.addProperty("month", month)
        attendancePayload.addProperty("year", year)
        vendorViewModel.getAttendance(attendancePayload)

    }

    private fun initObserver() {
        vendorViewModel.attendanceLiveData.observe(viewLifecycleOwner, Observer {

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

    }

    private fun setCalendarData(data: List<VendorAttendanceModelNew.Data?>) {
        val dateHashmap: HashMap<Int, Any> = HashMap()
        data.forEachIndexed { index, data ->
            dateHashmap[data?.date!!] = data.value ?: "default"
        }
        customCalendar.setDate(latestCalendar, dateHashmap)

    }

    private fun initCalendar() {
        val descHashMap: HashMap<Any, Property> = HashMap()

        // for default date
        val defaultProperty = Property()
        defaultProperty.layoutResource = R.layout.calendar_default_view
        defaultProperty.dateTextViewResource = R.id.text_view
        descHashMap["default"] = defaultProperty

        // for disabled date
        val disabledProperty = Property()
        disabledProperty.layoutResource = R.layout.calendar_disabled_view
        disabledProperty.dateTextViewResource = R.id.text_view
        descHashMap["disabled"] = disabledProperty

        // for current date
        val currentProperty = Property()
        currentProperty.layoutResource = R.layout.calendar_current_view
        currentProperty.dateTextViewResource = R.id.text_view
        descHashMap["current"] = currentProperty

        // for present date
        val presentProperty = Property()
        presentProperty.layoutResource = R.layout.calendar_present_view
        presentProperty.dateTextViewResource = R.id.text_view
        descHashMap["present"] = presentProperty

        // For absent
        val absentProperty = Property()
        absentProperty.layoutResource = R.layout.calendar_absent_view
        absentProperty.dateTextViewResource = R.id.text_view
        descHashMap["absent"] = absentProperty

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

            fetchActivityData(sDate)
        }
    }

    private fun fetchActivityData(sDate: String) {
        activityAdapter.submitList(getDummyActivityList())

    }


    private fun init() {
        customCalendar = binding.activityCalendar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVendorActivityBinding.inflate(inflater, container, false)
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

    override fun onNavigationButtonClicked(
        whichButton: Int,
        newMonth: Calendar?
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
        vendorViewModel.getAttendance(attendancePayload)

        return arr
    }

}