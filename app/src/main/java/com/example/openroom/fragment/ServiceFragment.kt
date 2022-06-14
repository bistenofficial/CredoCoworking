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

class ServiceFragment : Fragment(R.layout.service_fragment), ServiceAdapter.Listener {
    private var _binding: ServiceFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = ServiceAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ServiceFragmentBinding.inflate(inflater, container, false)
        init()
        return binding.root
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
        val bundle = Bundle()
        adapter.clearServiceList()
        when (service.imageId) {
            R.drawable.ping_pong -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.ping_pong,
                    R.drawable.table_tennis_playing,
                    R.drawable.table_tennis_playing_2,
                    R.drawable.table_tennis_playing_3
                )
                bundle.putIntArray("Images", imagesArray)
                bundle.putInt("Name", R.string.table_tennis)
                bundle.putInt("Info", R.string.tennis_info)
            }

            R.drawable.dart -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.dart
                )
                bundle.putIntArray("Images", imagesArray)
                bundle.putInt("Name", R.string.darts)
                bundle.putInt("Info", R.string.tennis_info)
            }

            R.drawable.workspace -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.workspace
                )
                bundle.putIntArray("Images", imagesArray)
                bundle.putInt("Name", R.string.workspace)
                bundle.putInt("Info", R.string.tennis_info)
            }
            R.drawable.printer -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.printer
                )
                bundle.putIntArray("Images", imagesArray)
                bundle.putInt("Name", R.string.printer)
                bundle.putInt("Info", R.string.tennis_info)
            }
            R.drawable.table_game -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.table_game
                )
                bundle.putIntArray("Images", imagesArray)
                bundle.putInt("Name", R.string.board_game)
                bundle.putInt("Info", R.string.tennis_info)
            }
            R.drawable.conference -> {
                val imagesArray: IntArray = intArrayOf(
                    R.drawable.conference
                )
                bundle.putIntArray("Images", imagesArray)
                bundle.putInt("Name", R.string.conference_hall)
                bundle.putInt("Info", R.string.tennis_info)
            }
        }
        findNavController().navigate(
            R.id.action_serviceFragment_to_serviceItemFragment,
            bundle
        )
    }
}