package client.controllers;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import client.views.ClientConfigView;
import client.views.ClientView;

public class ConnectThread extends Thread {

    private Socket s;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private JTextPane chatBoxPane;
    private JTable fileTable;
    private ClientView clientView;
    private ClientConfigView clientConfigView;

    public ConnectThread(ClientConfigView clientConfigView, Socket s, DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.s = s;

        this.clientConfigView = clientConfigView;
    }

    public void run() {
        try {
            while (true) {
                try {

                    DataInputStream input = new DataInputStream(s.getInputStream());
                    String receive = input.readUTF();
                    handleReceiveData(receive);
                } catch (Exception ev) {
                    try {
                        Thread.sleep(2000);
                        System.exit(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
        this.chatBoxPane = clientView.getChatBoxPane();
        this.fileTable = clientView.getFileTable();
    }

    private void handleReceiveData(String receive) {
        char c = receive.charAt(0);
        System.out.println("---------->" + c);

        switch (c) {
            case 'R':			//Xu ly ket qua tim kiem username
                handleResultFindUser(receive.substring(2));
                break;
            case 'M':
                handleMessageFromAnotherUser(receive.substring(2));
                break;
            case 'A':			//Hien thi tin nhan cua chinh minh lay tu CSDL
                handleShowMessageBySelft(receive.substring(2));
                break;
            case 'E':
                handleShowFileBySelft(receive.substring(2), receive.substring(2));
                break;
            case 'I':			//Hien thi file
                System.out.println(receive.substring(2));
                handleFileFromAnotherUser(receive.substring(2), receive.substring(2));
                break;
            case 'L':			//Xu ly ket qua Login
                handleReceiveLoginResult(receive.substring(2));
                break;
            case 'S':			//Xu ly ket qua dang ky
                handleReceiveSignupResult(receive.substring(2));
                break;
            case 'C':			//Xu ly ket qua dang ky
                handleUserConnect(receive.substring(2));
                break;
            case 'D':			//Xu ly ket qua dang ky
                handleUserDisconnect(receive.substring(2));
                break;
            default:
                break;
        }

    }

    private void handleUserDisconnect(String username) {
        clientView.removeUserOnlineList(username);
        System.out.println("Nhan off" + username);
    }

    private void handleUserConnect(String username) {
        clientView.addUserOnlineList(username);
        System.out.println("Nhan online" + username);

    }

    private void handleReceiveSignupResult(String res) {
        if (!res.contains("FAILED")) {
            clientConfigView.showClientView(res);
            clientConfigView.disposeSignUpView();
        } else {
            if (res.length() > 6) {
                clientConfigView.getSignUpView().showError(res.substring(6));
            }
        }

    }

    private void handleReceiveLoginResult(String res) {
        if (!res.equals("FAILED")) {
            clientConfigView.showClientView(res);
        } else {
            clientConfigView.getLoginView().showError("Username hoặc password không đúng!");
        }

    }

    private void handleShowMessageBySelft(String message) {
        this.clientView.setBodyMess1(message);
        chatBoxPane.selectAll();
        chatBoxPane.setSelectedTextColor(null);
    }

    private void handleShowFileBySelft(String fileid, String fileName) {
        this.clientView.setFileTableBody(fileid, fileName);
    }

    private void handleFileFromAnotherUser(String fileid, String fileName) {
        this.clientView.setFileTableBody(fileid, fileName);
    }

    private void handleResultFindUser(String result) {
        if (result.contains("ok")) {
            chatBoxPane.setText("");
            this.clientView.showWelcomeMess(result.substring(2));
            this.clientView.showBtnSend();
        } else {
            this.clientView.showErrorFindUser();
        }
    }

    private void handleMessageFromAnotherUser(String message) {
        this.clientView.showMessReceivedCurrentUser(message);
        chatBoxPane.selectAll();
        chatBoxPane.setSelectedTextColor(null);

    }

}
