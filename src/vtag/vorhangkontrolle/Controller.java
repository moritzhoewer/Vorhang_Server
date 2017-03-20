/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle;

import java.util.logging.Logger;

import javafx.application.Platform;
import vtag.vorhangkontrolle.networking.MessageHandler;
import vtag.vorhangkontrolle.networking.ServerListener;
import vtag.vorhangkontrolle.networking.TCPServer;
import vtag.vorhangkontrolle.networking.UDPDiscoveryServer;
import vtag.vorhangkontrolle.views.CommandView;
import vtag.vorhangkontrolle.views.DialogBase;
import vtag.vorhangkontrolle.views.HandleRequestDialog;
import vtag.vorhangkontrolle.views.SendingRequestDialog;
import vtag.vorhangkontrolle.views.StandbyView;

/**
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public class Controller implements MessageHandler, ServerListener {

	private enum State {
		DISCONNECTED, STANDBY, WAITING_FOR_APP, WAITING_FOR_USER, COMMAND
	}

	private State state;

	private UDPDiscoveryServer udpServer;
	private TCPServer tcpServer;

	private StandbyView standbyView;
	private CommandView commandView;
	private HandleRequestDialog handleRequestDialog;
	private DialogBase sendingRequestDialog;

	private Logger logger;

	public Controller() {
		state = State.DISCONNECTED;
		udpServer = new UDPDiscoveryServer();
		tcpServer = new TCPServer(this, this);
		standbyView = new StandbyView(this);
		commandView = new CommandView(this);
		handleRequestDialog = new HandleRequestDialog(this);
		sendingRequestDialog = new SendingRequestDialog(this);

		logger = Logger.getLogger("VorhangKontrolle");
	}

	public void start() {
		udpServer.start();
		tcpServer.start();
		standbyView.showAndWait();
	}

	public void stop() {
		udpServer.stop();
		tcpServer.stop();
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void handleMessage(String message) {
		logger.fine("Received: " + message);
		switch (state) {
		case STANDBY:
			if (message.equals("REQUEST")) {
				state = State.WAITING_FOR_USER;
				Platform.runLater(handleRequestDialog::show);
			}
			break;
		case WAITING_FOR_USER:
			if (message.equals("CANCEL")) {
				state = State.STANDBY;
				Platform.runLater(handleRequestDialog::close);
			}
			break;
		case COMMAND:
			if (message.equals("CANCEL")) {
				state = State.STANDBY;
				Platform.runLater(commandView::close);
			}
			break;
		case WAITING_FOR_APP:
			if (message.equals("ACCEPT")) {
				Platform.runLater(sendingRequestDialog::close);
				showCommand();
			} else if (message.equals("DENY")) {
				state = State.STANDBY;
				Platform.runLater(sendingRequestDialog::close);
			}
			break;
		}

	}

	@Override
	public void handleClientConnected() {
		Platform.runLater(standbyView::clientConnected);
		state = State.STANDBY;

	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void handleClientDisconnected() {
		Platform.runLater(standbyView::clientDisconnected);
		switch(state){
		case COMMAND:
			Platform.runLater(commandView::close);
			break;
		case WAITING_FOR_APP:
			Platform.runLater(sendingRequestDialog::close);
			break;
		case WAITING_FOR_USER:
			Platform.runLater(handleRequestDialog::close);
			break;
		}
		state = State.DISCONNECTED;
	}

	public void sendRequest() {
		if (state == State.STANDBY) {
			tcpServer.sendMessage("REQUEST");
			state = State.WAITING_FOR_APP;
			sendingRequestDialog.show();
		}
	}
	
	public void cancelRequest() {
		if (state == State.WAITING_FOR_APP) {
			tcpServer.sendMessage("CANCEL");
			state = State.STANDBY;
		}		
	}

	public void acceptRequest() {
		if (state == State.WAITING_FOR_USER) {
			tcpServer.sendMessage("ACCEPT");
			showCommand();
		}		
	}

	public void denyRequest() {
		if (state == State.WAITING_FOR_USER) {
			tcpServer.sendMessage("DENY");
			state = State.STANDBY;
		}
		
	}
	
	private void showCommand(){
		state = State.COMMAND;
		Platform.runLater(commandView::show);
	}

	public void sendCommand(String cmd) {
		if (state == State.COMMAND) {
			tcpServer.sendMessage(cmd);
		}		
	}
	
	public void commandDone(){
		if (state == State.COMMAND) {
			state = State.STANDBY;
		}
	}
}
