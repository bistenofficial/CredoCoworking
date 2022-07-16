package com.example.openroom.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.openroom.R
import com.example.openroom.adapter.ServiceAdapter
import com.example.openroom.databinding.ServiceFragmentBinding
import com.example.openroom.model.ServiceModel

class ServiceFragment : Fragment(), ServiceAdapter.Listener {
    private var _binding: ServiceFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = ServiceAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ServiceFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() = with(binding)
    {
        apply {
            serviceRecycler.layoutManager = GridLayoutManager(context, 2)
            serviceRecycler.adapter = adapter
            adapter.addService(ServiceModel(R.drawable.ping_pong, getString(R.string.table_tennis)))
            adapter.addService(ServiceModel(R.drawable.dart, getString(R.string.darts)))
            adapter.addService(ServiceModel(R.drawable.workspace, getString(R.string.workspace)))
            adapter.addService(ServiceModel(R.drawable.printer, getString(R.string.printer)))
            adapter.addService(ServiceModel(R.drawable.table_game, getString(R.string.board_game)))
            adapter.addService(
                ServiceModel(
                    R.drawable.conference,
                    getString(R.string.conference_hall)
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(service: ServiceModel) {
        var bundle = Bundle()
        adapter.clearServiceList()
        when (service.imageId) {
            R.drawable.ping_pong -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.ping_pong,
                    R.drawable.table_tennis_playing,
                    R.drawable.table_tennis_playing_2,
                    R.drawable.table_tennis_playing_3
                )
                val addonImageArray: IntArray = intArrayOf(
                    R.drawable.ping_pong,
                    R.drawable.table_tennis_playing
                )
                val addonStringArray: IntArray =
                    intArrayOf(R.string.table_tennis, R.string.table_tennis)

                bundle = createBundle(
                    imagesArray,
                    R.string.table_tennis,
                    R.string.tennis_info,
                    addonImageArray,
                    addonStringArray
                )
            }

            R.drawable.dart -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.dart
                )
                bundle = createBundle(
                    imagesArray,
                    R.string.table_tennis,
                    R.string.tennis_info,
                    null,
                    null
                )
            }

            R.drawable.workspace -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.workspace
                )
                bundle =
                    createBundle(imagesArray, R.string.workspace, R.string.tennis_info, null, null)
            }
            R.drawable.printer -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.printer
                )
                bundle =
                    createBundle(imagesArray, R.string.printer, R.string.tennis_info, null, null)
            }
            R.drawable.table_game -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.table_game
                )
                bundle =
                    createBundle(imagesArray, R.string.board_game, R.string.tennis_info, null, null)
            }
            R.drawable.conference -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.conference
                )
                bundle = createBundle(
                    imagesArray,
                    R.string.conference_hall,
                    R.string.tennis_info,
                    null,
                    null
                )
            }
        }
        findNavController().navigate(
            R.id.action_serviceFragment_to_serviceItemFragment,
            bundle
        )
    }

    private fun createBundle(
        imagesArray: IntArray,
        name: Int,
        info: Int,
        addonImageArray: IntArray?,
        addonStringArray: IntArray?
    ): Bundle {
        val bundle = Bundle()
        bundle.putIntArray("Images", imagesArray)
        bundle.putInt("Name", name)
        bundle.putInt("Info", info)
        bundle.putIntArray("AddonImages", addonImageArray!!)
        bundle.putIntArray("AddonName", addonStringArray!!)
        return bundle
    }
}