package com.example.openroom.fragment

import android.content.Context
import android.content.SharedPreferences
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
import com.example.openroom.databinding.AuthorizationFragmentBinding
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

class AuthFragment : Fragment() {
    private var phone: String? = null
    private var password: String? = null
    var sharedPreferencesPhone: SharedPreferences? = null
    private var visibilityCheck = true

    private var _binding: AuthorizationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthorizationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesPhone = requireActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE)

        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(EditPhone)

        textViewSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_registrationFragment)
        }

        buttonLogin.setOnClickListener {
            phone = EditPhone.text.toString()
            phone = phone!!.replace("\\D+".toRegex(), "")
            password = EditPassword.text.toString()
            if (checkData()) {
                signIn()
            }
        }
        buttonVisibility.setOnClickListener {
            val imgLeft = ResourcesCompat.getDrawable(resources, R.drawable.custom_lock_icon, null)
            if (visibilityCheck) {
                EditPassword.transformationMethod = null
                val position = EditPassword.length()
                val imgEnd =
                    ResourcesCompat.getDrawable(resources, R.drawable.custom_visibility, null)
                EditPassword.setCompoundDrawablesWithIntrinsicBounds(
                    imgLeft,
                    null,
                    imgEnd,
                    null
                )
                val etExt = EditPassword.text
                Selection.setSelection(etExt, position)
                visibilityCheck = false
            } else {
                val imgEnd =
                    ResourcesCompat.getDrawable(resources, R.drawable.custom_visibility_off, null)
                EditPassword.setCompoundDrawablesWithIntrinsicBounds(
                    imgLeft,
                    null,
                    imgEnd,
                    null
                )
                EditPassword.transformationMethod = PasswordTransformationMethod()
                visibilityCheck = true
                val position = EditPassword.length()
                val eText = EditPassword.text
                Selection.setSelection(eText, position)
            }
        }
    }

    private fun signIn() {
        password =
            Hashing.sha512().hashString(password.toString(), StandardCharsets.UTF_8).toString()
        apiManager = ApiManager.getInstance()
        val agentModel = AgentModel(
            null,
            null,
            null,
            phone,
            null,
            password,
            null,
            null,
            null,
            null
        )
        apiManager!!.signInAgent(agentModel, object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                when (response.code()) {
                    202 -> {
                        val editor = sharedPreferencesPhone!!.edit()
                        editor.putString("Phone", phone)
                        findNavController().navigate(R.id.action_authFragment_to_mainActivity)
                        editor.apply()
                        activity!!.finish()
                    }
                    404 -> {
                        Toast.makeText(
                            context,
                            getString(R.string.There_is_no_such_account),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    406 -> {
                        Toast.makeText(
                            context,
                            getString(R.string.wrong_password),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "Error is " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun checkData(): Boolean {
        return if (password!!.isNotEmpty() && phone!!.isNotEmpty()) {
            if (password!!.length > 7) {
                true
            } else {
                Toast.makeText(context, getString(R.string.MinLenPassword), Toast.LENGTH_LONG)
                    .show()
                false
            }
        } else {
            Toast.makeText(context, getString(R.string.Not_fields_filled), Toast.LENGTH_LONG).show()
            false
        }
    }
}