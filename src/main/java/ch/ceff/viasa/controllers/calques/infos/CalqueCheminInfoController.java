package ch.ceff.viasa.controllers.calques.infos;

import ch.ceff.viasa.Viasa;
import ch.ceff.viasa.models.calques.infos.CalqueChemin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CalqueCheminInfoController implements Initializable{

    @FXML
    private StackPane root_StackPane;

    @FXML
    private ImageView type_ImageView;

    @FXML
    private Label name_Label;

    @FXML
    private Label distanceTotalData_Label;

    @FXML
    private Label nbPointData_Label;

    public CalqueCheminInfoController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

    }

    public void updateData(CalqueChemin calqueChemin){

        // TODO FAIRE CECI MEILLEUR (DESIGN DE MERDE)

        type_ImageView.setImage(new Image(Viasa.class.getResource("images/chemin.png").toExternalForm()));
        name_Label.setText(calqueChemin.getName());

        final float distanceTotal = calqueChemin.getDistanceTotal();
        distanceTotalData_Label.setText(distanceTotal + " KM");

        nbPointData_Label.setText(String.valueOf(calqueChemin.getPoints().size()));

    }


    // ---------------- GETTERS ---------------- \\


    public StackPane getRoot_StackPane(){
        return root_StackPane;
    }

}
