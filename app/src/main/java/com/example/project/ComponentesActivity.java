package com.example.project;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ComponentesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_componentes);
        Client socket = new Client(ComponentesActivity.this);

        TextView btn = findViewById(R.id.btnLogout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.desconecta();
                startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));

            }
        });
    }
    public void comprobarConexion(){
        startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
    }
}