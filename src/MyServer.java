import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haosun on 5/4/18.
 */
public class MyServer {
    final static int PORT = 2020;
    final static String UPDATE_USERS="update_users_list:";
    final static String LOGOUT_MESSAGE="@@logout_me@@:";

    ArrayList<Socket> clientSocketList = new ArrayList<>();
    ArrayList<String> clientNameList = new ArrayList<>();
    ServerSocket serverSocket;

    public MyServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server started " + serverSocket);
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ServerListeningThread(socket, clientSocketList, clientNameList));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyServer();
    }
}
