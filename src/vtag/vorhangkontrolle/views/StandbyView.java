/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle.views;

import vtag.vorhangkontrolle.Controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public class StandbyView {
	
	private Controller controller;
	private Stage stage;
	
	@FXML
	private Button btnFohReady;
	
	@FXML
	private Label lblAppStatus;
	
	private Logger logger;
	
	public StandbyView(Controller controller){
		this.controller = controller;
		logger = Logger.getLogger("VorhangKontrolle");
		try {
            init();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load StandbyView", e);
            System.exit(-1);
        }
	}
	
	private void init() throws IOException {
        // Load FXML
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("StandbyView.fxml"));
        fxmlLoader.setController(this);

        Parent root = fxmlLoader.load(); // might throw IOException

        stage = new Stage();
        Scene scene = new Scene(root, 250, 135);
        stage.setTitle("VorhangKontrolle: Steuerung - (c) Moritz Höwer, 2017");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("VorhangKontrolle Logo.png")));
        stage.centerOnScreen();
        stage.setOnCloseRequest(ev -> controller.stop());
    }
	
	public void clientConnected(){
		lblAppStatus.setText("Verbunden");
		lblAppStatus.setTextFill(Color.GREEN);
		btnFohReady.setDisable(false);
	}
	
	public void clientDisconnected(){
		lblAppStatus.setText("Getrennt");
		lblAppStatus.setTextFill(Color.RED);
		btnFohReady.setDisable(true);		
	}
	
	public void showAndWait() {
        stage.showAndWait();
    }
	
	@FXML
	private void fohReady(ActionEvent e){
		controller.sendRequest();
	}

}
