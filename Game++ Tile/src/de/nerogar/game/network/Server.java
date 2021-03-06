package de.nerogar.game.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {

	private ArrayList<Client> clients = new ArrayList<Client>();
	private ServerSocket serverSocket;
	public int port;
	private boolean running = true;

	public Server(int port) {
		setName("ServerThread");
		this.port = port;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Faild to start Server Listener!");
		} finally {
			this.start();
		}
	}

	@Override
	public void run() {
		try {
			while (running) {
				Socket newClientSocket = serverSocket.accept();
				Client newClient = new Client(newClientSocket, this);
				clients.add(newClient);

				PacketConnectionInfo packetConnectionInfo = new PacketConnectionInfo();
				newClient.connectionInfoSent = true;
				newClient.sendPacket(packetConnectionInfo);

			}
		} catch (SocketException e) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getIP() {
		InetAddress current_addr = null;
		try {
			current_addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("Error fetching local IP");
			e.printStackTrace();
		}
		if (current_addr == null) return "unknown";
		return current_addr.getHostAddress();
	}

	public void stopAcceptingClients() {
		try {
			running = false;
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public void broadcastData(Packet packet) {
		if (!packet.packed) {
			packet.pack();
			packet.packInNetworkBuffer();
			packet.packed = true;
		}

		for (Client broadcastClient : clients) {

			broadcastClient.sendPacket(packet);
		}
	}

	public void stopServer() {
		synchronized (clients) {
			while (clients.size() > 0) {
				clients.get(0).stopClient();
			}
		}
		running = false;

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeClient(Client client) {
		synchronized (clients) {
			clients.remove(client);
		}
	}
}
