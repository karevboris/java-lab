package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by пк1 on 25.04.2017.
 */
public class Sender extends Thread {
    private ServerThread st;

    public Sender(ServerThread st) {
        this.st = st;
    }

    @Override
    public void run() {
        while (st.isAlive())
            if (st.allClient.size() > 1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                }
                st.sendToAll(2, null);
            }
    }
}
