package com.example.openroom.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openroom.R
import com.example.openroom.adapter.AddonAdapter
import com.example.openroom.adapter.ServicePhotoAdapter
import com.example.openroom.databinding.ServiceItemFragmentBinding
import com.example.openroom.model.AddonModel
import com.example.openroom.model.AgentModel

class ServiceItemFragment : Fragment(R.layout.service_item_fragment) {
    private var _binding: ServiceItemFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapterServicePhoto = ServicePhotoAdapter()
    private val adapterAddonAdapter = AddonAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ServiceItemFragmentBinding.inflate(inflater, container, false)

        val imageArray = arguments?.getIntArray("Images")
        val addonImages = arguments?.getIntArray("AddonImages")
        val addonName = arguments?.getIntArray("AddonName")

        binding.textViewItemInfo.text = getString(arguments?.getInt("Info")!!)
        binding.textViewSelectedService.text = getString(arguments?.getInt("Name")!!)

        init(imageArray,addonImages,addonName)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)= with(binding){
        super.onViewCreated(view, savedInstanceState)
        buttonBook.setOnClickListener()
        {

        }
        binding.imageViewBackServiceItem.setOnClickListener()
        {
            findNavController().navigate(R.id.action_serviceItemFragment_to_serviceFragment)
        }
    }

    private fun init(imageArray: IntArray?,addonImages:IntArray?,addonName:IntArray?) = with(binding)
    {
        apply {
            photoRecyler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            photoRecyler.adapter = adapterServicePhoto
            for (i in 0 until imageArray!!.size) {
                adapterServicePhoto.addPhoto(imageArray[i])
            }
            addondRecylcer.layoutManager = GridLayoutManager(context,3)
            addondRecylcer.adapter = adapterAddonAdapter

            for (i in 0 until addonImages!!.size) {
                adapterAddonAdapter.addAddon(AddonModel(addonImages[i], getString(addonName!![i]),false))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapterServicePhoto.clearPhotoList()
        _binding = null
    }
}