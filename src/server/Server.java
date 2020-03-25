package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import common.ClientMap;
import common.ClientObj;

/**
 * @author jack
 */
public class Server extends Thread{
	ServerSocket serverSocket;
	final static int DEFAULT_PORT = 8888;
	DataInputStream dis;
	DataOutputStream dos;
	Map<String, ClientObj> clientMap = ClientMap.getClientMap();
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(DEFAULT_PORT);
			System.out.println("服务端已打开");
			while (true) {
				Socket acceptsSocket = serverSocket.accept();

				dis = new DataInputStream(acceptsSocket.getInputStream());
				String id = dis.readUTF();
				ClientObj client = new ClientObj(acceptsSocket);
				System.out.println(id+"连接成功");
				clientMap.put(id, client);
				new ReceiveMessageFromClient(acceptsSocket,id).start();
			}
		} catch (Exception e) {
			
		}
	}
	public static void main(String[] args) {
		new Server().start();
	}
}
