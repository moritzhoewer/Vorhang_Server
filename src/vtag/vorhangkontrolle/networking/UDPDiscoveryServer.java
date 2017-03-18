/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server for network discovery. Based on
 * http://michieldemey.be/blog/network-discovery-using-udp-broadcast/
 * 
 * @author Moritz Höwer
 * @version 1.0 - 11.03.2017
 */
public class UDPDiscoveryServer {

	private static final int RECEIVE_BUFFER_SIZE = 4096;
	private static final int PORT = 9001;
	private DatagramSocket udpSocket;
	private Thread workerThread;
	private boolean running;
	private Logger logger;

	public UDPDiscoveryServer() {
		running = false;
		logger = Logger.getLogger("VorhangKontrolle");
	}

	public void start() {
		if (running) {
			throw new IllegalStateException("Server already runnning!");
		}
		try {
			udpSocket = new DatagramSocket(PORT);
			udpSocket.setBroadcast(true);
			workerThread = new Thread(this::serverLoop);
			workerThread.setDaemon(true);
			workerThread.setName("DiscoveryServer - Worker thread");
			workerThread.start();
			running = true;
			logger.info("DiscoveryServer started");
		} catch (SocketException e) {
			throw new RuntimeException("Unable to Start server!", e);
		}
	}

	public void stop() {
		if (!running) {
			return;
		}
		running = false;
		logger.fine("DiscoveryServer stopping");
		udpSocket.close();
		try {
			workerThread.join();
		} catch (InterruptedException e) {
		}
		workerThread = null;
		logger.info("DiscoveryServer stopped");

	}

	private void serverLoop() {
		byte[] buffer = new byte[RECEIVE_BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, RECEIVE_BUFFER_SIZE);
		while (running) {
			try {
				udpSocket.receive(packet);

				logger.fine(String.format("Reveived request from %s:%d\n", packet.getAddress().getHostAddress(),
						packet.getPort()));

				String message = new String(packet.getData()).trim();
				if (message.equals("VORHANGKONTROLLE_REQUEST")) {
					byte[] responseBuffer = "VORHANGKONTROLLE_RESPONSE".getBytes();
					DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length,
							packet.getSocketAddress());
					udpSocket.send(response);
					logger.fine(String.format("Sent response to %s\n", packet.getAddress().getHostAddress()));
				}
			} catch (IOException e) {
				if (running) {
					logger.log(Level.WARNING, "Exception in DiscoveryServer worker thread", e);
				}
			}
		}
	}

}
