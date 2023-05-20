package com.example.asynctest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText name, email, password, confirm;
    Button submit;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.passwordConf);
        submit = findViewById(R.id.register);
        ctx = this;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("name", name.getText().toString());
                        obj.put("password", password.getText().toString());
                        obj.put("email", email.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SignUp sign = new SignUp();
                    sign.execute(obj.toString());
                }
            }
        });


    }

    public boolean validate() {

        if(name.getText().length() == 0 || email.getText().length() == 0 || password.getText().length() == 0 || confirm.getText().length() == 0){
            Toast.makeText(ctx, "All Field Must Be Filled", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().length() < 8){
            Toast.makeText(ctx, "Password Must Be At Least 8 Digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.getText().toString().equals(confirm.getText().toString())){
            Toast.makeText(ctx, "Password Must Be Same With Confirm Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValidPassword(password.getText().toString())){
            Toast.makeText(ctx, "Password Must Be have a digit, a letter and a symbol", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            Toast.makeText(ctx, "Email Not Valid", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public class SignUp extends AsyncTask<String, String, Integer> {
        ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Registered");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s == 200){
                Toast.makeText(ctx, "User Success Registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginAcitvity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(ctx, "User Failed Registered", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int response = RequestTemplate.getResponseSignup("http://10.0.2.2:5000/Api/Users", strings[0]);
            return response;
        }
    }
}