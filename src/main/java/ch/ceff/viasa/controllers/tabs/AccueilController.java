package ch.ceff.viasa.controllers.tabs;

import ch.ceff.viasa.controllers.ViasaController;
import ch.ceff.viasa.models.stages.CreateProjetStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Initializable{

    @FXML
    private StackPane root_StackPane;

    @FXML
    private Button createNewOrienteering_Button, importOrienteering_Button;

    private ViasaController viasaController;

    public AccueilController(ViasaController viasaController){
        this.viasaController = viasaController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

        initCreateNewOrienteeringButton();
        initImportOrienteeringButton();

    }

    private void initCreateNewOrienteeringButton(){

        // TODO Créer le design du fichier fxml de ce controller (Bienvenue sur le concepter, ici vous pouvez créer des parcours d'orientation, ... )

        createNewOrienteering_Button.setOnMouseClicked(event -> {
            final CreateProjetStage createProjetStage = new CreateProjetStage((Stage) root_StackPane.getScene().getWindow(), viasaController);
            createProjetStage.show();
        });

    }

    /**
     * cette méthode va charger un parcour déjà existant qui se trouve sous forme de fichier
     */
    private void initImportOrienteeringButton(){

    }

    // ---------------- SETTERS ---------------- \\


}
