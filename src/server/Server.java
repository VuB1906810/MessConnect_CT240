package server;

//Java implementation of Server side
//It contains two classes : Server and ClientHandler
//Save file as Server.java
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.Connection;

import client.views.ClientView;
import models.MessageModel;
import models.FileModel;
import models.UserModel;
import server.database.MySQLDB;

import java.net.*;

//Server class
public class Server {

    private ArrayList<ClientHandler> clientThreads;
    private Connection conn;
    private UserModel userModel;
    private MessageModel messageModel;
    private FileModel fileModel;

    public Server() {
        try {

            clientThreads = new ArrayList<ClientHandler>();

            conn = MySQLDB.getConnection();

            userModel = new UserModel(conn);
            messageModel = new MessageModel(conn);
            fileModel = new FileModel(conn);

            System.out.println("Server is running");

            ServerSocket ss;

            ss = new ServerSocket(7244);

            // client request
            while (true) {
                Socket s = null;

                try {
                    s = ss.accept();

                    System.out.println("A new client is connected!");

                    // obtaining input and out streams
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    System.out.println("Assigning new thread for this client");

                    // create a new thread object
                    ClientHandler t = new ClientHandler(this, s, dis, dos);
                    t.start();
                    clientThreads.add(t);
                } catch (Exception e) {
                    s.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();

    }

    public void showAllUser() {
        System.out.print("[");
        for (int i = 0; i < clientThreads.size(); i++) {
            if (clientThreads.get(i).getUsername() != null) {
                System.out.print(clientThreads.get(i).getUsername() + ", ");
            }
        }
        System.out.println("]");
    }

    public ClientHandler getClient(String username) {
        for (int i = 0; i < clientThreads.size(); i++) {
            if (clientThreads.get(i).getUsername().equals(username)) {
                return clientThreads.get(i);
            }
        }
        return null;
    }

    public void removeClient(String username) {
        clientThreads.remove(getClient(username));
        showAllUser();
    }

    public void send(String userSend, String userReceive, String message) {

        messageModel.insertMessage(userSend, userReceive, message);

        if (userReceive.equals("all")) {
            sendMessageAll(userSend, message);
            return;
        }

        ClientHandler clientReceive = getClient(userReceive);

        if (clientReceive == null) {
            return;
        }

        if (clientReceive.getCurrentUser().equals(userSend)) {
            clientReceive.handleSendMessageToCurrentUser(message);
        }

    }

    public void sendFile(String userSend, String userReceive, String fileid, String fileName) {
        fileModel.insertFile(userSend, userReceive, fileid.substring(8).split(",")[0], fileid.split(",")[1].substring(10));
        if (userReceive.equals("all")) {
            sendFileAll(userSend, fileid, fileName);
            return;
        }

        ClientHandler clientReceive = getClient(userReceive);

        if (clientReceive == null) {
            return;
        }

        if (clientReceive.getCurrentUser().equals(userSend)) {
            clientReceive.handleSendFileToCurrentUser(fileid, fileName);
        }

    }

    private void sendMessageAll(String userSend, String message) {

        for (int i = 0; i < clientThreads.size(); i++) {
            if (clientThreads.get(i).getCurrentUser().equals("all") && !clientThreads.get(i).getUsername().equals(userSend)) {
                clientThreads.get(i).sendMessageToAllClient("M:@" + userSend + ": " + message);
            }
        }

    }

    private void sendFileAll(String userSend, String fileid, String fileName) {

        for (int i = 0; i < clientThreads.size(); i++) {
            if (clientThreads.get(i).getCurrentUser().equals("all") && !clientThreads.get(i).getUsername().equals(userSend)) {
                clientThreads.get(i).sendFileToAllClient("I:@" + userSend + ": " + fileName);
            }
        }

    }

    public ArrayList<String> getAllMessage(String username, String currentUser) {
        return messageModel.getAllMessage(username, currentUser);

    }

    public ArrayList<String> getAllMessage(String currentUser) {
        return messageModel.getAllMessage(currentUser);
    }

    public ArrayList<String> getAllFile(String username, String currentUser) {
        return fileModel.getAllFile(username, currentUser);

    }

    public ArrayList<String> getAllFile(String currentUser) {
        return fileModel.getAllFile(currentUser);
    }

    public boolean verifyLogin(String username, String password) {
        return userModel.verifyLogin(username, password);
    }

    public boolean checkExistUsername(String username) {
        return userModel.checkExistUsername(username);
    }

    public boolean handleSignup(String username, String password) {
        return userModel.handleSignup(username, password);
    }

    public void sendUserOnlineToAll(String username) {
        for (int i = 0; i < clientThreads.size(); i++) {
            if (clientThreads.get(i).getUsername() != null && !clientThreads.get(i).getUsername().equals(username)) {
                clientThreads.get(i).sendUserOnline(username);
            }
        }

    }

    public void sendUserOfflineToAll(String username) {
        for (int i = 0; i < clientThreads.size(); i++) {
            if (clientThreads.get(i).getUsername() != null) {
                clientThreads.get(i).sendUserOffline(username);
            }
        }

    }

    public void sendUserOnlineForNewLogin(ClientHandler client) {

        for (int i = 0; i < clientThreads.size(); i++) {
            if (clientThreads.get(i).getUsername() != null && !clientThreads.get(i).equals(client)) {
                client.sendUserOnline(clientThreads.get(i).getUsername());
            }
        }

    }

}
