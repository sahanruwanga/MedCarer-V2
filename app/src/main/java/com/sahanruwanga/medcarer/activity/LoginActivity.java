package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sahanruwanga.medcarer.R;
import android.widget.Toast;

import com.sahanruwanga.medcarer.app.User;


public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Email and Password fields initialization
        setEmail((EditText)findViewById(R.id.email));
        setPassword((EditText)findViewById(R.id.password));

        new User(this).checkLoginStatus();
    }

    public void doLogin(View view) {
        String email = getEmail().getText().toString().trim();
        String password = getPassword().getText().toString().trim();

        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {
            // login user
            User user = new User(this);
            user.setEmail(email);
            user.setPassword(password);
            user.login();
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Please enter the credentials!", Toast.LENGTH_LONG)
                    .show();
        }

    }

    // Call to open Register activity
    public void openRegisterActivity(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    //region Getters and Setters
    public EditText getEmail() {
        return email;
    }

    public void setEmail(EditText email) {
        this.email = email;
    }

    public EditText getPassword() {
        return password;
    }

    public void setPassword(EditText password) {
        this.password = password;
    }
    //endregion
}
