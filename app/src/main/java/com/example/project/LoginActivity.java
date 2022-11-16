package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    int port = 8888;
    String location = "10.0.2.2";
    String uri = "ws://" + location + ":" + port;
    Client socket = new Client();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText server = findViewById(R.id.inputID);
        final EditText user = findViewById(R.id.inputUsernameLogin);
        final EditText password = findViewById(R.id.inputPasswordLogin);
        final Button loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                socket.conectando();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> users = socket.getUsers();
                for (String key : users.keySet()) {
                    if (user.getText().toString().equals(key) && password.getText().toString().equals(users.get(user.getText().toString()))) {
                        Toast.makeText(LoginActivity.this, "LOGIN CORRECTE", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}


