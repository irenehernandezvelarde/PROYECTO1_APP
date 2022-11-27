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
    //VARIABLES
    //Web connection
    int port = 8888;
    String location = "10.0.2.2";
    String uri = "ws://" + location + ":" + port;
    static Client socket = new Client();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //GUI
        final Button loginButton = findViewById(R.id.btnLogin);
        final EditText server = findViewById(R.id.inputID);
        final EditText user = findViewById(R.id.inputUsernameLogin);
        final EditText password = findViewById(R.id.inputPasswordLogin);
        TextView registerButton = findViewById(R.id.textViewSignUp);
        Client.act = LoginActivity.this;

        //LOGIN
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                //SERVER CONNECTION
                socket.connecta();
                //Waiting for connection to open (triga una mica)
                try {Thread.sleep(2000);}
                catch (InterruptedException e) {e.printStackTrace();}
                if (socket.client.isOpen()) { //Si s'ha pogut conectar
                    //CREDENTIAL CHECK
                    String[] arrayUser = {user.getText().toString(), password.getText().toString()};
                    try {Thread.sleep(500);
                    } catch (InterruptedException e) {e.printStackTrace();}
                    socket.client.send(Client.objToBytes(arrayUser));
                    //CREDENTIAL FIELDS RESET
                    server.setText("");
                    user.setText("");
                    password.setText("");
                    //goes to login method to continue
                } else { //Si no s'ha pogut conectar (mostra un popup)
                    AlertDialog.Builder builder = new AlertDialog.Builder(Client.act);
                    builder.setTitle("Connection error");
                    builder.setMessage("Unable to connect to the server.\nMaybe server is not running?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {}
                    });
                    builder.show();
                }
            }
        });

        //REGISTER NEW ACCOUNT (NOT IMPLEMENTED)
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                RegisterActivity.socket = LoginActivity.socket;
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    public void login(boolean correctCredentials) {
        if (correctCredentials) {
            //REQUEST MODEL TO SERVER
                //socket.client.send("getModel");
            //SYNC CONNECTION WITH COMPONENT ACTIVITY
            ComponentesActivity.socket = LoginActivity.socket;
            //LAUNCH COMPONENT ACTIVITY
            Intent intent = new Intent(LoginActivity.this, ComponentesActivity.class);
            startActivity(intent);
        }
        else {
            //LAUNCH INVALID CREDENTIALS ERROR
            runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Client.act);
                    builder.setTitle("Credentials error");
                    builder.setMessage("The password or the Username entered is incorrect, please try again.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    builder.show();
                }
            });
        }
    }

}