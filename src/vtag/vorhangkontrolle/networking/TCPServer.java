/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server for TCP Connection to one Client.
 * 
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public class TCPServer {
	/**
	 * Internal states of the server
	 */
	private enum State {
		STARTING, STOPPED, WAITING_FOR_CLIENT, CONECTED, RESETTING
	}

	/**
	 * Port for the Server
	 */
	private final static int PORT = 8698;

	/**
	 * the ServerSocket for this server
	 */
	private ServerSocket serverSocket;

	/**
	 * the Socket which holds the connection to the Robot
	 */
	private Socket clientSocket;

	/**
	 * the Thread for the background readLoop
	 */
	private Thread readThread;

	/**
	 * flag for readLoop
	 */
	private boolean stop;

	private PrintWriter out;

	private BufferedReader in;

	private MessageHandler messageHandler;

	private ServerListener serverListener;

	/**
	 * the current internal state of the server
	 */
	private State state;
	// TODO: control access to prevent race conditions

	private Logger logger;

	public TCPServer(MessageHandler messageHandler, ServerListener serverListener) {
		this.messageHandler = messageHandler;
		this.serverListener = serverListener;
		stop = true;
		state = State.STOPPED;
		logger = Logger.getLogger("VorhangKontrolle");
	}

	public void start() {
		if (state != State.STOPPED) {
			// don't start twice
			return;
		}
		state = State.STARTING;
		try {
			serverSocket = new ServerSocket(PORT);
			readThread = new Thread(this::serverLoop);
			readThread.setDaemon(true);
			readThread.setName("Server readLoop");

			stop = false;
			readThread.start();
			logger.info("TCPServer started");

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to create TCPServer", e);
		}

	}

	public void stop() {
		switch (state) {
		case WAITING_FOR_CLIENT:
			stop = true;
			closeServerSocket();
			joinReadLoop();
			break;
		case CONECTED:
			stop = true;
			closeClientSocket();
			joinReadLoop();
			closeServerSocket();
			break;
		case RESETTING:
			stop = true;
			joinReadLoop();
			closeServerSocket();
		case STARTING: // TODO: potential race condition on state
			stop = true;
			break;
		case STOPPED:
			return;
		}
		logger.info("TCPServer stopped.");
		state = State.STOPPED;

	}

	/**
	 * closes the clientSocket
	 */
	private void closeClientSocket() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Problem disconnecting Client", e);
		}
	}

	/**
	 * waits for the readLoop to terminate
	 */
	private void joinReadLoop() {
		try {
			readThread.join();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Problem shutting down readLoop", e);
		}

		readThread = null;
	}

	/**
	 * closes the ServerSocket
	 */
	private void closeServerSocket() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Problem closing Server", e);
		}
		serverSocket = null;
	}

	/**
	 * background loop which handles most of the actual server work.
	 */
	private void serverLoop() {
		while (!stop) {
			try {
				state = State.WAITING_FOR_CLIENT;
				clientSocket = serverSocket.accept();
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream());

				state = State.CONECTED;
				logger.fine("Client connected!");
				serverListener.handleClientConnected();
				
				// start readLoop blocking this thread
				readLoop();

				state = State.RESETTING;
				logger.fine("Client disconnected!");
				serverListener.handleClientDisconnected();
				in.close();
				out.close();
				clientSocket.close();

			} catch (IOException e) {
				if (!stop) {
					logger.log(Level.SEVERE, "Exception in Server loop", e);
				}
			} finally{
				in = null;
				out = null;
				clientSocket = null;
			}
		}

	}

	private void readLoop() throws IOException {
		String msg;
		while (!stop && state == State.CONECTED) {
			msg = in.readLine();
			if(msg == null){
				break;
			}
			logger.fine("Received: " + msg);
			messageHandler.handleMessage(msg);
		}
	}
	
	public void sendMessage(String message){
		logger.fine("State: " + state + " - Sending: " + message);
		if(state != State.CONECTED){
			throw new IllegalStateException("No client connected!");
		}
		out.println(message);
		out.flush();
	}
}
