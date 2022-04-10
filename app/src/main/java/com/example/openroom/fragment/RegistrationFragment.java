package com.example.openroom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.openroom.MainApplication;
import com.example.openroom.R;
import com.example.openroom.api.Agent;
import com.example.openroom.api.ApiManager;
import com.example.openroom.fragment.AuthFragment;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
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
    private String phone, password, passwordRepeat, timeString;
    Toast toast;
    public static ApiManager apiManager;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);


        editTextPasswordRepeat = view.findViewById(R.id.EditTextPasswordRepeat);
        editTextPassword = view.findViewById(R.id.EditTextPassword);
        editTextLogin = view.findViewById(R.id.EditTextPhone);
        buttonSignUp = view.findViewById(R.id.buttonRegistration);

        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(editTextLogin);

        buttonSignUp.setOnClickListener(view1 -> {
            phone = editTextLogin.getText().toString();
            password = editTextPassword.getText().toString();
            passwordRepeat = editTextPasswordRepeat.getText().toString();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long time = timestamp.getTime();
            timeString = String.valueOf(time);

            String salts = Hashing.sha256().hashString("open" + Hashing.sha256().hashString("room", StandardCharsets.UTF_8).toString(), StandardCharsets.UTF_8).toString();
            final String hashed = Hashing.sha256().hashString(timeString + salts + phone, StandardCharsets.UTF_8).toString();

            if (checkData()) {
                signUp();
            }
        });
        return view;
    }

    private boolean checkData() {
        if ((password.length() != 0) && (passwordRepeat.length() != 0) && (phone.length() != 0)) {
            if ((password.length() > 8) && (passwordRepeat.length() > 8)) {
                if (password.equals(passwordRepeat)) {
                    return true;
                } else {
                    toast = Toast.makeText(getContext(), getString(R.string.Passwords_not_match), Toast.LENGTH_LONG);
                    toast.show();
                    return false;
                }
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

    private void signUp() {
        apiManager = ApiManager.getInstance();
        Agent agent = new Agent(null, null, null, editTextLogin.getText().toString(), null, editTextPassword.getText().toString(), null, 0, 4, null);
        apiManager.createAgent(agent, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    Toast.makeText(getContext(), "Успешная регистрация", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    Toast.makeText(getContext(), "Пользователь уже зарегестрирован", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

/*        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "Phone";
                field[1] = "password";
                String[] data = new String[2];
                data[0] = phone;
                data[1] = password;
                PutData putData = new PutData("http://192.168.0.104/LoginRegister/signup.php", "POST", field, data);//Необходимо менять локальный IP адрес устройств
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        Log.i("PutData", result);
                        if (result.equals("Sign Up Success")) {
                            toast = Toast.makeText(getContext(), getString(R.string.Successful_registration), Toast.LENGTH_LONG);
                            toast.show();
                            AuthFragment authFragment = new AuthFragment();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.fr_place, authFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            toast = Toast.makeText(getContext(), getString(R.string.Registration_error), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
            }
        });*/
    }
}
