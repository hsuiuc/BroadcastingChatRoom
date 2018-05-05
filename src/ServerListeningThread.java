import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by haosun on 5/4/18.
 */
public class ServerListeningThread implements Runnable {
    Socket clientSocket;
    ArrayList<Socket> clientSocketList;
    ArrayList<String> clientNameList;
    String userName;

    public ServerListeningThread(Socket clientSocket, ArrayList<Socket> clientSocketList, ArrayList<String> clientNameList) {
        this.clientSocket = clientSocket;
        this.clientSocketList = clientSocketList;
        this.clientNameList = clientNameList;

        try {
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            userName = dataInputStream.readUTF();
            clientSocketList.add(clientSocket);
            clientNameList.add(userName);
            broadcastMessage("****** " + userName +" Logged in at "+(new Date())+" ******");
            broadcastClientNameList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastClientNameList() {
        broadcastMessage(MyServer.UPDATE_USERS + clientNameList);
    }

    private void broadcastMessage(String message) {
        for (Socket clientSocket : clientSocketList) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            while (true) {
                String s1 = dataInputStream.readUTF();
                if (s1.toLowerCase().equals(MyServer.LOGOUT_MESSAGE))
                    break;
                broadcastMessage(userName + " said: " + " : " + s1);
            }

            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataOutputStream.writeUTF(MyServer.LOGOUT_MESSAGE);
            dataOutputStream.flush();
            clientNameList.remove(userName);
            broadcastMessage("****** "+userName+" Logged out at "+(new Date())+" ******");
            broadcastClientNameList();
            clientSocketList.remove(clientSocket);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
