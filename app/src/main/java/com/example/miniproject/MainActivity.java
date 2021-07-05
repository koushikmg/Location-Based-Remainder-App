package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView register;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout emailError, passError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        passError = (TextInputLayout) findViewById(R.id.passError);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SetValidation();
                Intent intent = getIntent();
                String name = intent.getStringExtra("name");
                String email = intent.getStringExtra("email");
                String phno = intent.getStringExtra("phno");
                String password = intent.getStringExtra("pass");
                Boolean validLogin = Validate(email,password);
                if(validLogin){
                    Intent intent1 = new Intent(MainActivity.this,RemainderActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("email",email);
                    intent.putExtra("phno",phno);
                    intent.putExtra("pass",password);
                    startActivity(intent1);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to RegisterActivity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private Boolean Validate(String mail, String pass) {
        Boolean validLogin = false;
        if(email.getText().toString().equals(mail) && password.getText().toString().equals(pass))
        {
            validLogin = true;
        }
        return validLogin;
    }


    private void SetValidation() {
        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if (isEmailValid && isPasswordValid) {
            Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
        }

    }

}