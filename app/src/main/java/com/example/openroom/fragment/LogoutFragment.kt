package com.example.openroom.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.openroom.R
import com.example.openroom.databinding.LogoutFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutFragment : BottomSheetDialogFragment() {
    override fun getTheme() = R.style.AppBottomSheetDialogTheme
    private var _binding:LogoutFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LogoutFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding)
    {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferencesPhone =
            requireActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE)
        buttonLogout.setOnClickListener()
        {
            val editor = sharedPreferencesPhone!!.edit()
            editor.putString("Phone", "")
            editor.apply()
            findNavController().navigate(R.id.action_logoutFragment_to_logRegActivity)
            requireActivity().finish()
        }
        buttonCancel.setOnClickListener()
        {
            findNavController().navigate(R.id.action_logoutFragment_to_profileFragment)
        }
    }

}