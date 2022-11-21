package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JTable;

//ClientHandler class
class ClientHandler extends Thread {

    final DataInputStream inputStream;
    final DataOutputStream outputStream;
    final Socket s;
    private String username;
    private String currentUser;
    private Server server;

    // Constructor
    public ClientHandler(Server server, Socket s, DataInputStream inputStream, DataOutputStream outputStream) {
        this.server = server;
        this.s = s;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        currentUser = "";
    }

    @Override
    public void run() {
        String received;
        try {
            while (true) {
                try {

                    // receive the answer from client
                    received = inputStream.readUTF();

                    handleReceiveFromClient(received);

                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
            this.server.sendUserOfflineToAll(username);
            //close resource
            s.close();
            this.inputStream.close();
            this.outputStream.close();
            if (username != null) {
                this.server.removeClient(username);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleReceiveFromClient(String received) {
        char c = received.charAt(0);
        switch (c) {
            case 'F':
                handleFindUser(received.substring(2));
                break;
            case 'M':
                handleSendMessageFromUser(received.substring(2));
                break;
            case 'E':
                handleSendFileFromUser(received.substring(2), received.substring(2));
                break;
            case 'L':
                handleLoginFromUser(received.substring(2));
                break;
            case 'R':
                handleRegisterFromUser(received.substring(2));
                break;
            case 'O':
                handleLogOutFromUser();
                break;
            default:
                break;
        }

    }

    private void handleLogOutFromUser() {
        this.server.sendUserOfflineToAll(username);
        this.username = null;
        currentUser = "";
    }

    private void handleRegisterFromUser(String data) {
        String username = data.substring(0, data.indexOf('/'));
        String password = data.substring(data.indexOf('/') + 1);

        try {
            if (this.server.checkExistUsername(username)) {
                outputStream.writeUTF("S:FAILED Username đã được đăng ký!");
                return;
            }
            if (this.server.handleSignup(username, password)) {
                outputStream.writeUTF("S:" + username);
                this.username = username;
            } else {
                outputStream.writeUTF("S:FAILED");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleLoginFromUser(String data) {
        String username = data.substring(0, data.indexOf('/'));
        String password = data.substring(data.indexOf('/') + 1);

        try {
            if (this.server.verifyLogin(username, password)) {
                outputStream.writeUTF("L:" + username);
                this.username = username;
                this.server.sendUserOnlineToAll(username);
                this.server.sendUserOnlineForNewLogin(this);
                System.out.println("A client has logged with username: " + username);
            } else {
                outputStream.writeUTF("L:FAILED");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleSendMessageFromUser(String message) {
        this.server.send(username, currentUser, message);
    }

    public void handleSendFileFromUser(String fileid, String fileName) {
        this.server.sendFile(username, currentUser, fileid, fileName);
    }

    public void handleFindUser(String user) {
        try {

            if (this.server.checkExistUsername(user) && !user.equals("all")) {
                outputStream.writeUTF("R:okBạn đang chat với @" + user);
                currentUser = user;
                ArrayList<String> listFile = this.server.getAllFile(username, currentUser);

                ArrayList<String> list = this.server.getAllMessage(username, currentUser);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        outputStream.writeUTF(list.get(i));
                    }
                }

                if (listFile != null) {
                    listFile.add(0, "I:@Reset");
                    for (int i = 0; i < listFile.size(); i++) {
                        outputStream.writeUTF(listFile.get(i));
                    }
                }

            } else if (user.equals("all")) {
                handleChatAll();
            } else {
                outputStream.writeUTF("R:null");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleChatAll() {
        try {
            outputStream.writeUTF("R:okBạn đang chat với @All");
            currentUser = "all";

            ArrayList<String> listFile = this.server.getAllFile(username);

            ArrayList<String> list = this.server.getAllMessage(username);

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    outputStream.writeUTF(list.get(i));
                }
            }
            if (listFile != null) {
                listFile.add(0, "I:@Reset");
                for (int i = 0; i < listFile.size(); i++) {
                    outputStream.writeUTF(listFile.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void sendMessageToAllClient(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendFileToAllClient(String file) {
        try {
            outputStream.writeUTF(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleSendMessageToCurrentUser(String message) {
        try {
            outputStream.writeUTF("M:@" + currentUser + ": " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleSendFileToCurrentUser(String fileid, String fileName) {
        try {
            outputStream.writeUTF("I:@" + currentUser + ": " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendUserOnline(String username) {
        try {
            outputStream.writeUTF("C:" + username);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendUserOffline(String username) {
        try {
            outputStream.writeUTF("D:" + username);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
