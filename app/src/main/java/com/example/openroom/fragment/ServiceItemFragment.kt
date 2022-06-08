package com.example.openroom.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openroom.R
import com.example.openroom.adapter.ServicePhotoAdapter
import com.example.openroom.databinding.ServiceItemFragmentBinding

class ServiceItemFragment : Fragment(R.layout.service_item_fragment) {
    private var _binding: ServiceItemFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = ServicePhotoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ServiceItemFragmentBinding.inflate(inflater, container, false)

        val imageArray = arguments?.getIntArray("Images")

        binding.textViewItemInfo.text = getString(arguments?.getInt("Info")!!)
        binding.textViewSelectedService.text = getString(arguments?.getInt("Name")!!)

        init(imageArray)
        return binding.root
    }

    private fun init(imageArray: IntArray?) = with(binding)
    {
        apply {
            photoRecyler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            photoRecyler.adapter = adapter
            for (i in 0 until imageArray!!.size) {
                adapter.addService(imageArray[i])
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.clearServiceList()
        _binding = null
    }
}