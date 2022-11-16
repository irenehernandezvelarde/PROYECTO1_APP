package com.example.project;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Client {
    int port = 8888;
    String location = "24.01.17";
    HashMap<String, String> users;
    WebSocketClient client;
    File file;
    String uri = "ws://" + location + ":" + port;
    public void conectando() {
        try {
            System.out.println("connecting");
            client = new WebSocketClient(new URI(uri), (Draft) new Draft_6455()) {
                @Override
                public void onMessage(ByteBuffer message) {
                    Object obj = bytesToObject(message);
                    if (obj.getClass().getSimpleName().equalsIgnoreCase("HashMap")) {
                        users = (HashMap<String, String>) obj;
                    } else if (obj.getClass().getSimpleName().equalsIgnoreCase("File")) {
                        file = (File) obj;
                    }
                }
                public void onMessage(String message) {
                }
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to: " + getURI());
                }
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from: " + getURI());
                }
                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + uri + " no és una direcció URI de WebSocket vàlida");
        }
    }
    public File getXml() {
        client.send("getModel");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return file;
    }
    public HashMap<String, String> getUsers() {
        client.send("getUsers");
        while (users == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return users;
    }
    public static Object bytesToObject(ByteBuffer arr) {
        Object result = null;
        try {
            byte[] bytesArray = new byte[arr.remaining()];
            arr.get(bytesArray, 0, bytesArray.length);
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
}
