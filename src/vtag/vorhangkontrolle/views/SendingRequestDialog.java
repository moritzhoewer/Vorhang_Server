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
public class SendingRequestDialog extends DialogBase {
	public SendingRequestDialog(Controller controller){
		super(controller);
	}
	
	@Override
	protected void init() throws IOException {
        // Load FXML
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("SendingRequestDialog.fxml"));
        fxmlLoader.setController(this);

        Parent root = fxmlLoader.load(); // might throw IOException

        Scene scene = new Scene(root, 300, 180);
        stage.setScene(scene);
    }
	
	@FXML
	private void cancelRequest(ActionEvent e){
		controller.cancelRequest();
		stage.close();
	}

}
