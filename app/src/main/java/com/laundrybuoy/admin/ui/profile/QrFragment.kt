package com.laundrybuoy.admin.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.laundrybuoy.admin.BaseFragment
import com.laundrybuoy.admin.adapter.QrAdapter
import com.laundrybuoy.admin.databinding.FragmentQrBinding
import com.laundrybuoy.admin.model.profile.GetQrResponse
import com.laundrybuoy.admin.utils.NetworkResult
import com.laundrybuoy.admin.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QrFragment : BaseFragment() {

    private var _binding: FragmentQrBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private lateinit var qrAdapter: QrAdapter
    var qrDocList: MutableList<GetQrResponse.Data> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initObserver()
        getAllQrs()
    }

    private fun getAllQrs() {
        profileViewModel.getAllQrCodes()
    }

    private fun initObserver() {
        profileViewModel._qrDocLiveData.observe(viewLifecycleOwner, Observer {
            qrAdapter.submitList(it)
            qrAdapter.notifyDataSetChanged()
        })


        profileViewModel.qrCodesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true && !it.data?.data.isNullOrEmpty()) {
                        setQrData(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })

        profileViewModel.deleteQrLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        getMainActivity()?.showSnackBar(it.data?.message)
                        getAllQrs()
                    }
                }
                is NetworkResult.Error -> {
                    getMainActivity()?.showSnackBar(it.message)
                }
            }
        })
    }

    private fun setQrData(data: List<GetQrResponse.Data>) {
        profileViewModel.setQrDocs(data.toMutableList())
    }

    private fun init() {

        initQrRv()
        onClick()
    }

    private fun onClick() {
        binding.closeQrIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.addQrCv.setOnClickListener {
            val updateFrag = AddQrFragment()
            updateFrag.setCallback {
                getAllQrs()
            }
            updateFrag.isCancelable = true
            updateFrag.show(childFragmentManager, "add_qr")

        }
    }

    private fun initQrRv() {
        qrAdapter = QrAdapter(object : QrAdapter.OnClickInterface {


            override fun onImageClicked(doc: GetQrResponse.Data) {

            }

            override fun onDeleteClicked(
                position: Int,
                doc: GetQrResponse.Data,
            ) {

                profileViewModel.deleteQr(doc.id?:"")
            }


        })
        binding.uploadedDocsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.uploadedDocsRv.adapter = qrAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        return binding.root
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