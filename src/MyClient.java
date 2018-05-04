import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by haosun on 5/3/18.
 */
public class MyClient {
    class MyGUI implements ActionListener {
        private JButton sendButton, logoutButton,loginButton, exitButton;
        private JFrame chatWindow;
        private JTextArea txtBroadcast;
        private JTextArea txtMessage;
        private JList usersList;

        public void display() {
            chatWindow=new JFrame();
            txtBroadcast=new JTextArea(5,30);
            txtBroadcast.setEditable(false);
            txtMessage=new JTextArea(2,20);
            usersList=new JList();

            sendButton=new JButton("Send");
            logoutButton=new JButton("Log out");
            loginButton=new JButton("Log in");
            exitButton=new JButton("Exit");

            JPanel center1=new JPanel();
            center1.setLayout(new BorderLayout());
            center1.add(new JLabel("Broad Cast messages from all online users",JLabel.CENTER),"North");
            center1.add(new JScrollPane(txtBroadcast),"Center");

            JPanel south1=new JPanel();
            south1.setLayout(new FlowLayout());
            south1.add(new JScrollPane(txtMessage));
            south1.add(sendButton);

            JPanel south2=new JPanel();
            south2.setLayout(new FlowLayout());
            south2.add(loginButton);
            south2.add(logoutButton);
            south2.add(exitButton);

            JPanel south=new JPanel();
            south.setLayout(new GridLayout(2,1));
            south.add(south1);
            south.add(south2);

            JPanel east=new JPanel();
            east.setLayout(new BorderLayout());
            east.add(new JLabel("Online Users",JLabel.CENTER),"East");
            east.add(new JScrollPane(usersList),"South");

            chatWindow.add(east,"East");
            chatWindow.add(center1,"Center");
            chatWindow.add(south,"South");

            chatWindow.pack();
            chatWindow.setTitle("Login for Chat");
            chatWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            chatWindow.setVisible(true);

            sendButton.addActionListener(this);
            logoutButton.addActionListener(this);
            loginButton.addActionListener(this);
            exitButton.addActionListener(this);
            logoutButton.setEnabled(false);
            loginButton.setEnabled(true);

            txtMessage.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent fe) {
                    txtMessage.selectAll();
                }
            });

            chatWindow.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent ev) {
                    if(clientSocket!=null) {
                        JOptionPane.showMessageDialog(chatWindow,"u r logged out right now. ","Exit",JOptionPane.INFORMATION_MESSAGE);
                        //todo
                        //logoutSession();
                    }
                    System.exit(0);
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton eventButton = (JButton) e.getSource();

            if (eventButton == sendButton) {
                if (clientSocket == null) {
                    JOptionPane.showMessageDialog(chatWindow, "please log in first");
                    return;
                } else {
                    try {
                        clientOutputStream.writeUTF(txtMessage.getText());
                        txtMessage.setText("");
                    } catch (IOException e1) {
                        txtBroadcast.append("send button clicked " + e1);
                    }
                }
            }
            else if (eventButton == loginButton) {
                String userName = JOptionPane.showInputDialog(chatWindow, "enter your nick name");
                if (userName != null) {
                    // todo
                    txtBroadcast.append(userName + " has logged in");
                }
            }
            else if (eventButton == logoutButton) {
                logoutSession();
            }
            else if (eventButton == exitButton) {
                JOptionPane.showMessageDialog(chatWindow,"u r logged out right now. ","Exit",JOptionPane.INFORMATION_MESSAGE);
                logoutSession();
                System.exit(0);
            }
        }
    }

    private Socket clientSocket;
    private DataOutputStream clientOutputStream;
    private MyGUI myGUI;

    private void logoutSession() {
        if (clientSocket == null) {
            //connection not established, do nothing
            return;
        } else {
            try {
                clientOutputStream.writeUTF("log out");
                Thread.sleep(500);
            } catch (IOException | InterruptedException e) {
                myGUI.txtBroadcast.append("\n inside logout" + e);
            }

            myGUI.logoutButton.setEnabled(false);
            myGUI.loginButton.setEnabled(true);
            myGUI.chatWindow.setTitle("login to chat");
        }
    }

    public MyClient() {
        myGUI = new MyGUI();
        myGUI.display();
    }

    public static void main(String[] args) {
        new MyClient();
    }
}
