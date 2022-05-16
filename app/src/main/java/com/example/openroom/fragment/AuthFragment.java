package com.example.openroom.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.openroom.R;
import com.example.openroom.activity.MainActivity;
import com.example.openroom.api.Agent;
import com.example.openroom.api.ApiManager;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class AuthFragment extends Fragment {
    private Button buttonLogin;
    private TextView textViewCreate;
    private EditText editTextPassword;
    private String phone, password, timeString;
    private EditText editTextLogin;
    SharedPreferences sharedPreferencesPhone;
    Toast toast;

    public static ApiManager apiManager;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View views = inflater.inflate(R.layout.authorization_fragment, container, false);
        initialize(views);
        sharedPreferencesPhone = this.getActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE);
        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(editTextLogin);

        textViewCreate.setOnClickListener(view -> {

            RegistrationFragment registrationFragment = new RegistrationFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fr_place, registrationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        buttonLogin.setOnClickListener(view -> {
            phone = editTextLogin.getText().toString();
            phone = phone.replaceAll("\\D+", "");
            password = editTextPassword.getText().toString();
            if (checkData())
            {
                signIn();
            }
        });
        return views;
    }

    private void initialize(View view) {
        textViewCreate = view.findViewById(R.id.textViewCreate);
        editTextPassword = view.findViewById(R.id.EditTextPassword);
        editTextLogin = view.findViewById(R.id.EditTextLogin);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        sharedPreferencesPhone = this.getActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE);
    }

    private void signIn() {

        password = Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
        apiManager = ApiManager.getInstance();

        Agent agent = new Agent(null, null, null, phone, null, password, null, null, null, null);
        apiManager.signInAgent(agent, new Callback<ResponseBody>()
        {
            boolean login = false;
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 202: {
                        SharedPreferences.Editor editor = sharedPreferencesPhone.edit();
                        editor.putString("Phone", phone);;
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        editor.apply();
                        break;
                    }
                    case 404: {
                        Toast.makeText(getContext(), getString(R.string.There_is_no_such_account), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 406: {
                        Toast.makeText(getContext(), "Пароль или логин не совпадают", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkData() {
        if ((password.length() != 0) && (phone.length() != 0)) {
            if ((password.length() > 3)) {
                return true;
            } else {
                toast = Toast.makeText(getContext(), getString(R.string.MinLenPassword), Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
        } else {
            toast = Toast.makeText(getContext(), getString(R.string.Not_fields_filled), Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

}
