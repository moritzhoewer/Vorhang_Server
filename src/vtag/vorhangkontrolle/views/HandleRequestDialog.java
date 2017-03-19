/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle.views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import vtag.vorhangkontrolle.Controller;

/**
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public class HandleRequestDialog extends DialogBase {

	public HandleRequestDialog(Controller controller) {
		super(controller);
	}

	@Override
	protected void init() throws IOException {
		// Load FXML
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HandleRequestDialog.fxml"));
		fxmlLoader.setController(this);

		Parent root = fxmlLoader.load(); // might throw IOException

		Scene scene = new Scene(root, 300, 180);
		stage.setScene(scene);
	}

	@FXML
	private void acceptRequest(ActionEvent e) {
		controller.acceptRequest();
		stage.close();
	}

	@FXML
	private void denyRequest(ActionEvent e) {
		controller.denyRequest();
		stage.close();
	}

}
