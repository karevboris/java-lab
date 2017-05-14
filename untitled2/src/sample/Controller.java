package sample;

import javafx.fxml.FXML;

import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import server.Ship;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static server.Ship.numClients;

public class Controller {

    int port = 3124;
    InetAddress ip = null;
    Socket cs;
    InputStream is;
    OutputStream os;
    DataInputStream dis;
    DataOutputStream dos;

    Color[] color;
    boolean move = true;
    boolean flag = false;
    boolean isUserLog = true;
    int numCients = 0;
    int number=0;
    int[] Coord;

    @FXML
    private javafx.scene.canvas.Canvas canvas;
    @FXML
    private Button button;
    @FXML
    private Button cnct;
    @FXML
    private Pane pane;
    @FXML
    private Rectangle rec;
    @FXML
    private Rectangle target;
    @FXML
    private TextField crdX;
    @FXML
    private TextField crdY;
    @FXML
    private TextField crdTX;
    @FXML
    private TextField crdTY;
    @FXML
    private javafx.scene.control.Label jLabel1;

    @FXML
    private TextField login;
    @FXML
    private TextField pass;
    @FXML
    private TextField nameShip;
    @FXML
    private Button reg;
    @FXML
    private Button log;

    @FXML
    private void disconnect() {
        flag=true;
    }

    @FXML
    private void registration() {
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs = new Socket(ip, port);
            System.out.append("Check registration: ");

            os = cs.getOutputStream();
            is = cs.getInputStream();

            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);

            String user, answer;
            user = "checkRegistration " + String.valueOf(login.getText()) + " " + String.valueOf(pass.getText()) + " " + String.valueOf(nameShip.getText());
            try {
                dos.writeUTF(user);
                dos.flush();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                answer=dis.readUTF();
                if (answer.equals("true")){
                    cnct.setDisable(false);
                    System.out.append("Registretion success\n");
                    reg.setDisable(true);
                    log.setDisable(true);

                    login.setDisable(true);
                    pass.setDisable(true);
                    nameShip.setDisable(true);
                }
                else {
                    cnct.setDisable(true);
                    System.out.append("This user name already in use\n");
                    cs.close();
                }
                //cs.close();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void logIN() {
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs = new Socket(ip, port);
            System.out.append("Check LogIn: ");

            os = cs.getOutputStream();
            is = cs.getInputStream();

            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);

            String user, answer;
            user = "checkUser " + String.valueOf(login.getText()) + " " + String.valueOf(pass.getText()) + " " + String.valueOf(nameShip.getText());
            try {
                dos.writeUTF(user);
                dos.flush();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                answer=dis.readUTF();
                if (answer.equals("true")){
                    cnct.setDisable(false);
                    System.out.append("LogIn success\n");
                    log.setDisable(true);
                    reg.setDisable(true);

                    login.setDisable(true);
                    pass.setDisable(true);
                    nameShip.setDisable(true);
                }
                else {
                    cnct.setDisable(true);
                    System.out.append("Uncorrected user name or password\n");
                    cs.close();
                }
                //cs.close();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void checkTextUserName() {
        if ((!login.getText().isEmpty()) && (!login.getText().contains(" ")))
            if ((!pass.getText().isEmpty()) && (!pass.getText().contains(" ")))
                if ((!nameShip.getText().isEmpty()) && (!nameShip.getText().contains(" "))) {
                    reg.setDisable(false);
                    log.setDisable(false);
                }
                else {
                    reg.setDisable(true);
                    log.setDisable(true);
                }
            else {
                reg.setDisable(true);
                log.setDisable(true);
            }
        else {
            reg.setDisable(true);
            log.setDisable(true);
        }
    }

    @FXML
    private void connect() {
        if (isUserLog) {
            //try {
                /*ip = InetAddress.getLocalHost();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                cs = new Socket(ip, port);
                System.out.append("Client start \n");

                os = cs.getOutputStream();
                is = cs.getInputStream();

                dis = new DataInputStream(is);
                dos = new DataOutputStream(os);*/

                try {
                    dos.writeUTF("connect");
                    dos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }

                String init;
                init = "coords " + String.valueOf(crdX.getText()) + " " + String.valueOf(crdY.getText()) + " " + String.valueOf(crdTX.getText()) + " " + String.valueOf(crdTY.getText());
                try {
                    dos.writeUTF(init);
                    dos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                color = new Color[10];
                color[0] = Color.RED;
                color[1] = Color.GREEN;
                color[2] = Color.YELLOW;
                color[3] = Color.ORANGE;

                GraphicsContext context = canvas.getGraphicsContext2D();

                new Thread() {
                    @Override
                    public void run() {
                        flag = false;
                        boolean stop = false;
                        while (!stop) {
                            try {
                                String data = dis.readUTF();
                                context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                                for (int i = 1; i < 60; i += 4)
                                    for (int j = 1; j < 60; j += 2) {
                                        context.setFill(Color.BLACK);
                                        context.fillRect(10 * i, 10 * j, 10, 10);
                                    }
                                for (int i = 3; i < 60; i += 4)
                                    for (int j = 2; j < 60; j += 2) {
                                        context.setFill(Color.BLACK);
                                        context.fillRect(10 * i, 10 * j, 10, 10);
                                    }
                                for (int j = 0; j < numCients && numCients > 1; j++) {
                                    if (j != number) {
                                        context.setFill(color[j]);
                                        context.fillRect(10 * Coord[j * 4], 10 * Coord[j * 4 + 1], 10, 10);
                                        context.setStroke(color[j]);
                                        context.strokeRect(10 * Coord[j * 4 + 2], 10 * Coord[j * 4 + 3], 10, 10);
                                    }
                                }

                                StringTokenizer stok = new StringTokenizer(data, " ");
                                String type = stok.nextToken();
                                if (type.equals("stopServer")) {
                                    stop = true;
                                    cs.close();
                                    System.out.append("Server stop \n");
                                    cnct.setDisable(true);
                                    log.setDisable(false);
                                    reg.setDisable(false);
                                    login.setDisable(false);
                                    pass.setDisable(false);
                                    nameShip.setDisable(false);
                                    break;
                                }
                                if (type.equals("stop")) {
                                    stop = true;
                                    cs.close();
                                    cnct.setDisable(true);
                                    log.setDisable(false);
                                    reg.setDisable(false);
                                    login.setDisable(false);
                                    pass.setDisable(false);
                                    nameShip.setDisable(false);
                                    System.out.append("The goal is achieved. Client stop \n");
                                    break;
                                }
                                if (type.equals("$")) {
                                    int i = 0;
                                    int[] coord = new int[4];
                                    int col = Integer.parseInt(stok.nextToken());
                                    number = col;
                                    while (stok.hasMoreTokens()) {
                                        coord[i] = Integer.parseInt(stok.nextToken());
                                        i++;
                                    }
                                    context.setFill(color[col]);
                                    context.fillRect(10 * coord[0], 10 * coord[1], 10, 10);
                                    context.setStroke(color[col]);
                                    context.strokeRect(10 * coord[2], 10 * coord[3], 10, 10);
                                }
                                if (type.equals("all")) {
                                    numCients = Integer.parseInt(stok.nextToken());
                                    Coord = new int[4 * numCients];
                                    if (numCients > 1) {
                                        for (int j = 0; j < numCients; j++) {
                                            int i = 0;
                                            while ((stok.hasMoreTokens()) && (i < 4)) {
                                                Coord[j * 4 + i] = Integer.parseInt(stok.nextToken());
                                                i++;
                                            }
                                            context.setFill(color[j]);
                                            context.fillRect(10 * Coord[j * 4], 10 * Coord[j * 4 + 1], 10, 10);
                                            context.setStroke(color[j]);
                                            context.strokeRect(10 * Coord[j * 4 + 2], 10 * Coord[j * 4 + 3], 10, 10);
                                        }
                                    }
                                }
                                if (flag) {
                                    stop = true;
                                    dos.writeUTF("stop ");
                                    dos.flush();
                                    System.out.append("Client stop \n");
                                    cnct.setDisable(true);
                                    reg.setDisable(false);
                                    log.setDisable(false);

                                    login.setDisable(false);
                                    pass.setDisable(false);
                                    nameShip.setDisable(false);
                                } else {
                                    dos.writeUTF("ok ");
                                    dos.flush();
                                }
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }.start();
            /*} catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }
}