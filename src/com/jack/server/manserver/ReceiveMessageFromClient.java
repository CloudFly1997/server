package com.jack.server.manserver;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            if ("[GROUP]".equals(message.getType())) {
                users = getGroupMembers(message.getToUser());
                users.remove(message.getFromUser());
            }
            System.out.println(users);
            for (String user : users) {
                boolean isRead = false;
                if (clientMap.containsKey(user)) {
                    ClientObj client = clientMap.get(user);
                    client.getOos().writeObject(message);
                    isRead = true;
                }
                writeTXTToDB(message, isRead);
            }
        }

    }


    public void writeTXTToDB(Message message, boolean isRead) {
        String insertSql = "INSERT INTO message(message_type,from_user,to_user,message_date,content,is_read) values(?,?,?,?,?,?)";
        Connection conn = DbUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(insertSql);
            preparedStatement.setString(1, message.getType());
            preparedStatement.setString(2, message.getFromUser());
            preparedStatement.setString(3, message.getToUser());
            preparedStatement.setString(4, message.getDateTime());
            preparedStatement.setString(5, message.getMessageContent());
            preparedStatement.setBoolean(6, isRead);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, null, preparedStatement);
        }
    }

    public List<String> getGroupMembers(String groupAccount) {
        List<String> members = new ArrayList<>();
        String sql = "SELECT user_id FROM chat.`group_member` WHERE group_id = ?";
        Connection conn = DbUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,groupAccount);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                members.add(resultSet.getString("user_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn,resultSet,preparedStatement);
        }
        return members;
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
