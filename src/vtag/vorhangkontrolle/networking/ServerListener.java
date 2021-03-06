/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle.networking;

/**
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public interface ServerListener {

	void handleClientConnected();
	
	void handleClientDisconnected();
	
}
