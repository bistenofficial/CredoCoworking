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
    TextView textViewPhone,textViewSurName,Logout;
    EditText editTextSurname,editTextNameAgent,editTextPatronymic,editTextEmail;
    String[] words;
    Button buttonChangeData;
    Toast toast;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.profile_fragment, container, false);

        sharedPreferencesPhone = this.getActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE);

        Logout = inflatedView.findViewById(R.id.Logout);
        textViewSurName = inflatedView.findViewById(R.id.textViewSurName);
        buttonChangeData = inflatedView.findViewById(R.id.buttonChangeData);
        textViewPhone = inflatedView.findViewById(R.id.textViewPhone);
        editTextNameAgent = inflatedView.findViewById(R.id.editTextNameAgent);
        editTextSurname = inflatedView.findViewById(R.id.editTextSurname);
        editTextPatronymic = inflatedView.findViewById(R.id.editTextPatronymic);
        editTextEmail = inflatedView.findViewById(R.id.editTextEmail);
        getData();
        Logout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LogRegActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = sharedPreferencesPhone.edit();
            editor.putString("Phone", "");
            editor.apply();
            getActivity().finish();
        });

        buttonChangeData.setOnClickListener(v -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                String[] field = new String[5];
                field[0] = "Phone";
                field[1] = "Surname";
                field[2] = "Name";
                field[3] = "Patronymic";
                field[4] = "Email";
                String[] data = new String[5];
                data[0] = sharedPreferencesPhone.getString("Phone", "");
                data[1] = editTextSurname.getText().toString();
                data[2] = editTextNameAgent.getText().toString();
                data[3] = editTextPatronymic.getText().toString();
                data[4] = editTextEmail.getText().toString();
                PutData putData = new PutData("http://192.168.88.43/LoginRegister/changeagentdata.php", "POST", field, data);//Необходимо менять локальный IP адрес устройств
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        Log.i("PutData", result);
                        if (result.equals("Change data Success"))
                        {
                            getData();
                            toast = Toast.makeText(getContext(),getString(R.string.Data_updated_successfully),Toast.LENGTH_LONG);
                        }
                        else
                        {
                            toast = Toast.makeText(getContext(),getString(R.string.Data_update_error),Toast.LENGTH_LONG);
                        }
                        toast.show();
                    }
                }
            });
        });
        return inflatedView;

    }
    private void getData()
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            String[] field = new String[1];
            field[0] = "Phone";
            String[] data = new String[1];
            data[0] = sharedPreferencesPhone.getString("Phone", "");
            PutData putData = new PutData("http://192.168.88.43/LoginRegister/getagentdata.php", "POST", field, data);//Необходимо менять локальный IP адрес устройств
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    words = result.split("/");
                    Log.i("PutData", result);
                    textViewPhone.setText("Phone " + words[0]);
                    textViewSurName.setText(words[1]+" "+words[2]);
                    editTextSurname.setText(words[1]);
                    editTextNameAgent.setText(words[2]);
                    editTextPatronymic.setText(words[3]);
                    editTextEmail.setText(words[4]);
                }
            }
        });
    }
}
