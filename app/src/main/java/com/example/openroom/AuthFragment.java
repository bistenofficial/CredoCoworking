package com.example.openroom;

import android.content.Intent;
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

public class AuthFragment extends Fragment
{
    View inflatedView;
    private Button buttonLogin;
    private TextView textViewCreate;
    private EditText editTextPassword;
    private EditText editTextLogin;
    Toast toast;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        inflatedView = inflater.inflate(R.layout.authorization_fragment, container, false);
        textViewCreate = inflatedView.findViewById(R.id.textViewCreate);
        editTextPassword = inflatedView.findViewById(R.id.EditTextPassword);
        editTextLogin = inflatedView.findViewById(R.id.EditTextLogin);
        buttonLogin = inflatedView.findViewById(R.id.buttonLogin);
        textViewCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                RegistrationFragment registrationFragment = new RegistrationFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fr_place,registrationFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String password = editTextPassword.getText().toString();
                String login = editTextLogin.getText().toString();

                if ((password.length()!=0) && (login.length()!=0))
                {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "Phone";
                            field[1] = "Password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = login;
                            data[1] = password;
                            PutData putData = new PutData("http://192.168.0.108/LoginRegister/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    //End ProgressBar (Set visibility to GONE)
                                    Log.i("PutData", result);
                                    if (result.equals("Login Success"))
                                    {
                                        toast = Toast.makeText(getContext(),"Успешный вход",Toast.LENGTH_LONG);
                                        toast.show();
                                        Intent intentSign = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intentSign);
                                        getActivity().finish();
                                    }
                                    else
                                    {
                                        toast = Toast.makeText(getContext(),"Ошибка входа",Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else
                {
                    toast = Toast.makeText(getContext(),"Ошибка ввода",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return inflatedView;
    }

}
