package com.example.openroom;

import android.content.Intent;
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

public class AuthFragment extends Fragment
{
    View inflatedView;
    private Button buttonLogin;
    private TextView textViewCreate;
    private EditText editTextPassword;
    Toast toast;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        inflatedView = inflater.inflate(R.layout.authorization_fragment, container, false);
        buttonLogin = inflatedView.findViewById(R.id.buttonLogin);
        editTextPassword = inflatedView.findViewById(R.id.EditTextPassword);
        textViewCreate = inflatedView.findViewById(R.id.textViewCreate);
        textViewCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String text = editTextPassword.getText().toString();
                if (editTextPassword.getText().length()!=0)
                {
                    RegistrationFragment registrationFragment = new RegistrationFragment();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fr_place,registrationFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else
                {
                    toast = Toast.makeText(getContext(),"Ошибка ввода",Toast.LENGTH_LONG);
                }
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intentSign = new Intent(getActivity(), MainActivity.class);
                startActivity(intentSign);
                getActivity().finish();
            }
        });

        return inflatedView;
    }

}
