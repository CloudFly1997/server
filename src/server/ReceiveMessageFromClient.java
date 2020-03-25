package server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import common.ClientMap;
import common.ClientObj;
import common.DbUtil;
import common.MessageHandle;
import pojo.Message;


/**
 * @author jack
 */
public class ReceiveMessageFromClient extends Thread {

    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;
    private Map<String, ClientObj> clientMap;
    private String id;
    private Socket socket;

    public ReceiveMessageFromClient(Socket socket, String id) {
        try {
            this.id = id;
            this.socket = socket;
            dis = new DataInputStream(this.socket.getInputStream());
            ois = new ObjectInputStream(this.socket.getInputStream());
            clientMap = ClientMap.getClientMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void push(String receiveMessageFromClient) {
        List<String> users = MessageHandle.getNeedToPushUsers(receiveMessageFromClient);
        System.out.println(users);
        try {
            for (String user : users) {
                if (clientMap.containsKey(user)) {
                    ClientObj client = clientMap.get(user);
                    Socket socketToUser = client.getSocket();
                    dos = new DataOutputStream(socketToUser.getOutputStream());
                    dos.writeUTF(receiveMessageFromClient);
                    if (MessageHandle.getMessageType(receiveMessageFromClient).equals("[TXT]")) {
                        writeToDB(receiveMessageFromClient, true);
                    }
                } else {
                    if (MessageHandle.getMessageType(receiveMessageFromClient).equals("[TXT]")) {
                        writeToDB(receiveMessageFromClient, false);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToDB(String message, boolean isRead) {
        String insertSql = "INSERT INTO message(message_type,from_user,to_user,message_date,content,is_read) values(?,?,?,?,?,?)";
        Connection conn = DbUtil.getConnection();
        Message messageObj = MessageHandle.getMessageObject(message);
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSql);
            preparedStatement.setString(1, messageObj.getType());
            preparedStatement.setString(2, messageObj.getFromUser());
            preparedStatement.setString(3, messageObj.getToUser());
            preparedStatement.setString(4, messageObj.getDateTime());
            preparedStatement.setString(5, messageObj.getMessageContent());
            preparedStatement.setBoolean(6, isRead);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean isThisThreadIsAlive = true;
        while (isThisThreadIsAlive) {
            try {
                String receiveMessageFromClient = dis.readUTF();
                System.out.println(receiveMessageFromClient);
                push(receiveMessageFromClient);
            } catch (IOException e) {
                isThisThreadIsAlive = false;
                try {
                    System.out.println(clientMap);
                    clientMap.get(id).getDis().close();
                    clientMap.get(id).getDis().close();
                    clientMap.get(id).getSocket().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (clientMap.remove(id, clientMap.get(id))) {
                    System.out.println(id + "断开连接");
                }
                System.out.println(clientMap.entrySet());
            }
        }
    }

}
