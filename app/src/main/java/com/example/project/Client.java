package com.example.project;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Client {
    //VARIABLES
    //Connection
    int port = 8888;
    String location = "10.0.2.2";
    String uri = "ws://" + location + ":" + port;
    WebSocketClient client;
    //Credential validation
    HashMap<String, String> users;
    //In between classes comunication
    static Activity act;
    static ComponentesActivity actComponentes;

    public void connecta() {
        try {
            Log.i("WEB_CLIENT","Connecting");
            client = new WebSocketClient(new URI(uri), (Draft) new Draft_6455()) {

                @Override public void onMessage(ByteBuffer message) {
                    Object obj = bytesToObject(message);
                    String[] user = (String[]) obj;
                    for (String s : user) {
                        System.out.println(s);
                    }
                }

                @Override public void onMessage(String message) {
                    //SPLIT KEY-VALUE
                    System.out.println("Mensaje: " + message);
                    String key = message.split("/")[0];
                    String value = message.split("/")[1];

                    //USER CREDENTIALS CHECKING
                    if (key.contentEquals("passwordCheck")) {
                        if (value.contentEquals("true")) {
                            System.out.println("ENTRA EN TRUE");
                            ((LoginActivity) act).login(true);
                        } else {
                            System.out.println("ENTRA EN FALSE");
                            ((LoginActivity) act).login(false);
                        }
                    }

                    //MODEL SYNC
                    if (key.contentEquals("model")) {
                        Log.i("MODEL_STRING", value);
                        actComponentes.modelo = new Modelo(value);
                    }

                }

                @Override public void onOpen(ServerHandshake handshake){
                    System.out.println("Connected to: " + getURI());
                }

                @Override public void onClose ( int code, String reason,boolean remote){
                    if (act instanceof ComponentesActivity && !((ComponentesActivity)act).logout) {
                        ((ComponentesActivity) act).connectionClosedMessage();
                    }
                }

                @Override public void onError (Exception ex) {ex.printStackTrace();}

            };
            client.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + uri + " no és una direcció URI de WebSocket vàlida");
        }
    }

    public void desconecta() {client.close();}

    public static Object bytesToObject(ByteBuffer arr) {
        Object result = null;
        try {
            // Transforma el ByteButter en byte[]
            byte[] bytesArray = new byte[arr.remaining()];
            arr.get(bytesArray, 0, bytesArray.length);

            // Transforma l'array de bytes en objecte
            ByteArrayInputStream in = new ByteArrayInputStream(bytesArray);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();

        } catch (ClassNotFoundException e) {e.printStackTrace();
        } catch (UnsupportedEncodingException e) { e.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}
        return result;
    }

    public static byte[] objToBytes (Object obj) {
        byte[] result = null;
        try {
            // Transforma l'objecte a bytes[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            result = bos.toByteArray();
        } catch (IOException e) { e.printStackTrace(); }
        return result;
    }

}
