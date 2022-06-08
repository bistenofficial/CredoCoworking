package com.example.openroom.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Selection
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.openroom.R
import com.example.openroom.model.AgentModel
import com.example.openroom.api.ApiManager
import com.example.openroom.fragment.ProfileFragment.Companion.apiManager
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

class AuthFragment : Fragment(R.layout.authorization_fragment) {
    private var buttonLogin: Button? = null
    private var buttonVisibility: Button? = null
    private var textViewCreate: TextView? = null
    private var editTextPassword: EditText? = null
    private var editTextLogin: EditText? = null
    private var phone: String? = null
    private var password: String? = null
    var sharedPreferencesPhone: SharedPreferences? = null
    private var visibilityCheck = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view)

        textViewCreate!!.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_registrationFragment)
        }

        buttonLogin!!.setOnClickListener {
            phone = editTextLogin!!.text.toString()
            phone = phone!!.replace("\\D+".toRegex(), "")
            password = editTextPassword!!.text.toString()
            if (checkData())
            {
                signIn()
            }
        }
        buttonVisibility!!.setOnClickListener {
            val imgLeft = ResourcesCompat.getDrawable(resources, R.drawable.custom_lock_icon, null)
            if (visibilityCheck) {
                editTextPassword!!.transformationMethod = null
                val position = editTextPassword!!.length()
                val imgEnd =
                    ResourcesCompat.getDrawable(resources, R.drawable.custom_visibility, null)
                editTextPassword!!.setCompoundDrawablesWithIntrinsicBounds(
                    imgLeft,
                    null,
                    imgEnd,
                    null
                )
                val etExt = editTextPassword!!.text
                Selection.setSelection(etExt, position)
                visibilityCheck = false
            } else {
                val imgEnd =
                    ResourcesCompat.getDrawable(resources, R.drawable.custom_visibility_off, null)
                editTextPassword!!.setCompoundDrawablesWithIntrinsicBounds(
                    imgLeft,
                    null,
                    imgEnd,
                    null
                )
                editTextPassword!!.transformationMethod = PasswordTransformationMethod()
                visibilityCheck = true
                val position = editTextPassword!!.length()
                val eText = editTextPassword!!.text
                Selection.setSelection(eText, position)
            }
        }
    }

    private fun initialize(view: View) {
        textViewCreate = view.findViewById(R.id.textViewSignUp)
        editTextPassword = view.findViewById(R.id.EditPassword)
        editTextLogin = view.findViewById(R.id.EditPhone)
        buttonLogin = view.findViewById(R.id.buttonLogin)
        buttonVisibility = view.findViewById(R.id.buttonVisibility)
        sharedPreferencesPhone =
            this.requireActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE)
        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(editTextLogin!!)
    }

    private fun signIn() {
        password = Hashing.sha512().hashString(password.toString(), StandardCharsets.UTF_8).toString()
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
                        Toast.makeText(context, getString(R.string.wrong_password), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "Error is " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun checkData(): Boolean
    {
        return if (password!!.isNotEmpty() && phone!!.isNotEmpty()) {
            if (password!!.length > 7) {
                true
            } else {
                Toast.makeText(context, getString(R.string.MinLenPassword), Toast.LENGTH_LONG).show()
                false
            }
        } else {
            Toast.makeText(context, getString(R.string.Not_fields_filled), Toast.LENGTH_LONG).show()
            false
        }
    }
}