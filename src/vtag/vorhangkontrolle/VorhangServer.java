/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public class VorhangServer extends Application {
	
	public VorhangServer() {
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Logger logger = Logger.getLogger("VorhangKontrolle");
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.ALL);
		logger.addHandler(ch);
		
		Controller c = new Controller();
		c.start();		
	}

}
