package com.example.openroom.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.openroom.R
import com.example.openroom.api.ApiManager
import com.example.openroom.fragment.ProfileFragment.Companion.apiManager
import com.example.openroom.model.AgentModel
import com.google.common.hash.Hashing
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.nio.charset.StandardCharsets
import java.sql.Timestamp

class RegistrationFragment : Fragment(R.layout.registration_fragment) {
    private var editTextLogin: EditText? = null
    private var editTextPassword: EditText? = null
    private var buttonSignUp: Button? = null
    private var textViewSignIn: TextView? = null
    private var phone: String? = null
    private var password: String? = null
    private var userSalt: String? = null
    private var passwordResult: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view)
        buttonSignUp!!.setOnClickListener {
            password = editTextPassword!!.text.toString()
            phone = editTextLogin!!.text.toString()
            if (fieldCheck()) {
                hashing()
                registrationRequest()
            }
        }
        textViewSignIn!!.setOnClickListener { findNavController().navigate(R.id.action_registrationFragment_to_authFragment) }
    }

    private fun initialize(view: View) {
        editTextPassword = view.findViewById(R.id.EditTextPassword)
        textViewSignIn = view.findViewById(R.id.textViewSignIn)
        editTextLogin = view.findViewById(R.id.EditTextPhone)
        buttonSignUp = view.findViewById(R.id.buttonRegistration)
        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(editTextLogin!!)
    }

    private fun fieldCheck(): Boolean {
        return if (password!!.isNotEmpty() && phone!!.isNotEmpty()) {
            if (password!!.length >= 8) true else {
                Toast.makeText(context, getString(R.string.MinLenPassword), Toast.LENGTH_SHORT)
                    .show()
                false
            }
        } else {
            Toast.makeText(context, getString(R.string.Not_fields_filled), Toast.LENGTH_SHORT)
                .show()
            false
        }
    }

    private fun hashing() {
        val timestamp = Timestamp(System.currentTimeMillis())
        val time = timestamp.time
        val timeString = time.toString()
        phone = phone!!.replace("\\D+".toRegex(), "")
        val hardCodeSalt = Hashing.sha512().hashString(
            "open" + Hashing.sha512().hashString("room", StandardCharsets.UTF_8),
            StandardCharsets.UTF_8
        ).toString()
        userSalt =
            Hashing.sha512().hashString(timeString + phone, StandardCharsets.UTF_8).toString()
        password =
            Hashing.sha512().hashString(password.toString(), StandardCharsets.UTF_8).toString()
        val salt =
            Hashing.sha512().hashString(hardCodeSalt + userSalt, StandardCharsets.UTF_8).toString()
        passwordResult =
            Hashing.sha512().hashString(salt + password, StandardCharsets.UTF_8).toString()
    }

    private fun registrationRequest() {
        apiManager = ApiManager.getInstance()
        val agentModel = AgentModel(
            null,
            null,
            null,
            phone,
            null,
            passwordResult,
            userSalt,
            0,
            null,
            null
        )
        apiManager!!.createAgent(agentModel, object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.code() == 201) {
                    Toast.makeText(
                        context,
                        getString(R.string.Successful_registration),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_registrationFragment_to_authFragment)
                } else if (response.code() == 400) {
                    Toast.makeText(
                        context,
                        getString(R.string.User_already_registered),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "Error is " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}