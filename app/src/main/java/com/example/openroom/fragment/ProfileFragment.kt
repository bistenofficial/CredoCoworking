package com.example.openroom.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openroom.BuildConfig
import com.example.openroom.R
import com.example.openroom.adapter.ProfileAdapter
import com.example.openroom.api.ApiManager
import com.example.openroom.databinding.ProfileFragmentBinding
import com.example.openroom.model.AgentModel
import com.example.openroom.model.ProfileItemModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(R.layout.profile_fragment), ProfileAdapter.Listener {
    private var sharedPreferencesPhone: SharedPreferences? = null
    var agentModel: AgentModel? = null
    private val adapter = ProfileAdapter(this)

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding)
    {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesPhone =
            requireActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE)
        getData()
    }

    private fun init() = with(binding)
    {
        apply {
            adapter.clearProfileList()
            recyclerViewProfile.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerViewProfile.adapter = adapter
            adapter.clearProfileList()
            adapter.addProfile(
                ProfileItemModel(
                    R.drawable.ic_baseline_person,
                    getString(R.string.edit_profile),
                    0
                )
            )
            adapter.addProfile(
                ProfileItemModel(
                    R.drawable.ic_baseline_notifications,
                    getString(R.string.notifications),
                    0
                )
            )
            adapter.addProfile(
                ProfileItemModel(
                    R.drawable.ic_baseline_shield,
                    getString(R.string.security),
                    0
                )
            )

            adapter.addProfile(
                ProfileItemModel(
                    R.drawable.ic_baseline_logout,
                    getString(R.string.logout),
                    1
                )
            )

        }
    }

    private fun getData() = with(binding)
    {
        apiManager = ApiManager.getInstance()
        ("Version " + (BuildConfig.VERSION_NAME) + "(" + BuildConfig.VERSION_CODE.toString() + ")").also {
            textViewProjectInfo.text = it
        }
        apiManager!!.getAgentData(
            sharedPreferencesPhone!!.getString("Phone", ""),
            object : Callback<AgentModel?> {
                override fun onResponse(
                    call: Call<AgentModel?>,
                    response: Response<AgentModel?>
                ) {
                    agentModel = response.body()
                    textViewPhone.text = agentModel!!.phone
                    //editTextSurname.setText(agentModel!!.surname)
                    //editTextPatronymic.setText(agentModel!!.patronymic)
                    (agentModel!!.surname + " " + agentModel!!.name).also {
                        textViewSurName.text = it
                    }

                    //editTextEmail.setText(agentModel!!.email)
                    //editTextNameAgent.setText(agentModel!!.name)
                }

                override fun onFailure(call: Call<AgentModel?>, t: Throwable) {
                    Toast.makeText(context, "Error is " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        var apiManager: ApiManager? = null
    }

    override fun onClick(profileItem: ProfileItemModel) {
        when (profileItem.photo) {
            R.drawable.ic_baseline_logout -> findNavController().navigate(R.id.action_profileFragment_to_logoutFragment)
        }
    }
}