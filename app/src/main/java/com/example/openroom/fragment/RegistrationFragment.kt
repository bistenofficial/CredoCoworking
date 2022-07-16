package com.example.openroom.fragment

import android.os.Bundle
import android.text.Selection
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.openroom.R
import com.example.openroom.api.ApiManager
import com.example.openroom.databinding.RegistrationFragmentBinding
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
    private var phone: String? = null
    private var password: String? = null
    private var userSalt: String? = null
    private var passwordResult: String? = null

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!
    private var visibilityCheck = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding){
        super.onViewCreated(view, savedInstanceState)
        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(EditTextPhone)

        buttonRegistration.setOnClickListener {
            password = EditTextPassword.text.toString()
            phone = EditTextPhone.text.toString()
            if (fieldCheck()) {
                hashing()
                registrationRequest()
            }
        }
        buttonVisibilityReg.setOnClickListener()
        {
            val imgLeft = ResourcesCompat.getDrawable(resources, R.drawable.custom_lock_icon, null)
            if (visibilityCheck) {
                EditTextPassword.transformationMethod = null
                val position = EditTextPassword.length()
                val imgEnd =
                    ResourcesCompat.getDrawable(resources, R.drawable.custom_visibility, null)
                EditTextPassword.setCompoundDrawablesWithIntrinsicBounds(
                    imgLeft,
                    null,
                    imgEnd,
                    null
                )
                val etExt = EditTextPassword.text
                Selection.setSelection(etExt, position)
                visibilityCheck = false
            } else {
                val imgEnd =
                    ResourcesCompat.getDrawable(resources, R.drawable.custom_visibility_off, null)
                EditTextPassword.setCompoundDrawablesWithIntrinsicBounds(
                    imgLeft,
                    null,
                    imgEnd,
                    null
                )
                EditTextPassword.transformationMethod = PasswordTransformationMethod()
                visibilityCheck = true
                val position = EditTextPassword.length()
                val eText = EditTextPassword.text
                Selection.setSelection(eText, position)
            }
        }
        textViewSignIn.setOnClickListener { findNavController().navigate(R.id.action_registrationFragment_to_authFragment) }
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
                } else if (response.code() == 406) {
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
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}