package ch.ceff.viasa.controllers.stages;

import ch.ceff.viasa.Viasa;
import ch.ceff.viasa.controllers.ViasaController;
import ch.ceff.viasa.controllers.creations.ClassicCreationTabController;
import ch.ceff.viasa.models.creations.ClassicCreation;
import com.esri.arcgisruntime.mapping.Basemap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateProjetStageController implements Initializable{

    @FXML
    private AnchorPane anchPane_mainPane;
    @FXML
    private Button btn_apply;
    @FXML
    private Button btn_cancel;
    @FXML
    private TextField txt_runName;
    @FXML
    private CheckBox check_forcedOrder;
    @FXML
    private CheckBox check_freeOrder;
    @FXML
    private CheckBox check_streetview;
    @FXML
    private CheckBox check_blankSatellite;
    @FXML
    private CheckBox check_namedSatellite;

    private Stage createProjetStage;
    private ViasaController viasaController;

    public CreateProjetStageController(ViasaController viasaController, Stage createProjetStage){
        this.viasaController = viasaController;
        this.createProjetStage = createProjetStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

        //Sélectionner 1 des 2 options d'odre de course
        RunTypeCheck(check_forcedOrder, check_freeOrder);
        RunTypeCheck(check_freeOrder, check_forcedOrder);

        //Sélectionner 1 des 3 options de vue de la carte pour la course
        ViewTypeCheck(check_streetview, check_blankSatellite, check_namedSatellite);
        ViewTypeCheck(check_blankSatellite, check_streetview, check_namedSatellite);
        ViewTypeCheck(check_namedSatellite, check_streetview, check_blankSatellite);

        //Fonction des boutons
        BtnCancelParameters(btn_cancel);
        BtnApplyParameters(btn_apply);

        // TESTING
        /*
        btn_finished.setOnAction(event -> {
            try{
                final FXMLLoader fxmlLoader = new FXMLLoader(Viasa.class.getResource("fxml/creations/classicCreationTab.fxml"));
                final ClassicCreationTabController classicCreationTabController = new ClassicCreationTabController(viasaController);
                fxmlLoader.setController(classicCreationTabController);
                final Parent root = fxmlLoader.load(); // LORS D'UN LOAD : SA VA APPELLE LA METHODE INITIALIZE DU CONTROLLEUR (ICI classicCreationTabController)
                final Tab tab = new Tab();
                tab.setText("NomDeLaCourse");
                tab.setContent(root);
                classicCreationTabController.setTab(tab);
                viasaController.getCreationTabControllers().add(classicCreationTabController);
                viasaController.getCreator_TabPane().getTabs().add(tab);

            }catch(IOException e){
                e.printStackTrace();
            }

            createProjetStage.close();
        });
        */


    }

    private void RunTypeCheck(CheckBox checked, CheckBox unchecked){
        checked.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
//                    checked.setSelected(true);
                unchecked.setSelected(false);
            }
        });
    }

    private void ViewTypeCheck(CheckBox viewSelected, CheckBox viewUnselected1, CheckBox viewUnselected2){
        viewSelected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                viewUnselected1.setSelected(false);
                viewUnselected2.setSelected(false);
            }
        });
    }

    public void BtnCancelParameters(Button btn){
        btn.setOnAction(event -> {
            createProjetStage.close();
        });
    }

    private void BtnApplyParameters(Button btn){
        btn_apply.setOnAction(event -> {

            // CREATION DE L'OBET ClassiCreation

            Basemap currentBasemap = null;
            if(check_blankSatellite.isSelected()){
                currentBasemap = Basemap.createImagery();
            }else if(check_namedSatellite.isSelected()){
                currentBasemap = Basemap.createImageryWithLabels();
            }else if(check_streetview.isSelected()){
                currentBasemap = Basemap.createStreets();
            }

            ClassicCreation.CourseOrder order = null;
            if(check_freeOrder.isSelected()){
                order = ClassicCreation.CourseOrder.NO_ORDERED;
            }else if(check_forcedOrder.isSelected()){
                order = ClassicCreation.CourseOrder.ORDERED;
            }

            final ClassicCreation classicCreation = new ClassicCreation(txt_runName.getText(), currentBasemap, order);

            try{
                final FXMLLoader fxmlLoader = new FXMLLoader(Viasa.class.getResource("fxml/creations/classicCreationTab.fxml"));
                final ClassicCreationTabController classicCreationTabController = new ClassicCreationTabController(viasaController, classicCreation);
                fxmlLoader.setController(classicCreationTabController);
                final Parent root = fxmlLoader.load(); // LORS D'UN LOAD : SA VA APPELLE LA METHODE INITIALIZE DU CONTROLLEUR (ICI classicCreationTabController)
                final Tab tab = new Tab();
                tab.setText(txt_runName.getText());
                tab.setContent(root);
                classicCreationTabController.setTab(tab);
                viasaController.getCreationTabControllers().add(classicCreationTabController);
                viasaController.getCreator_TabPane().getTabs().add(tab);
                viasaController.getCreator_TabPane().getSelectionModel().select(tab);
            }catch(IOException e){
                System.out.println(e);
            }
            createProjetStage.close();
        });
    }
}
