package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    int port = 8888;
    String location = "24.01.17";
    String uri = "ws://" + location + ":" + port;
    Client socket = new Client();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button loginButton = findViewById(R.id.btnLogin);
        final EditText server = findViewById(R.id.inputID);
        final EditText user = findViewById(R.id.inputUsernameLogin);
        final EditText password = findViewById(R.id.inputPasswordLogin);
        TextView btn = findViewById(R.id.textViewSignUp);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.connecta();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(LoginActivity.this, "LOGIN OK", Toast.LENGTH_SHORT);
                toast.show();
                String[] arrayUser = {user.getText().toString(), password.getText().toString()};
                socket.client.send(socket.objToBytes(arrayUser));
                startActivity(new Intent(LoginActivity.this, ComponentesActivity.class));

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}