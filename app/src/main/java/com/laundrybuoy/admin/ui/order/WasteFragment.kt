package com.laundrybuoy.admin.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.databinding.FragmentWasteBinding
import com.laundrybuoy.admin.utils.getProgress
import com.laundrybuoy.admin.utils.roundOffDecimal
import kotlinx.coroutines.*

class WasteFragment : BaseFragment() {

    private var _binding: FragmentWasteBinding? = null
    private val binding get() = _binding!!
    var myJob: Job = Job()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myJob = startRepeatingJob(1000L)

    }

    private fun startRepeatingJob(timeInterval: Long): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                // add your task here
                requireActivity().runOnUiThread(Runnable {
                    setProgressBar()
                })
                delay(timeInterval)
            }
        }
    }

    private fun setProgressBar() {

        val deadLineTime = "2022-10-03T00:00:00Z"
        val progressOfDeadline = deadLineTime.getProgress()

        if (progressOfDeadline != null) {
            val progress = (progressOfDeadline.second).roundOffDecimal()
            progress.let {
                if (it != null) {
                    binding.progressBar.progress = progress?.toInt()!!
                    if(progress>=50){
                        binding.progressBar.setIndicatorColor(resources.getColor(R.color.colorSuccess))
                    }else if(progress>=25&&progress<50){
                        binding.progressBar.setIndicatorColor(resources.getColor(R.color.primary))
                    }else{
                        binding.progressBar.setIndicatorColor(resources.getColor(R.color.colorError))
                    }
                }
            }

            val numberOfHours = (progressOfDeadline.first % 86400) / 3600;
            val numberOfMinutes = ((progressOfDeadline.first % 86400) % 3600) / 60
            val numberOfSeconds = ((progressOfDeadline.first % 86400) % 3600) % 60

            binding.progressBarTimeLeftTv.text =
                "$numberOfHours : $numberOfMinutes : $numberOfSeconds"

        } else {
            binding.progressBar.progress = 0
            binding.progressBarTimeLeftTv.text = "Error"

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWasteBinding.inflate(inflater, container, false)
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
        myJob.cancel()
        _binding = null
    }

}