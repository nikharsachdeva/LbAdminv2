package com.laundrybuoy.admin.ui.order

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.laundrybuoy.admin.BaseBottomSheet
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.adapter.order.QrSliderAdapter
import com.laundrybuoy.admin.databinding.FragmentOrderDetailBasicBinding
import com.laundrybuoy.admin.databinding.FragmentViewNoteConcernBinding
import com.laundrybuoy.admin.model.order.GetConcernResponse
import com.laundrybuoy.admin.model.order.GetNotesResponse
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator

class ViewNoteConcernFragment : BaseBottomSheet() {

    private var _binding: FragmentViewNoteConcernBinding? = null
    private val binding get() = _binding!!

    lateinit var viewPagerAdapter: QrSliderAdapter
    lateinit var indicator: CircleIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun onClick() {
        binding.closeViewIv.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()

        val screenType = arguments?.getString(SCREEN_TYPE)
        if(!screenType.isNullOrEmpty()){
            if(screenType == "NOTE"){
                val noteObj = arguments?.getParcelable<GetNotesResponse.Data>(
                    NOTE_OBJECT
                )
                setNoteUI(noteObj)
            }else if (screenType == "CONCERN"){
                val concernObj = arguments?.getParcelable<GetConcernResponse.Data>(
                    CONCERN_OBJECT
                )
                setConcernUI(concernObj)
            }
        }

    }


    private fun setConcernUI(concernObj: GetConcernResponse.Data?) {
        binding.viewHeadingTv.text=concernObj?.concernType.toString()
        binding.viewDescriptionTv.text=concernObj?.concernDescription.toString()

        concernObj?.profile.let {
            viewPagerAdapter = QrSliderAdapter(requireContext(), it!!)
            binding.viewpagerImages.adapter = viewPagerAdapter
            indicator = requireView().findViewById(R.id.indicatorImages) as CircleIndicator
            indicator.setViewPager(binding.viewpagerImages)
        }
    }

    private fun setNoteUI(noteObj: GetNotesResponse.Data?) {

        binding.viewHeadingTv.text="Note"
        binding.viewDescriptionTv.text=noteObj?.noteDescription.toString()

        noteObj?.profile.let {
            viewPagerAdapter = QrSliderAdapter(requireContext(), it!!)
            binding.viewpagerImages.adapter = viewPagerAdapter
            indicator = requireView().findViewById(R.id.indicatorImages) as CircleIndicator
            indicator.setViewPager(binding.viewpagerImages)
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentViewNoteConcernBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val SCREEN_TYPE = "SCREEN_TYPE"
        private const val NOTE_OBJECT = "NOTE_OBJECT"
        private const val CONCERN_OBJECT = "CONCERN_OBJECT"

        fun newInstance(
            screenType: String,
            noteObject: GetNotesResponse.Data?,
            concernObject: GetConcernResponse.Data?
        ): ViewNoteConcernFragment {
            val viewFragment = ViewNoteConcernFragment()
            val args = Bundle()
            args.putString(SCREEN_TYPE, screenType)
            args.putParcelable(NOTE_OBJECT, noteObject)
            args.putParcelable(CONCERN_OBJECT, concernObject)
            viewFragment.arguments = args
            return viewFragment
        }
    }
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    fun setBottomNav() {
        getMainActivity()?.setBottomNavigationVisibility(false)
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