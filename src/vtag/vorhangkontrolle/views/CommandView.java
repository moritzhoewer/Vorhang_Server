/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import vtag.vorhangkontrolle.Controller;

/**
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public class CommandView {
	
	private Controller controller;
	private Stage stage;
	
	private Logger logger;
	
	public CommandView(Controller controller){
		this.controller = controller;
		logger = Logger.getLogger("VorhangKontrolle");
		try {
            init();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load CommandView", e);
            System.exit(-1);
        }
	}
	
	private void init() throws IOException {
        // Load FXML
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("CommandView.fxml"));
        fxmlLoader.setController(this);

        Parent root = fxmlLoader.load(); // might throw IOException

        stage = new Stage();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("VorhangKontrolle: Steuerung - (c) Moritz Höwer, 2017");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("VorhangKontrolle Logo.png")));
        stage.centerOnScreen();
        stage.setOnCloseRequest(ev -> handlePerfect(null));
    }
	
	public void show() {
        stage.show();
    }
	
	public void close() {
        stage.close();
    }
	
	@FXML
	private void handleOpen(ActionEvent e){
		controller.sendCommand("CMD_OPEN");
	}
	
	@FXML
	private void handleClose(ActionEvent e){
		controller.sendCommand("CMD_CLOSE");
	}
	
	@FXML
	private void handleStop(ActionEvent e){
		controller.sendCommand("CMD_STOP");
	}
	
	@FXML
	private void handleWait(ActionEvent e){
		controller.sendCommand("CMD_WAIT");
	}
	
	@FXML
	private void handlePerfect(ActionEvent e){
		controller.sendCommand("CMD_PERFECT");
		controller.commandDone();
		stage.close();
	}

}
