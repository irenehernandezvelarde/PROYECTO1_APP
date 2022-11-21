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
        //si no logro que funcione descomentar esta linea de abajo:
        //startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
        Log.i("AWEG","HOLA");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(true);
        builder1.setTitle("My Dialogo");
        builder1.setMessage("Lanzar Activity");
        builder1.setInverseBackgroundForced(true);
        builder1.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("AWEGFWEF","HOLAOTRAVEZ");
                        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

     /**  PRUEBA 3
     ----------------------------------------------------------------------------------
        AlertDialog.Builder ValidationDialog = new AlertDialog.Builder(ComponentesActivity.this);
        ValidationDialog.setTitle("Validation");
        ValidationDialog.setMessage("Voulez-vous vraiment valider").setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(ComponentesActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog Dialog = ValidationDialog.create();
        Dialog.show();


         PRUEBA 2
         ----------------------------------------------------------------------------------
         AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("title");
            alert.setMessage("massage");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //Do something here where "ok" clicked and then perform intent from activity context
                    Intent intent = new Intent(ComponentesActivity.this, LoginActivity.class);
                    ComponentesActivity.this.startActivity(intent);
                }
            });
            alert.show();

         PRUEBA 1
         ----------------------------------------------------------------------------------
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("screen2");
        builder.setMessage("go to screen2?");

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(ComponentesActivity.this, LoginActivity.class);
                startActivity(intent);


            }
        });**/
    }

    public void desconectar(){
        socket.desconecta();
        startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
    }
}