package ch.ceff.viasa.controllers.calques.infos;

import ch.ceff.viasa.Viasa;
import ch.ceff.viasa.models.calques.infos.CalquePoint;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CalquePointInfoController implements Initializable{

    @FXML
    private StackPane root_StackPane;
    @FXML
    Label lbl_alt;
    @FXML
    Label lbl_lat;
    @FXML
    Label lbl_long;
    @FXML
    ImageView type_ImageView;

    public CalquePointInfoController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

    }

    public void updateData(CalquePoint calquePoint){
        // TODO afficher : L'ALTITUDE, LONGITUDE, LATITUDE DE CE POINT DANS LES DIFFERENTS LABELS

        type_ImageView.setImage(new Image(Viasa.class.getResource("images/point.png").toExternalForm()));
    }

    public StackPane getRoot_StackPane(){
        return root_StackPane;
    }
}
