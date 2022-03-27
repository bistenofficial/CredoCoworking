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

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class ProfileFragment extends Fragment {
    View inflatedView;
    SharedPreferences sharedPreferencesPhone;
    TextView textViewPhone;
    EditText editTextSurname;
    String[] words;
    Button buttonLogin;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.profile_fragment, container, false);
        buttonLogin = inflatedView.findViewById(R.id.buttonGetData);
        textViewPhone = inflatedView.findViewById(R.id.textViewPhone);
        editTextSurname = inflatedView.findViewById(R.id.editTextSurname);
        sharedPreferencesPhone = this.getActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "Phone";
                String[] data = new String[1];
                data[0] = sharedPreferencesPhone.getString("Phone", "");
                PutData putData = new PutData("http://192.168.0.109/LoginRegister/getagentdata.php", "POST", field, data);//Необходимо менять локальный IP адрес устройств
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        words = result.split("/");
                        Log.i("PutData", result);
                        textViewPhone.setText("Phone " + words[0]);
                        editTextSurname.setText(words[1]);
                    }
                }
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return inflatedView;

    }
}
