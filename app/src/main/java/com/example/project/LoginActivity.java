package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    int port = 8888;
    String location = "10.0.2.2";
    String uri = "ws://" + location + ":" + port;
    static Client socket = new Client();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button loginButton = findViewById(R.id.btnLogin);
        final EditText server = findViewById(R.id.inputID);
        final EditText user = findViewById(R.id.inputUsernameLogin);
        final EditText password = findViewById(R.id.inputPasswordLogin);
        TextView btn = findViewById(R.id.textViewSignUp);
        Client.act = LoginActivity.this;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                socket.connecta();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String[] usersArray = {user.getText().toString(), password.getText().toString()};
                socket.client.send(socket.objToBytes(usersArray));

                startActivity(new Intent(LoginActivity.this, ComponentesActivity.class));
                server.setText("");
                user.setText("");
                password.setText("");

                Toast toast = Toast.makeText(LoginActivity.this, "LOGIN CORRECTO", Toast.LENGTH_SHORT);
                toast.show();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentesActivity.socket = LoginActivity.socket;
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
    public void login(boolean isCorrect) {
        if (isCorrect) {
            socket.client.send("getModel");
            ComponentesActivity.socket = LoginActivity.socket;
            Intent intent = new Intent(LoginActivity.this, ComponentesActivity.class);
            startActivity(intent);
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Client.act);
                    builder.setTitle("Connection error");
                    builder.setMessage("There has been an error with the Server connection, please try again later, we are working on it.\nThank you!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.i("hola","deu");
                        }
                    });
                    builder.show();
                }
            });
        }
    }

}