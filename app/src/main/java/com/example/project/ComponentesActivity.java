package com.example.project;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ComponentesActivity extends AppCompatActivity {
    static Client socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_componentes);
        Client.act = ComponentesActivity.this;

        TextView btn = findViewById(R.id.btnLogout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desconectar();
            }
        });
    }
    public void comprobarConexion(){
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Client.act);
                builder.setTitle("Connection error");
                builder.setMessage("There has been an error with the Server connection, please try again later, we are working on it.\nThank you!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
                    }
                });
                builder.show();
            }
        });
    }

    public void desconectar(){
        socket.desconecta();
        startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
    }
}