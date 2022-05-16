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

import com.example.openroom.R;
import com.example.openroom.activity.LogRegActivity;
import com.example.openroom.api.Agent;
import com.example.openroom.api.ApiManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    SharedPreferences sharedPreferencesPhone;
    TextView textViewPhone, textViewSurName, Logout;
    EditText editTextSurname, editTextNameAgent, editTextPatronymic, editTextEmail;
    Button buttonChangeData;
    Agent agent;
    public static ApiManager apiManager;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.profile_fragment, container, false);
        initialize(inflatedView);
        getData();

        Logout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LogRegActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = sharedPreferencesPhone.edit();
            editor.putString("Phone", "");
            editor.apply();
            getActivity().finish();
        });

        buttonChangeData.setOnClickListener(v ->
        {
            apiManager = ApiManager.getInstance();
            agent.setEmail(editTextEmail.getText().toString());
            agent.setName(editTextNameAgent.getText().toString());
            agent.setPatronymic(editTextPatronymic.getText().toString());
            agent.setSurname(editTextSurname.getText().toString());

            apiManager.updateAgentData(agent, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    Toast.makeText(getContext(),"Успешное обновление данных",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        return inflatedView;

    }

    private void initialize(View inflatedView) {
        sharedPreferencesPhone = this.getActivity().getSharedPreferences("Phone", Context.MODE_PRIVATE);
        Logout = inflatedView.findViewById(R.id.Logout);
        textViewSurName = inflatedView.findViewById(R.id.textViewSurName);
        buttonChangeData = inflatedView.findViewById(R.id.buttonChangeData);
        textViewPhone = inflatedView.findViewById(R.id.textViewPhone);
        editTextNameAgent = inflatedView.findViewById(R.id.editTextNameAgent);
        editTextSurname = inflatedView.findViewById(R.id.editTextSurname);
        editTextPatronymic = inflatedView.findViewById(R.id.editTextPatronymic);
        editTextEmail = inflatedView.findViewById(R.id.editTextEmail);
    }

    private void getData() {
        apiManager = ApiManager.getInstance();
        apiManager.getAgentData(sharedPreferencesPhone.getString("Phone", ""), new Callback<Agent>() {
            @Override
            public void onResponse(Call<Agent> call, Response<Agent> response) {
                agent = response.body();
                textViewPhone.setText(agent.getPhone());
                editTextSurname.setText(agent.getSurname());
                editTextPatronymic.setText(agent.getPatronymic());
                textViewSurName.setText(agent.getName());
                editTextEmail.setText(agent.getEmail());
                editTextNameAgent.setText(agent.getName());
            }

            @Override
            public void onFailure(Call<Agent> call, Throwable t) {
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
