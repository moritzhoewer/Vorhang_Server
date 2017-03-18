/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public class VorhangServer {
	
	public VorhangServer() {
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logger logger = Logger.getLogger("VorhangKontrolle");
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.ALL);
		logger.addHandler(ch);
	}

}
