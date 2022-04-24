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

import org.signal.argon2.Argon2;
import org.signal.argon2.Argon2Exception;
import org.signal.argon2.MemoryCost;
import org.signal.argon2.Type;
import org.signal.argon2.Version;

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
    private String phone, password, passwordRepeat, timeString, userSalt, hardCodeSalt, passwordResult;
    Toast toast;

    public static ApiManager apiManager;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        initialize(view);

        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(editTextLogin);

        buttonSignUp.setOnClickListener(view1 -> {
            hashing();
            passwordRepeat = editTextPasswordRepeat.getText().toString();
            if (fieldCheck()) {
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
        timeString = String.valueOf(time);

        phone = editTextLogin.getText().toString();
        phone = phone.replaceAll("\\D+","");
        password = editTextPassword.getText().toString();

        hardCodeSalt = Hashing.sha256().hashString("open" + Hashing.sha256().hashString("room", StandardCharsets.UTF_8), StandardCharsets.UTF_8).toString();

        userSalt = Hashing.sha256().hashString(timeString + phone, StandardCharsets.UTF_8).toString();


        Argon2 argon2 = new Argon2.Builder(Version.V13)
                .type(Type.Argon2i)
                .memoryCost(MemoryCost.MiB(64))
                .parallelism(1)
                .iterations(10)
                .hashLength(32)
                .build();
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] saltBytes = Hashing.sha256().hashString(hardCodeSalt + userSalt, StandardCharsets.UTF_8).toString().getBytes(StandardCharsets.UTF_8);

        try {
            Argon2.Result result = argon2.hash(passwordBytes, saltBytes);
            passwordResult = result.getHashHex();
        } catch (Argon2Exception e) {
            e.printStackTrace();
        }
    }

    private void registrationRequest() {

        apiManager = ApiManager.getInstance();
        Agent agent = new Agent(null, null, null, phone, null, passwordResult, userSalt.toString(), 0, null, null);
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
