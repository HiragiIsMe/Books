package com.example.asynctest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LoginAcitvity extends AppCompatActivity {
    Button login, signup;
    EditText email, password;
    Context ctx;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitvity);

        ctx = this;
        session = new Session(ctx);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        if (session.getToken() == "") {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(email.getText().length() != 0 || password.getText().length() != 0){
                        JSONObject data = new JSONObject();
                        try {
                            data.put("email", email.getText().toString());
                            data.put("password", password.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LoginTask task = new LoginTask();
                        task.execute(data.toString());
                    }else{
                        Toast.makeText(getApplicationContext(),"Can't Find The User",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Intent intent = new Intent(LoginAcitvity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginAcitvity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public class LoginTask extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(LoginAcitvity.this);
        @Override
        protected String doInBackground(String... strings) {
            JSONObject obj = RequestTemplate.GetJsonUrlParam("http://10.0.2.2:5000/Api/Auth", strings[0]);
            return obj.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s == null){
                Toast.makeText(getApplicationContext(),"Can't Find The User",Toast.LENGTH_LONG).show();
            }else{
               try{
                    JSONObject obj = new JSONObject(s);
                    session.SetToken(obj.getString("token"));
                    Intent intent = new Intent(LoginAcitvity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Authenticate");
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}