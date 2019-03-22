package com.example.exploradordeviajes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exploradordeviajes.Helpers.ApiError;
import com.example.exploradordeviajes.Helpers.ApiSuccess;
import com.example.exploradordeviajes.Helpers.ErrorsUtils;
import com.example.exploradordeviajes.Helpers.SuccessUtils;
import com.example.exploradordeviajes.Modelos.Users;
import com.example.exploradordeviajes.apis.ApiUtils;
import com.example.exploradordeviajes.apis.RegisterService;
import com.example.exploradordeviajes.apis.RetroFitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class Register extends AppCompatActivity {
    private TextView mResponseTv;
    private RegisterService registerService;
    private static final String TAG = "Register activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText emailET = (EditText)findViewById(R.id.email);
        final EditText passwordET = (EditText)findViewById(R.id.password);
        Button submitBtn = (Button) findViewById(R.id.registrarse);

        registerService = ApiUtils.getAPIService();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    Users user = new Users(email,password);
                    registerUser(user);
                }
            }
        });
    }

    public void registerUser(Users user) {

        // asynchronously sends the request and notifies
        registerService.registerUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {

                if(response.isSuccessful()) {
                    ApiSuccess success = SuccessUtils.parserSuccess(response, RetroFitClient.getRetrofitInstance());
                    Toast.makeText(getApplicationContext(),success.getToken(),Toast.LENGTH_LONG).show();// Set your own toast  message
                    Toast.makeText(getApplicationContext(),success.getMessage(),Toast.LENGTH_LONG).show();// Set your own toast  message
                    Log.i(TAG, "post submitted to API." + response.body());
                }else{
                    ApiError error = ErrorsUtils.parserError(response, RetroFitClient.getRetrofitInstance());
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();// Set your own toast  message
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showBadResponse(t);
                Log.e(TAG, t.getMessage());
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void showResponse(Object response) {
        System.out.println("================ Response ==================");
        System.out.println(response);
        System.out.println("==================================");
    }
    public void showBadResponse(Throwable response) {
        System.out.println("================BAD Response ==================");
        System.out.println(response);
        System.out.println("==================================");
    }

}
