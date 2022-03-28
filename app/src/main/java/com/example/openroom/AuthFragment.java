package com.example.openroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AuthFragment extends Fragment {
    View inflatedView;
    private Button buttonLogin;
    private TextView textViewCreate;
    private EditText editTextPassword;
    private String phone, password, timeString;
    private EditText editTextLogin;
    SharedPreferences sharedPreferencesPhone;
    Toast toast;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferencesPhone = this.getActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE);
        inflatedView = inflater.inflate(R.layout.authorization_fragment, container, false);
        textViewCreate = inflatedView.findViewById(R.id.textViewCreate);
        editTextPassword = inflatedView.findViewById(R.id.EditTextPassword);
        editTextLogin = inflatedView.findViewById(R.id.EditTextLogin);
        buttonLogin = inflatedView.findViewById(R.id.buttonLogin);
        textViewCreate.setOnClickListener(view -> {

            RegistrationFragment registrationFragment = new RegistrationFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fr_place, registrationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        buttonLogin.setOnClickListener(view -> {
            phone = editTextLogin.getText().toString();
            password = editTextPassword.getText().toString();
            if (checkData())
            {
                signIn();
            }
        });
        return inflatedView;
    }

    private void signIn() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "Phone";
                field[1] = "Password";
                String[] data = new String[2];
                data[0] = phone;
                data[1] = password;
                PutData putData = new PutData("http://192.168.88.43/LoginRegister/login.php", "POST", field, data);//Необходимо менять локальный IP адрес устройств
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        Log.i("PutData", result);
                        if (result.equals("Login Success")) {
                            toast = Toast.makeText(getContext(), "Успешный вход", Toast.LENGTH_LONG);
                            toast.show();
                            SharedPreferences.Editor editor = sharedPreferencesPhone.edit();
                            editor.putString("Phone", phone);
                            editor.apply();
                            Intent intentSign = new Intent(getActivity(), MainActivity.class);
                            startActivity(intentSign);
                            getActivity().finish();
                        } else {
                            toast = Toast.makeText(getContext(), "Ошибка входа", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
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
