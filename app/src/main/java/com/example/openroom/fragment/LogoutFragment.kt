package com.example.openroom.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.openroom.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutFragment : BottomSheetDialogFragment() {
    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.logout_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferencesPhone =
            requireActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE)

        val buttonCancel: Button = view.findViewById(R.id.buttonCancel)
        val buttonLogout: Button = view.findViewById(R.id.buttonLogout)

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