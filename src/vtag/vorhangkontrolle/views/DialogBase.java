package vtag.vorhangkontrolle.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vtag.vorhangkontrolle.Controller;

public abstract class DialogBase {

	protected Controller controller;
	protected Stage stage;
	protected Logger logger;

	public DialogBase(Controller controller) {
		this.controller = controller;
		
		logger = Logger.getLogger("VorhangKontrolle");
		try {
			preInit();
            init();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load FXML for a Dialog", e);
            System.exit(-1);
        }
	}
	
	protected abstract void init() throws IOException;
	
	private void preInit(){
		stage = new Stage();
		stage.setTitle("VorhangKontrolle: Steuerung - (c) Moritz Höwer, 2017");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("VorhangKontrolle Logo.png")));
        stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
	}

	public void show() {
	    stage.show();
	}

	public void close() {
		stage.close();
	}

}