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
    int port = 8888;
    String location = "10.0.2.2";
    String uri = "ws://" + location + ":" + port;
    WebSocketClient client;
    HashMap<String, String> users;
    File file;
    static Activity act;


    public void connecta() {
        try {
            System.out.println("connecting");
            client = new WebSocketClient(new URI(uri), (Draft) new Draft_6455()) {

                @Override
                public void onMessage(ByteBuffer message) {
                    Object obj = bytesToObject(message);
                    String[] user = (String[]) obj;
                    for (String s : user) {
                        System.out.println(s);
                    }
                }

                public void onMessage(String message) {
                    System.out.println("Mensaje: "+message);
                    if ((message.equalsIgnoreCase("true") || message.equalsIgnoreCase("false")) && act instanceof LoginActivity) {
                        ((LoginActivity) act).login(Boolean.parseBoolean(message));
                    } else if (message.contains("block")){
                        Log.i("RECIEVED", message);
                    }else{
                        ((ComponentesActivity) act).comprobarConexion();
                    }
                }

                @Override public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to: " + getURI());
                }

                @Override public void onClose(int code, String reason, boolean remote) {
                    if (act instanceof ComponentesActivity) {
                        ((ComponentesActivity) act).comprobarConexion();
                    }
                }

                @Override public void onError(Exception ex) {
                    ex.printStackTrace();
                }

            };
            client.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + uri + " no és una direcció URI de WebSocket vàlida");
        }
    }

    public void desconecta() {
        client.close();
    }

    public File getModel() {
        client.send("getModel");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return file;
    }

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

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
