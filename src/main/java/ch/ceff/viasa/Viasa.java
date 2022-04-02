package ch.ceff.viasa;

import ch.ceff.viasa.controllers.ViasaController;
import com.esri.arcgisruntime.arcgisservices.ArcGISMapServiceInfo;
import com.esri.arcgisruntime.arcgisservices.RelationshipInfo;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.internal.jni.CoreRelationshipInfo;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Viasa extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{

        final FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("fxml/viasa.fxml"));
        final ViasaController viasaController = new ViasaController();
        fxmlLoader.setController(viasaController);
        final Parent root = fxmlLoader.load();

        primaryStage.setTitle("Viasa");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(400);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

}
