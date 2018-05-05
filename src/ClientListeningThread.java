import javax.swing.text.BadLocationException;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by haosun on 5/4/18.
 */
public class ClientListeningThread implements Runnable {
    private DataInputStream dataInputStream;
    private MyClient myClient;

    public ClientListeningThread(DataInputStream dataInputStream, MyClient myClient) {
        this.dataInputStream = dataInputStream;
        this.myClient = myClient;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String s = dataInputStream.readUTF();
                if (s.startsWith(MyServer.UPDATE_USERS)) {
                    updateUsersList(s);
                }
                else if (s.equals(MyServer.LOGOUT_MESSAGE)) {
                    break;
                }
                else {
                    myClient.myGUI.txtBroadcast.append("\n" + s);
                }
                int lineOffset = myClient.myGUI.txtBroadcast.getLineStartOffset(myClient.myGUI.txtBroadcast.getLineCount()-1);
                myClient.myGUI.txtBroadcast.setCaretPosition(lineOffset);
            } catch (IOException | BadLocationException e) {
                myClient.myGUI.txtBroadcast.append("\n client listening thread" + e);
            }
        }
    }

    private void updateUsersList(String s) {
        s = s.replace(MyServer.UPDATE_USERS, "").replace("[", "").replace("]", "");

        Vector<String> tmp = new Vector<>();
        String[] users = s.split(",");
        Collections.addAll(tmp, users);

        myClient.myGUI.usersList.setListData(tmp);
    }
}
