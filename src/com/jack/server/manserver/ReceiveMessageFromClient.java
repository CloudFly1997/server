package com.jack.server.manserver;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.jack.server.common.ClientMap;
import com.jack.server.common.ClientObj;
import com.jack.server.util.DbUtil;
import com.jack.server.common.MessageHandle;
import com.jack.transfer.FileMessage;
import com.jack.transfer.Message;


/**
 * @author jack
 */
public class ReceiveMessageFromClient extends Thread {


    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Map<String, ClientObj> clientMap = ClientMap.getClientMap();
    private String id;
    private Socket socket;

    public ReceiveMessageFromClient(Socket socket, ObjectInputStream objectInputStream,
                                    ObjectOutputStream objectOutputStream, String id) {
        this.ois = objectInputStream;
        this.oos = objectOutputStream;
        this.socket = socket;
        this.id = id;
    }

    public void push(Object object) throws IOException {
        if (object instanceof Message) {
            Message message = (Message) object;
            System.out.println(message);
            List<String> users = MessageHandle.getNeedToPushUsers(message.getToUser());
            System.out.println(users);
            for (String user : users) {
                boolean isRead = false;
                if (clientMap.containsKey(user)) {
                    ClientObj client = clientMap.get(user);
                    client.getOos().writeObject(message);
                    isRead = true;
                }
                if ("[TXT]".equals(message.getType())) {
                    writeTXTToDB(message, isRead);
                }
            }
        }
        if (object instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage)object;
            System.out.println(fileMessage);
            new Thread(new ReceiveFileFromClient(id,socket)).start();
        }
    }


    public void writeTXTToDB(Message message, boolean isRead) {
        String insertSql = "INSERT INTO message(message_type,from_user,to_user,message_date,content,is_read) values(?,?,?,?,?,?)";
        Connection conn = DbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertSql);
            preparedStatement.setString(1, message.getType());
            preparedStatement.setString(2, message.getFromUser());
            preparedStatement.setString(3, message.getToUser());
            preparedStatement.setString(4, message.getDateTime());
            preparedStatement.setString(5, message.getMessageContent());
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
                Object receive = ois.readObject();
                push(receive);
            } catch (IOException | ClassNotFoundException e) {
                if (clientMap.remove(id, clientMap.get(id))) {
                    System.out.println(id + "断开连接");
                }
                System.out.println(clientMap.entrySet());
                isThisThreadIsAlive = false;
            }
        }
    }

}
