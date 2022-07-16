package com.example.openroom.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.openroom.R
import com.example.openroom.databinding.EditProfileFragmentBinding
import com.example.openroom.fragment.ProfileFragment.Companion.apiManager
import com.example.openroom.model.AgentModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditProfileFragment : Fragment() {
    private var _binding: EditProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private var sharedPreferencesPhone: SharedPreferences? = null
    var agentModel: AgentModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditProfileFragmentBinding.inflate(inflater, container, false)
        sharedPreferencesPhone =
            requireActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE)
        getData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding)
    {
        super.onViewCreated(view, savedInstanceState)

        imageViewBackEditProfile.setOnClickListener()
        {
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
        buttonUpdate.setOnClickListener()
        {
            agentModel!!.email = editTextEmail.text.toString()
            agentModel!!.name = editTextName.text.toString()
            agentModel!!.patronymic = editTextPatronymic.text.toString()
            agentModel!!.surname = editTextSurname.text.toString()
            val dob = editTextDOB.text.split(".").toTypedArray()
            agentModel!!.dateOfBirth = dob[2]+"-"+dob[1]+"-"+dob[0]
            apiManager!!.updateAgentData(agentModel, object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    Toast.makeText(context, "Успешное обновление данных", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(context, "Error is " + t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
        editTextDOB.setOnClickListener()
        {
            val today = Calendar.getInstance()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                requireActivity(),
                { _, year, monthOfYear, dayOfMonth ->
                    if (monthOfYear + 1 < 10 && dayOfMonth < 10) editTextDOB.setText("""0$dayOfMonth.0${monthOfYear + 1}.$year""")
                    else if (dayOfMonth < 10) editTextDOB.setText("""0$dayOfMonth.${monthOfYear + 1}.$year""")
                    else if (monthOfYear + 1 < 10) editTextDOB.setText("""$dayOfMonth.0${monthOfYear + 1}.$year""")
                },
                year,
                month,
                day
            )
            today.add(Calendar.YEAR, -14)
            dpd.datePicker.maxDate = today.timeInMillis
            dpd.show()
        }
    }

    private fun getData() = with(binding)
    {
        apiManager!!.getAgentData(
            sharedPreferencesPhone!!.getString("Phone", ""),
            object : Callback<AgentModel?> {
                override fun onResponse(
                    call: Call<AgentModel?>,
                    response: Response<AgentModel?>
                ) {
                    agentModel = response.body()
                    editTextPhone.setText(agentModel!!.phone)
                    editTextSurname.setText(agentModel!!.surname)
                    editTextName.setText(agentModel!!.name)
                    editTextPatronymic.setText(agentModel!!.patronymic)
                    editTextEmail.setText(agentModel!!.email)
                    try {
                        val dob = agentModel!!.dateOfBirth.toString().split("-").toTypedArray()
                        editTextDOB.setText(dob[2] + "." + dob[1] + "." + dob[0])
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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
}