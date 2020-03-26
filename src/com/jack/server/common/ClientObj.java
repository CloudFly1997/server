package com.jack.server.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientObj {
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public Socket getSocket() {
		return socket;
	}

	public ClientObj(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
}
