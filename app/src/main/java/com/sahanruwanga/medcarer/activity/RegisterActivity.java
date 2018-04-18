package com.sahanruwanga.medcarer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.sahanruwanga.medcarer.R;
import android.widget.Toast;

import com.sahanruwanga.medcarer.app.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setFullName((EditText)findViewById(R.id.fullName));
        setEmail((EditText)findViewById(R.id.email));
        setPassword((EditText)findViewById(R.id.password));
    }

    public void doRegister(View view) {
        String name = getFullName().getText().toString().trim();
        String email = getEmail().getText().toString().trim();
        String password = getPassword().getText().toString().trim();

        // Check required details and create new User object to be registered
        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
//            registerUser(name, email, password);
            new User(name, email, password, this).register();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    // Link to open login activity
    public void openLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //region Getters and Setters
    public EditText getFullName() {
        return fullName;
    }

    public void setFullName(EditText fullName) {
        this.fullName = fullName;
    }

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
