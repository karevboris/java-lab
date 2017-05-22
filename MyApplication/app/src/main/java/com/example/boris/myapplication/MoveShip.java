package com.example.boris.myapplication;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * Created by Boris on 17.05.2017.
 */

class MoveShip extends AsyncTask<Void, Void, Void> {
    SceneView v;
    Client cs;
    int[] Coord;
    int numClients=0;
    public MoveShip(SceneView _v){
        v = _v;
        cs = new Client();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        v.invalidate();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            cs.start();
            while (true)
            {
                try {
                    String data = cs.dis.readUTF();

                    StringTokenizer stok = new StringTokenizer(data, " ");
                    String type = stok.nextToken();

                    if (type.equals("all")) {
                        numClients = Integer.parseInt(stok.nextToken());
                        Coord = new int[4 * numClients];
                        if (numClients > 0) {
                            for (int j = 0; j < numClients; j++) {
                                int i = 0;
                                while ((stok.hasMoreTokens()) && (i < 4)) {
                                    Coord[j * 4 + i] = Integer.parseInt(stok.nextToken());
                                    i++;
                                }
                            }
                        }
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                publishProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}