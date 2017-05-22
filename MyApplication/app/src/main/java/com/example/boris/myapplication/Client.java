package com.example.boris.myapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Boris on 17.05.2017.
 */

public class Client {
    public DataInputStream dis;
    public DataOutputStream dos;


    public void start() {
        try {
            int port = 3124;
            String address = "192.55.233.2";
            InetAddress ipAddress = InetAddress.getByName(address);

            Socket socket = new Socket(ipAddress, port);

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            dis= new DataInputStream(is);
            dos = new DataOutputStream(os);

            dos.writeUTF("android");
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
