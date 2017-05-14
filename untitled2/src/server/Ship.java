package server;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ship extends Thread {

    ServerThread st;
    Socket cs;
    UUID id = UUID.randomUUID();
    DataInputStream dis;
    DataOutputStream dos;
    InputStream is;
    OutputStream os;

    int num;
    public int number=0;
    private int posX=0;
    private int posY=0;
    private int targetX=60;
    private int targetY=60;
    boolean move = true;
    boolean goal = false;
    public boolean stop = false;
    private Direction direction;
    public static int numClients=0;
    public String shipName;

    public UUID getUUID() {
        return id;
    }

    public Ship(ServerThread st, Socket cs, String shipName) {
        this.st = st;
        this.cs = cs;
        this.shipName = shipName;
        numClients++;
        try {
            is = cs.getInputStream();
            os = cs.getOutputStream();

            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);

            //start();
        } catch (IOException ex) {
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isEnd() {
        return ((posX==targetX)&&(posY==targetY));
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setX(int x) {
        this.posX = x;
    }

    public void setY(int y) {
        this.posY = y;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public void setTX(int x) {
        this.targetX = x;
    }

    public void setTY(int y) {
        this.targetY = y;
    }

    public int getTX() {
        return targetX;
    }

    public int getTY() {
        return targetY;
    }

    private String move() {
        String data;
        if (move) {
            setX(getX() + direction.deltaX());
            setY(getY() + direction.deltaY());
        }
        data="$ "+String.valueOf(number)+" "+String.valueOf(posX)+" "+String.valueOf(posY)+" "+String.valueOf(targetX)+" "+String.valueOf(targetY);
        return data;
    }

    @Override
    public void run() {
        stop = false;
        while (!stop) {
            {
                try {
                    String str = dis.readUTF();
                    StringTokenizer stok = new StringTokenizer(str, " ");
                    String type = stok.nextToken();
                    if (type.equals("stop")){
                        stop = true;
                        cs.close();
                        //st.pos[posX][posY]=false;
                        st.removeClient(id);
                    }
                    if (type.equals("coords")) {
                        posX = Integer.parseInt(stok.nextToken());
                        posY = Integer.parseInt(stok.nextToken());
                        targetX = Integer.parseInt(stok.nextToken());
                        targetY = Integer.parseInt(stok.nextToken());
                        if (isEnd()) {
                            //goal=true;
                            //st.pos[getX()][getY()]=false;
                            stop=true;
                        }
                        if( st.f) st.sendToAll(1, id);
                        else {
                            stop=true;
                            dos.writeUTF("stopServer");
                            st.removeClient(id);
                        }
                    }
                    if (type.equals("ok")){
                        if(isEnd()) {
                            //goal=true;
                            //st.pos[getX()][getY()]=false;
                            stop=true;
                        }
                        if( st.f) st.sendToAll(1, id);
                        else {
                            stop=true;
                            dos.writeUTF("stopServer");
                            st.removeClient(id);
                        }
                    }
                }catch (IOException ex) {
                    Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void stopClient()
    {
        try {
            cs.close();
            st.removeClient(id);
            numClients--;
            st.addToLog("Client " + String.valueOf(num)+" stoped");
        } catch (IOException ex){
            Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendData(String data, int num, boolean flag)
    {
        if(dos!= null) {
            new Thread() {
                @Override
                public void run() {
                    if (num==1)try {
                        move=flag;
                        if ((!stop)&&(st.f)) dos.writeUTF(move());
                        else if (stop){
                            dos.writeUTF("stop");
                            st.removeClient(id);
                        }
                        else{
                            dos.writeUTF("stopServer");
                        }
                        dos.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
                    }else
                    try {
                        //st.addToLog(data+"\n");
                        dos.writeUTF(data);
                        dos.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
        }
    }
}
