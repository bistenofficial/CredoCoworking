package com.example.openroom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.openroom.R;
import com.example.openroom.api.Agent;
import com.example.openroom.api.ApiManager;
import com.google.common.hash.Hashing;


import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;


public class RegistrationFragment extends Fragment {
    private EditText editTextPasswordRepeat, editTextLogin, editTextPassword;
    private Button buttonSignUp;
    private String phone;
    private String password;
    private String passwordRepeat;
    private String userSalt;
    private String passwordResult;
    Toast toast;

    public static ApiManager apiManager;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        initialize(view);

        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(editTextLogin);

        buttonSignUp.setOnClickListener(view1 -> {

            passwordRepeat = editTextPasswordRepeat.getText().toString();
            password = editTextPassword.getText().toString();
            phone = editTextLogin.getText().toString();
            if (fieldCheck()) {
                hashing();
                registrationRequest();
            }
        });
        return view;
    }

    private void initialize(View view) {
        editTextPasswordRepeat = view.findViewById(R.id.EditTextPasswordRepeat);
        editTextPassword = view.findViewById(R.id.EditTextPassword);
        editTextLogin = view.findViewById(R.id.EditTextPhone);
        buttonSignUp = view.findViewById(R.id.buttonRegistration);
    }

    private boolean fieldCheck() {
        if ((password.length() != 0) && (passwordRepeat.length() != 0) && (phone.length() != 0)) {
            if ((password.length() >= 8) && (passwordRepeat.length() >= 8)) {
                if (password.equals(passwordRepeat)) {
                    return true;
                } else {
                    toast = Toast.makeText(getContext(), getString(R.string.Passwords_not_match), Toast.LENGTH_SHORT);
                    toast.show();
                    return false;
                }
            } else {
                toast = Toast.makeText(getContext(), getString(R.string.MinLenPassword), Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        } else {
            toast = Toast.makeText(getContext(), getString(R.string.Not_fields_filled), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
    }

    private void hashing() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long time = timestamp.getTime();
        String timeString = String.valueOf(time);

        phone = phone.replaceAll("\\D+", "");

        String hardCodeSalt = Hashing.sha512().hashString("open" + Hashing.sha512().hashString("room", StandardCharsets.UTF_8), StandardCharsets.UTF_8).toString();

        userSalt = Hashing.sha512().hashString(timeString + phone, StandardCharsets.UTF_8).toString();

        password = Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();

        String salt = Hashing.sha512().hashString(hardCodeSalt + userSalt, StandardCharsets.UTF_8).toString();

        passwordResult = Hashing.sha512().hashString(salt + password, StandardCharsets.UTF_8).toString();

    }

    private void registrationRequest() {

        apiManager = ApiManager.getInstance();
        Agent agent = new Agent(null, null, null, phone, null, passwordResult, userSalt, 0, null, null);
        apiManager.createAgent(agent, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    Toast.makeText(getContext(), getString(R.string.Successful_registration), Toast.LENGTH_SHORT).show();

                    AuthFragment authFragment = new AuthFragment();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fr_place, authFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                } else if (response.code() == 400) {
                    Toast.makeText(getContext(), getString(R.string.User_already_registered), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
