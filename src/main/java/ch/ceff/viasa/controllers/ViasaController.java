package ch.ceff.viasa.controllers;

import ch.ceff.viasa.Viasa;
import ch.ceff.viasa.constants.LeftToolBarTypes;
import ch.ceff.viasa.controllers.calques.CalqueController;
import ch.ceff.viasa.controllers.calques.infos.CalqueCheminInfoController;
import ch.ceff.viasa.controllers.calques.infos.CalquePointInfoController;
import ch.ceff.viasa.controllers.creations.ClassicCreationTabController;
import ch.ceff.viasa.controllers.tabs.AccueilController;
import ch.ceff.viasa.controls.MyCallBack;
import ch.ceff.viasa.controls.MyTreeItem;
import ch.ceff.viasa.controls.ToolHbox;
import ch.ceff.viasa.listeners.CalqueInfoListener;
import ch.ceff.viasa.listeners.ToolHboxListener;
import ch.ceff.viasa.models.calques.Calque;
import ch.ceff.viasa.models.calques.infos.CalqueChemin;
import ch.ceff.viasa.models.calques.infos.CalquePoint;
import ch.ceff.viasa.models.stages.CreateProjetStage;
import ch.ceff.viasa.services.PrintingService;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class ViasaController implements Initializable{

    @FXML
    private BorderPane root_BorderPane;

    @FXML
    private VBox toolBar_Vbox;

    @FXML
    private MenuBar headerMenuBar_MenuBar;

    @FXML
    private TabPane creator_TabPane;

    @FXML
    private TreeView calques_TreeView;

    @FXML
    private Tab accueil_Tab, information_Tab;

    private final List<ToolHbox> toolHboxes;

    private final List<ClassicCreationTabController> creationTabControllers;

    private ToolHboxListener toolHboxListener;
    private CalqueInfoListener calqueInfoListener;

    private ToolHbox selectedToolHbox;

    private CalquePointInfoController calquePointInfoController;
    private CalqueCheminInfoController calqueCheminInfoController;

    public ViasaController(){
        toolHboxes = new ArrayList<>();
        creationTabControllers = new ArrayList<>();

        calquePointInfoController = null;
        calqueCheminInfoController = null;

        initListeners();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

        calques_TreeView.setCellFactory(new MyCallBack());
        calques_TreeView.setShowRoot(false);

        initMyHboxes();
        initTopMenuBar();
        initCreatorTabPane();
        initAccueil();

    }

    private void initListeners(){
        initCalqueInfoListener();
        initToolHboxListener();
    }

    /**
     * Cette méthode va définir le code qui devra être executer lorsque
     * la méthode updateLeftToolBar sera appelé (cette dite méthode sera appelé lorsque l'utilisateur selectionnera un des outils dans
     * la bar d'outil).
     */
    private void initToolHboxListener(){
        toolHboxListener = excepted -> {
            final AtomicBoolean found = new AtomicBoolean(false);
            toolHboxes.forEach(toolHbox -> {
                if(excepted != toolHbox){
                    toolHbox.setSelected(false);
                }else{
                    toolHbox.setSelected(!toolHbox.isSelected());
                }

                if(toolHbox.isSelected()){
                    toolHbox.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                    found.set(true);
                    selectedToolHbox = toolHbox;
                }else{
                    toolHbox.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
                    if(!found.get()) selectedToolHbox = null;
                }
            });
        };
    }

    /**
     * Cette méthode va définir le code qui devra être executer lors d'un clic sur un calque pour mettre a jour les informations pour qu'elle corresponde au calque selectionné
     */
    private void initCalqueInfoListener(){
        calqueInfoListener = calque -> {
            if(calque instanceof CalquePoint){
                if(calquePointInfoController != null){
                    calquePointInfoController.updateData((CalquePoint) calque);
                    information_Tab.setContent(calquePointInfoController.getRoot_StackPane());
                }else{
                    try{
                        final FXMLLoader fxmlLoader = new FXMLLoader(Viasa.class.getResource("fxml/calques/infos/calquePointInfo.fxml"));
                        final CalquePointInfoController calquePointInfoController = new CalquePointInfoController();
                        fxmlLoader.setController(calquePointInfoController);
                        final Parent root = fxmlLoader.load();
                        calquePointInfoController.updateData((CalquePoint) calque);
                        information_Tab.setContent(root);
                        this.calquePointInfoController = calquePointInfoController;
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

            }else if(calque instanceof CalqueChemin){
                if(calqueCheminInfoController != null){
                    calqueCheminInfoController.updateData((CalqueChemin) calque);
                    information_Tab.setContent(calqueCheminInfoController.getRoot_StackPane());
                }else{
                    try{
                        final FXMLLoader fxmlLoader = new FXMLLoader(Viasa.class.getResource("fxml/calques/infos/calqueCheminInfo.fxml"));
                        final CalqueCheminInfoController calqueCheminInfoController = new CalqueCheminInfoController();
                        fxmlLoader.setController(calqueCheminInfoController);
                        final Parent root = fxmlLoader.load();
                        calqueCheminInfoController.updateData((CalqueChemin) calque);
                        information_Tab.setContent(root);
                        this.calqueCheminInfoController = calqueCheminInfoController;
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /**
     * Cette méthode initialise les objets ToolHbox
     * Leur propriété toolHboxListener pointera sur cette objet (this)
     * Leur propriété leftToolBarTypes sera définie en fonction de leur id
     * Ils sont également ajouté à une list (permet : optimisation du code)
     */
    private void initMyHboxes(){
        toolBar_Vbox.getChildren().forEach(node -> {
            if(node instanceof ToolHbox){
                final ToolHbox toolHbox = (ToolHbox) node;
                toolHbox.setToolHboxListener(toolHboxListener);
                if(toolHbox.getId().equals("point-myhbox")){
                    toolHbox.setLeftToolBarTypes(LeftToolBarTypes.POINT);
                }else if(toolHbox.getId().equals("ruler-myhbox")){
                    toolHbox.setLeftToolBarTypes(LeftToolBarTypes.RULER);
                }
                toolHboxes.add(toolHbox);
            }
        });
    }

    /**
     * Cette méthode définie les evénements au niveau des sous menus de la bar de menu
     * L'application pour containir plusieur MenuBar. Afin d'isoler au mieux les sous menus des différents menuBar,
     * il serai plus facile de travailler avec des variables local
     */
    private void initTopMenuBar(){

        final MenuItem newMenuItem = headerMenuBar_MenuBar.getMenus()
                .filtered(menu -> menu.getText().equalsIgnoreCase("fichier")).get(0)
                .getItems().filtered(menuItem -> menuItem.getText().equalsIgnoreCase("New")).get(0);

        final MenuItem printMenuItem = headerMenuBar_MenuBar.getMenus()
                .filtered(menu -> menu.getText().equalsIgnoreCase("fichier")).get(0)
                .getItems().filtered(menuItem -> menuItem.getText().equalsIgnoreCase("Print")).get(0);

        newMenuItem.setOnAction(event -> {
            final CreateProjetStage createProjetStage = new CreateProjetStage((Stage) root_BorderPane.getScene().getWindow(), this);
            createProjetStage.show();
        });

        printMenuItem.setOnAction(event -> {
            final Printing printing = new Printing();
            printing.printAll();
        });
    }

    private void initCreatorTabPane(){
        creator_TabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("SWAP ");
            boolean found = false;
            for(ClassicCreationTabController classicCreationTabController : creationTabControllers){
                if(classicCreationTabController.getRoot_StackPane() == newValue.getContent()){
                    System.out.println("EQUALS !!!!!!!!");
                    calques_TreeView.setRoot(classicCreationTabController.getRootItem());
                    found = true;
                    break;
                }
            }
            if(!found){
                calques_TreeView.setRoot(null);
            }
        });
    }

    /**
     * Cette méthode va charger le fichier fxml dans le "accueil_Tab"
     */
    private void initAccueil(){
        try{
            final FXMLLoader fxmlLoader = new FXMLLoader(Viasa.class.getResource("fxml/tabs/accueil.fxml"));
            AccueilController accueilController = new AccueilController(this);
            fxmlLoader.setController(accueilController);
            final Parent root = fxmlLoader.load();
            accueil_Tab.setContent(root);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Ajout d'un calque dans la liste des calques
     * L'Objet TreeItem "destination" : C'est à cette objet que l'on va ajouté le calque
     *
     * @param calque
     * @param name
     * @param destination
     */
    public CalqueController addCalque(Calque calque, String name, TreeItem<AnchorPane> destination){
        try{
            final FXMLLoader fxmlLoader = new FXMLLoader(Viasa.class.getResource("fxml/calques/calque.fxml"));
            final CalqueController calqueController = new CalqueController(calqueInfoListener, name);
            fxmlLoader.setController(calqueController);
            final Parent root = fxmlLoader.load();
            final MyTreeItem<AnchorPane> item = new MyTreeItem<>((AnchorPane) root, calqueController);

            if(calque instanceof CalquePoint){
                ((CalquePoint) calque).getPoint().getNameProperty().bind(calqueController.getTxt_name().textProperty());
                calqueController.setImage(new Image(Viasa.class.getResource("images/point.png").toURI().toString()));
                calqueController.setCalque(calque);
            }

            destination.getChildren().add(item);
            item.update();

            return calqueController;
        }catch(IOException | URISyntaxException e){
            e.printStackTrace();
        }
        return null;
    }

    private class Printing{

        public final Tab tab;

        public final Optional<ClassicCreationTabController> optionalClassicCreationTabController;

        public Printing(){
            tab = creator_TabPane.getSelectionModel().getSelectedItem();
            optionalClassicCreationTabController = creationTabControllers.stream()
                    .filter(classicCreationTabController -> classicCreationTabController.getTab().equals(tab))
                    .findAny();
        }

        public void printAll(){
                printMap();
                printData();
        }

        private void printMap(){
            if(optionalClassicCreationTabController.isPresent()){
                final ClassicCreationTabController classicCreationTabControllerResult = optionalClassicCreationTabController.get();
                classicCreationTabControllerResult.getMinimaps_Vbox().setVisible(false);
                PrintingService.print((Region) tab.getContent());
                classicCreationTabControllerResult.getMinimaps_Vbox().setVisible(true);
            }
        }

        private void printData(){

        }

    }

    // ---------------- GETTERS ---------------- \\

    public TabPane getCreator_TabPane(){
        return creator_TabPane;
    }

    public TreeView getCalques_TreeView(){
        return calques_TreeView;
    }

    public ToolHbox getSelectedToolHbox(){
        return selectedToolHbox;
    }

    public List<ClassicCreationTabController> getCreationTabControllers(){
        return creationTabControllers;
    }


    // ---------------- SETTERS ---------------- \\


}
