package com.example.openroom.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.openroom.R
import com.example.openroom.api.Agent
import com.example.openroom.api.ApiManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private var sharedPreferencesPhone: SharedPreferences? = null
    var textViewPhone: TextView? = null
    var textViewSurName: TextView? = null
    private var Logout: TextView? = null
    var editTextSurname: EditText? = null
    var editTextNameAgent: EditText? = null
    var editTextPatronymic: EditText? = null
    var editTextEmail: EditText? = null
    private var buttonChangeData: Button? = null
    var agent: Agent? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.profile_fragment, container, false)
        initialize(inflatedView)
        data
        Logout!!.setOnClickListener {
            val editor = sharedPreferencesPhone!!.edit()
            editor.putString("Phone", "")
            editor.apply()
            findNavController().navigate(R.id.action_profileFragment_to_navigation)
        }
        buttonChangeData!!.setOnClickListener {
            apiManager = ApiManager.getInstance()
            agent!!.email = editTextEmail!!.text.toString()
            agent!!.name = editTextNameAgent!!.text.toString()
            agent!!.patronymic = editTextPatronymic!!.text.toString()
            agent!!.surname = editTextSurname!!.text.toString()
            apiManager!!.updateAgentData(agent, object : Callback<ResponseBody?> {
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
        return inflatedView
    }

    private fun initialize(inflatedView: View) {
        sharedPreferencesPhone = this.requireActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE)
        Logout = inflatedView.findViewById(R.id.Logout)
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
                object : Callback<Agent?> {
                    override fun onResponse(call: Call<Agent?>, response: Response<Agent?>) {
                        agent = response.body()
                        textViewPhone!!.text = agent!!.phone
                        editTextSurname!!.setText(agent!!.surname)
                        editTextPatronymic!!.setText(agent!!.patronymic)
                        textViewSurName!!.text = agent!!.name
                        editTextEmail!!.setText(agent!!.email)
                        editTextNameAgent!!.setText(agent!!.name)
                    }

                    override fun onFailure(call: Call<Agent?>, t: Throwable) {
                        Toast.makeText(context, "Error is " + t.message, Toast.LENGTH_LONG).show()
                    }
                })
        }

    companion object {
        var apiManager: ApiManager? = null
    }
}