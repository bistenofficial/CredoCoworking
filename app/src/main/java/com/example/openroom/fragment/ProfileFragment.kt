package com.example.openroom.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.openroom.R
import com.example.openroom.model.AgentModel
import com.example.openroom.api.ApiManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(R.layout.profile_fragment) {
    private var sharedPreferencesPhone: SharedPreferences? = null
    var textViewPhone: TextView? = null
    var textViewSurName: TextView? = null
    private var logout: TextView? = null
    var editTextSurname: EditText? = null
    var editTextNameAgent: EditText? = null
    var editTextPatronymic: EditText? = null
    var editTextEmail: EditText? = null
    private var buttonChangeData: Button? = null
    var agentModel: AgentModel? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view)
        data
        logout!!.setOnClickListener {
            val editor = sharedPreferencesPhone!!.edit()
            editor.putString("Phone", "")
            editor.apply()
            findNavController().navigate(R.id.action_profileFragment_to_logRegActivity)
            requireActivity().finish()
        }

        buttonChangeData!!.setOnClickListener {
            apiManager = ApiManager.getInstance()
            agentModel!!.email = editTextEmail!!.text.toString()
            agentModel!!.name = editTextNameAgent!!.text.toString()
            agentModel!!.patronymic = editTextPatronymic!!.text.toString()
            agentModel!!.surname = editTextSurname!!.text.toString()
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
    }

    private fun initialize(inflatedView: View) {
        sharedPreferencesPhone =
            this.requireActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE)
        logout = inflatedView.findViewById(R.id.Logout)
        textViewSurName = inflatedView.findViewById(R.id.textViewSurName)
        buttonChangeData = inflatedView.findViewById(R.id.buttonChangeData)
        textViewPhone = inflatedView.findViewById(R.id.textViewPhone)
        editTextNameAgent = inflatedView.findViewById(R.id.editTextNameAgent)
        editTextSurname = inflatedView.findViewById(R.id.editTextSurname)
        editTextPatronymic = inflatedView.findViewById(R.id.editTextPatronymic)
        editTextEmail = inflatedView.findViewById(R.id.editTextEmail)
    }

    private val data: Unit
        get() {
            apiManager = ApiManager.getInstance()
            apiManager!!.getAgentData(
                sharedPreferencesPhone!!.getString("Phone", ""),
                object : Callback<AgentModel?> {
                    override fun onResponse(call: Call<AgentModel?>, response: Response<AgentModel?>) {
                        agentModel = response.body()
                        textViewPhone!!.text = agentModel!!.phone
                        editTextSurname!!.setText(agentModel!!.surname)
                        editTextPatronymic!!.setText(agentModel!!.patronymic)
                        textViewSurName!!.text = agentModel!!.name
                        editTextEmail!!.setText(agentModel!!.email)
                        editTextNameAgent!!.setText(agentModel!!.name)
                    }

                    override fun onFailure(call: Call<AgentModel?>, t: Throwable) {
                        Toast.makeText(context, "Error is " + t.message, Toast.LENGTH_LONG).show()
                    }
                })
        }

    companion object {
        var apiManager: ApiManager? = null
    }
}