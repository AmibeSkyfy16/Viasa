package ch.ceff.viasa.controllers.creations;

import ch.ceff.viasa.Viasa;
import ch.ceff.viasa.constants.LeftToolBarTypes;
import ch.ceff.viasa.controllers.ViasaController;
import ch.ceff.viasa.controls.MyTreeItem;
import ch.ceff.viasa.controls.ToolHbox;
import ch.ceff.viasa.models.calques.Calque;
import ch.ceff.viasa.models.calques.infos.CalquePoint;
import ch.ceff.viasa.models.creations.ClassicCreation;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.CoordinateFormatter;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ClassicCreationTabController implements Initializable{

    @FXML
    private StackPane root_StackPane;

    @FXML
    private VBox minimaps_Vbox;

    @FXML
    private ScrollPane minimaps_ScrollPane;

    @FXML
    private CheckBox hiddenMinimaps_CheckBox;

    @FXML
    private ImageView streetView_ImageView;
    @FXML
    private ImageView imageryWithLabels_ImageView;
    @FXML
    private ImageView imagery_ImageView;

    private Tab tab;

    private final TreeItem<AnchorPane> rootItem; // Control qui gère les calques

    private final List<MyTreeItem> myTreeItems;

    private final List<Calque> calques;

    private final ViasaController viasaController;

    private final ClassicCreation classicCreation; // LORS D'UNE SAUVEGARDE, CETTE OBJET SERA CREER ET INIT... et ensuite sauvegarde

    private final GraphicsOverlay graphicsOverlay;

    private final MapView mapView;

    private ArcGISMap map;

    private ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphics;


    public ClassicCreationTabController(ViasaController viasaController, ClassicCreation classicCreation){
        this.viasaController = viasaController;
        this.classicCreation = classicCreation;
        rootItem = new TreeItem<>();
        myTreeItems = new ArrayList<>();
        calques = new ArrayList<>();
        graphicsOverlay = new GraphicsOverlay();
        mapView = new MapView();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

        initCalques();

        initMinimaps();

        createMap();

        createMinimaps();

    }

    /**
     * Cette méthode va s'occuper de l'objet TreeView (celui qui gère les calques)
     */
    private void initCalques(){
        viasaController.getCalques_TreeView().setRoot(rootItem);
        myTreeItems.forEach(MyTreeItem::update); // Mise a jour graphique des calques
    }

    private void initMinimaps(){

        hiddenMinimaps_CheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                minimaps_Vbox.getChildren().remove(minimaps_ScrollPane);
                minimaps_Vbox.setMaxHeight(30);
                minimaps_Vbox.setPrefHeight(30);
            }else{
                minimaps_Vbox.getChildren().add(minimaps_ScrollPane);
                minimaps_Vbox.setMaxHeight(200);
                minimaps_Vbox.setPrefHeight(200);
            }
        });
        hiddenMinimaps_CheckBox.selectedProperty().set(true);


        // Definission des événements lors du clique sur une des minimaps

        imagery_ImageView.setOnMousePressed(event -> {
            classicCreation.setCurrentBaseMap(Basemap.createImagery());
            map.setBasemap(classicCreation.getCurrentBaseMap());
        });
        imageryWithLabels_ImageView.setOnMousePressed(event -> {
            classicCreation.setCurrentBaseMap(Basemap.createImageryWithLabels());
            map.setBasemap(classicCreation.getCurrentBaseMap());
        });
        streetView_ImageView.setOnMousePressed(event -> {
            classicCreation.setCurrentBaseMap(Basemap.createStreets());
            map.setBasemap(classicCreation.getCurrentBaseMap());
        });

        minimaps_Vbox.toFront();

    }

    /**
     * Cette méthode créer la map de base (celle dans laquel on va créer la parcour)
     */
    private void createMap(){

        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud4751061109,none,KGE60RFLTJ05NERL1072");

        map = new ArcGISMap(classicCreation.getCurrentBaseMap());
        mapView.setMap(map);
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        mapView.setOnMouseEntered(event -> { // Changement du curseur de la souris
            final ToolHbox toolHbox = viasaController.getSelectedToolHbox();
            if(toolHbox == null) return; // Dans le cas ou aucun outils est selectionné, on ne fera rien
            if(toolHbox.getLeftToolBarTypes() == LeftToolBarTypes.CURSOR){
                root_StackPane.getScene().setCursor(ImageCursor.DEFAULT); // Pour l'instant -> cursor = le curseur de base
            }
            if(toolHbox.getLeftToolBarTypes() == LeftToolBarTypes.POINT){
                final Image image = new Image(Viasa.class.getResource("images/cursorPoint.png").toExternalForm());
                final ImageCursor imageCursor = new ImageCursor(image, 16, 0);
                root_StackPane.getScene().setCursor(imageCursor);
            }
        });
        mapView.setOnMouseExited(event -> { // Changement du curseur de la souris
            root_StackPane.getScene().setCursor(ImageCursor.DEFAULT);
        });
        mapView.setOnMouseClicked(event -> {
            if(viasaController.getSelectedToolHbox() != null){ // On est sur que au moins un outil est selectionné
                if(event.isStillSincePress() && event.getButton() == MouseButton.PRIMARY){ // On clique sur la map
                    final Point2D mapViewPoint = new Point2D(event.getX(), event.getY());
                    switch(viasaController.getSelectedToolHbox().getLeftToolBarTypes()){ // ToolHBox(Outil qui est selectionné) SOIT un point, un chemin ou la regle
                        case POINT:

                            identifyGraphics = mapView.identifyGraphicsOverlayAsync(graphicsOverlay, mapViewPoint, 5, false);

                            identifyGraphics.addDoneListener(() -> Platform.runLater(() -> {

                                IdentifyGraphicsOverlayResult result = null;

                                try{
                                    result = identifyGraphics.get();
                                }catch(InterruptedException | ExecutionException e){
                                    e.printStackTrace();
                                }

                                final List<Graphic> graphics = result.getGraphics();

                                if(graphics.isEmpty()){ // SI LA LISTE EST VIDE, C'EST QUE L'ON A PAS ENCORE PLACE DE POINT A CETTE ENDROIT
                                    addPoint(mapViewPoint);
                                }else{ // SINON IL Y A DEJA UN OU PLUSIEURS POINT DANS L'ENDROIT OU L'ON A CLIQUE
                                    graphics.forEach(graphic -> {
                                        if(graphic.getSymbol() instanceof TextSymbol) return;
                                        if(graphic.isSelected()) graphic.setSelected(false);
                                        else graphic.setSelected(true);
                                    });
                                }
                            }));
                            break;
                        case CHEMIN:
                            addChemin();
                            break;
                    }
                }
            }
        });

        root_StackPane.getChildren().add(mapView);

    }

    private void addPoint(Point2D point){

        // Ajout du point graphiquement
        final PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(new Image(Viasa.class.getResource("images/Point.png").toString()));

        final TextSymbol textSymbol = new TextSymbol();
        textSymbol.setText("sdkjfdgfdsjgbfdg");
        textSymbol.setOffsetX(5); // TODO Modifier le offset X et Y pour que le text sois position correctement
        textSymbol.setOffsetY(10);
        ;
        pointSymbol.setHeight(40);
        pointSymbol.setWidth(40);
        final Point mapPoint = mapView.screenToLocation(point);
        final Point normalizedMapPoint = (Point) GeometryEngine.normalizeCentralMeridian(mapPoint);
        graphicsOverlay.getGraphics().add(new Graphic(normalizedMapPoint, pointSymbol));
        graphicsOverlay.getGraphics().add(new Graphic(normalizedMapPoint, textSymbol));


        // Ajout du point (data)

        String test = CoordinateFormatter.toLatitudeLongitude(normalizedMapPoint, CoordinateFormatter.LatitudeLongitudeFormat.DEGREES_MINUTES_SECONDS, 1);


        final ClassicCreation.Point.Longitude longitude = new ClassicCreation.Point.Longitude(4, 4, 4);
        final ClassicCreation.Point.Latitude latitude = new ClassicCreation.Point.Latitude(4, 7, 4);


        ClassicCreation.Point point1 = new ClassicCreation.Point(longitude, latitude, "CheckPoint 1");

        point1.getNameProperty().addListener((observable, oldValue, newValue) -> {
            textSymbol.setText(newValue);

        });

        // BINDING

        classicCreation.getPoints().add(point1);


        // Ajout du calque pour ce point
        viasaController.addCalque(new CalquePoint(point1), "CheckPoint 1", rootItem);
    }

    private void addChemin(){
        // TODO
    }

    private void createMinimaps(){


//        final MapView mapView1 = new MapView();
//        final MapView mapView2 = new MapView();
//        final MapView mapView3 = new MapView();
//        final MapView mapView4 = new MapView();
//
//        mapView1.setMap(new ArcGISMap(Basemap.createImagery()));
//        mapView1.setAttributionTextVisible(false);
//        mapView1.getStyleClass().add("minimaps");
//        mapView1.setOnMouseDragged(null);
////        mapView1.setOnMouseEntered();
//
//        mapView2.setMap(new ArcGISMap(Basemap.createImageryWithLabels()));
//        mapView2.setAttributionTextVisible(false);
//        mapView2.getStyleClass().add("minimaps");
//        mapView2.setOnMouseDragged(null);
//
//        mapView3.setMap(new ArcGISMap(Basemap.createOpenStreetMap()));
//        mapView3.setAttributionTextVisible(false);
//        mapView3.getStyleClass().add("minimaps");
//        mapView3.setOnMouseDragged(null);
//
//        mapView4.setMap(new ArcGISMap(Basemap.createTerrainWithLabels()));
//        mapView4.setAttributionTextVisible(false);
//        mapView4.getStyleClass().add("minimaps");
//        mapView4.setOnMouseDragged(null);
//
//        // TODO CHANGER LA MAP SUIVANT LA MINIMAPS QUI EST SELECTIONNE
//
//        // TODO AFFICHER OU ON LA LISTE DE MINIMAPS SUIVANT SI LA CHECKBOX EST COCHE OU PAS
//
//        one_StackPane.getChildren().add(mapView1);
//        two_StackPane.getChildren().add(mapView2);
//        three_StackPane.getChildren().add(mapView3);
//        four_StackPane.getChildren().add(mapView4);

        minimaps_Vbox.toFront();

    }

    private void save(){
        final List<ClassicCreation.Point> points = new ArrayList<>();
        final List<ClassicCreation.Chemin> chemins = new ArrayList<>();
        for(Calque calque : calques){
            if(calque instanceof CalquePoint){
                final CalquePoint calquePoint = (CalquePoint) calque;
//                points.add(new ClassicCreation.Point(calquePoint.getPoint().getX(), calquePoint.getPoint().getY(), calque.getName()));
            }
        }

//        classicCreation = new ClassicCreation(tab.getText(), points, chemins);

//        classicCreation.save(new File());
    }

    // ---------------- GETTERS ---------------- \\

    public StackPane getRoot_StackPane(){
        return root_StackPane;
    }

    public TreeItem<AnchorPane> getRootItem(){
        return rootItem;
    }

    public Tab getTab(){
        return tab;
    }

    public VBox getMinimaps_Vbox(){
        return minimaps_Vbox;
    }

    // ---------------- SETTERS ---------------- \\


    public void setTab(Tab tab){
        this.tab = tab;
    }
}
