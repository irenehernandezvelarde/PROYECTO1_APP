package com.example.project;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void desconectar(){
        socket.desconecta();
        startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
    }
}