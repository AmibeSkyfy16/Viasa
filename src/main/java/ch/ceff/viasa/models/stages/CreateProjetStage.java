package ch.ceff.viasa.models.stages;

import ch.ceff.viasa.Viasa;
import ch.ceff.viasa.controllers.ViasaController;
import ch.ceff.viasa.controllers.stages.CreateProjetStageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CreateProjetStage extends Stage{

    public CreateProjetStage(Stage primaryStage, ViasaController viasaController){
        super(StageStyle.DECORATED);
        try{
            final FXMLLoader fxmlLoader = new FXMLLoader(Viasa.class.getResource("fxml/stages/createProjetStage.fxml"));
            final CreateProjetStageController createProjetStageController = new CreateProjetStageController(viasaController, this);
            fxmlLoader.setController(createProjetStageController);
            final Parent root = fxmlLoader.load();

            this.setScene(new Scene(root));

            this.initModality(Modality.APPLICATION_MODAL);
            this.initOwner(primaryStage);
            this.setX((primaryStage.widthProperty().getValue() - 300) / 2 + (primaryStage.getX() / 2));
            this.setY((primaryStage.heightProperty().getValue() - 200) / 2 + (primaryStage.getY() / 2));
        }catch(IOException e){
            e.printStackTrace();
        }

    }


}
