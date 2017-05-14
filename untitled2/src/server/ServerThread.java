package server;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static server.Direction.*;

public class ServerThread extends Thread{
    JTextArea log;
    public boolean f = true;

    int port = 3124;
    InetAddress ip = null;

    BData BD;
    DataInputStream dis;
    DataOutputStream dos;
    InputStream is;
    OutputStream os;

    ServerSocket ss;
    Hashtable<UUID, Ship> allClient =
            new Hashtable<UUID, Ship>();

    public boolean [][] pos = new boolean [60][60];

    public synchronized void addToLog(String s)
    {
        String str = log.getText();
        str+= s  + "\n";
        log.setText(str);
    }

    synchronized void sendHelm(UUID uuid) {
        Ship value = allClient.get(uuid);

        if ((value.getTX()-value.getX()>0)&&(!pos[value.getX()+1][value.getY()])) {
            value.setDirection(RIGHT);
            pos[value.getX()+1][value.getY()]=true;
            pos[value.getX()][value.getY()]=false;
        }
        else if ((value.getTX()-value.getX()<0)&&(!pos[value.getX()-1][value.getY()])) {
            value.setDirection(LEFT);
            pos[value.getX()-1][value.getY()]=true;
            pos[value.getX()][value.getY()]=false;
        }
        else if ((value.getTY()-value.getY()>0)&&(!pos[value.getX()][value.getY()+1])) {
            value.setDirection(UP);
            pos[value.getX()][value.getY()+1]=true;
            pos[value.getX()][value.getY()]=false;
        }
        else if ((value.getTY()-value.getY()<0)&&(!pos[value.getX()][value.getY()-1])) {
            value.setDirection(DOWN);
            pos[value.getX()][value.getY()-1]=true;
            pos[value.getX()][value.getY()]=false;
        }
    }

    synchronized void sendToAll(int num, UUID uuid)
    {
        if(num==1){
            boolean flag=false;
            Ship value = allClient.get(uuid);

            if(!value.isEnd()) {
                if ((value.getTX() - value.getX() > 0) && (!pos[value.getX() + 1][value.getY()])) {
                    value.setDirection(RIGHT);
                    pos[value.getX() + 1][value.getY()] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                } else if ((value.getTX() - value.getX() < 0) && (!pos[value.getX() - 1][value.getY()])) {
                    value.setDirection(LEFT);
                    pos[value.getX() - 1][value.getY()] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                } else if ((value.getTY() - value.getY() > 0) && (!pos[value.getX()][value.getY() + 1])) {
                    value.setDirection(UP);
                    pos[value.getX()][value.getY() + 1] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                } else if ((value.getTY() - value.getY() < 0) && (!pos[value.getX()][value.getY() - 1])) {
                    value.setDirection(DOWN);
                    pos[value.getX()][value.getY() - 1] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                }

                else if ((value.getTX() - value.getX() > 0) && (!pos[value.getX()][value.getY()+1])){
                    value.setDirection(UP);
                    pos[value.getX()][value.getY() + 1] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                } else if ((value.getTX() - value.getX() > 0) && (!pos[value.getX()][value.getY()-1])){
                    value.setDirection(DOWN);
                    pos[value.getX()][value.getY() - 1] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                }

                else if ((value.getTX() - value.getX() < 0) && (!pos[value.getX()][value.getY()+1])){
                    value.setDirection(UP);
                    pos[value.getX()][value.getY() + 1] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                } else if ((value.getTX() - value.getX() < 0) && (!pos[value.getX()][value.getY()-1])){
                    value.setDirection(DOWN);
                    pos[value.getX()][value.getY() - 1] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                }

                else if ((value.getTY() - value.getY() > 0) && (!pos[value.getX()+1][value.getY()])){
                    value.setDirection(RIGHT);
                    pos[value.getX()+1][value.getY()] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                } else if ((value.getTY() - value.getY() > 0) && (!pos[value.getX()-1][value.getY()])){
                    value.setDirection(LEFT);
                    pos[value.getX()-1][value.getY()] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                }
                else if ((value.getTY() - value.getY() < 0) && (!pos[value.getX()+1][value.getY()])){
                    value.setDirection(RIGHT);
                    pos[value.getX()+1][value.getY()] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                } else if ((value.getTY() - value.getY() < 0) && (!pos[value.getX()-1][value.getY()])){
                    value.setDirection(LEFT);
                    pos[value.getX()-1][value.getY()] = true;
                    pos[value.getX()][value.getY()] = false;
                    flag = true;
                }
            }
            int count=0;
            for (Map.Entry<UUID, Ship> entrySet :
                    allClient.entrySet()) {
                if (entrySet.getKey()==uuid) {
                    value.number = count;
                    break;
                }
                count++;
            }
            value.sendData(null,num, flag);
        }
        else {
            String data="all ";
            data+=String.valueOf(allClient.size());
            for (Map.Entry<UUID, Ship> entrySet :
                    allClient.entrySet()) {
                Ship value = entrySet.getValue();
                data += " "+String.valueOf(value.getX()) + " " + String.valueOf(value.getY()) + " " + String.valueOf(value.getTX()) + " " + String.valueOf(value.getTY());
            }
            for (Map.Entry<UUID, Ship> entrySet :
                    allClient.entrySet()) {
                Ship value = entrySet.getValue();
                value.sendData(data, num, true);
            }
            //addToLog(data+"\n");
        }
    }

    synchronized void stopServer()
    {
        f = false;

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
                ss.close();
            } catch (IOException ignored) {}
    }

    ServerThread(JTextArea log)
    {
        BD = new BData();
        this.log = log;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ss = new ServerSocket(port, 0, ip);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i=0; i<60; i++)for (int j=0; j<60; j++) pos[i][j]=false;
        for (int i=1; i<60; i+=4)
            for (int j=1; j<60; j+=2) {
                pos[i][j]=true;
            }
        for (int i=3; i<60; i+=4)
            for (int j=2; j<60; j+=2) {
                pos[i][j]=true;
            }
        addToLog("Server start\n");
    }

    @Override
    public void run() {
        int i=0;
        Sender sender = new Sender(this);
        sender.start();
        f=true;
        while(f)
        {
            try {
                Socket cs = ss.accept();

                os = cs.getOutputStream();
                is = cs.getInputStream();

                dis = new DataInputStream(is);
                dos = new DataOutputStream(os);

                String userName = null, pass = null ,shipName = null;

                String user = dis.readUTF();
                StringTokenizer stok = new StringTokenizer(user, " ");
                String type = stok.nextToken();
                String answer = "false";

                if(type.equals("checkRegistration")){
                    userName=stok.nextToken();
                    pass = stok.nextToken();
                    shipName = stok.nextToken();
                    answer = "false";
                    if (BD.checkUser(userName)) {
                        BD.Insert(userName, pass, shipName);
                        answer = "true";
                    }
                    dos.writeUTF(answer);
                    dos.flush();
                }

                if(type.equals("checkUser")){
                    userName=stok.nextToken();
                    pass = stok.nextToken();
                    shipName = stok.nextToken();
                    answer = "false";
                    if (BD.checkLogIn(userName, pass)) {
                        BD.setShipName(userName, shipName);
                        answer = "true";
                    }
                    dos.writeUTF(answer);
                    dos.flush();
                }

                if (answer.equals("true")) type = dis.readUTF();

                if(type.equals("connect")) {
                    Ship ct = new Ship(this, cs, shipName);
                    addToLog("Ship '" + ct.shipName + "' connected\n");
                    allClient.put(ct.getUUID(), ct);
                    ct.start();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void removeClient(UUID uuid) {
        Ship value = allClient.get(uuid);
        pos[value.getX()][value.getY()]=false;
        allClient.remove(uuid);
    }
}

