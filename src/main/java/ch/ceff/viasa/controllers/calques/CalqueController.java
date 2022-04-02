package ch.ceff.viasa.controllers.calques;

import ch.ceff.viasa.controls.MyTreeItem;
import ch.ceff.viasa.listeners.CalqueControllerListener;
import ch.ceff.viasa.listeners.CalqueInfoListener;
import ch.ceff.viasa.models.calques.Calque;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CalqueController implements Initializable, CalqueControllerListener{

    @FXML
    private AnchorPane root_AnchorPane;

    @FXML
    private ImageView arrow_ImageView, type_ImageView;

    @FXML
    private TextField txt_name;

    private Calque calque;

    private CalqueInfoListener calqueInfoListener;

    public CalqueController(CalqueInfoListener calqueInfoListener, String name){
        this.calqueInfoListener = calqueInfoListener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

        initCalques();

    }

    /**
     * Cette méthode met à jour les calques ( exemple :SI UN CALQUE DE TYPE CHEMIN NE CONTIENT PAS DE POINT -> on affiche pas le triangle, ...)
     * @param myTreeItem
     */
    @Override
    public void updateCalques(MyTreeItem myTreeItem){
        if(myTreeItem.isLeaf()){
            arrow_ImageView.setVisible(false);
        }else{
            arrow_ImageView.setVisible(true);
            arrow_ImageView.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1){
                    myTreeItem.setExpanded(!myTreeItem.isExpanded());
                    arrow_ImageView.setRotate((arrow_ImageView.getRotate() == 90) ? 0 : 90);
                }
            });
        }
        root_AnchorPane.setOnMouseClicked(event -> {

            // OBLIGER DE FAIRE CELA LORSQUE QUE l'ON DOUBLE CLICK SUR UN CALQUE. CELA EST DU A
            // LA METHODE handleClicks DE L'OBJET TreeCellBehavior QUI SE TROUVE DANS UN PACKAGE PRIVEE !
            if(event.getClickCount() >= 2){
                myTreeItem.setExpanded(false);
                arrow_ImageView.setRotate(0);
            }

            // AFFICHE LES INFOS
            if(event.getClickCount() == 1){
                calqueInfoListener.updateTabInfo(calque);
            }

        });
    }

    private void initCalques(){
        // TODO AFFICHER OU NON LES CALQUES sur la maps (SUIVANT SI LA CHECKBOX EST COCHE OU PAS)
    }

    // ---------------- SETTERS ---------------- \\

    public void setImage(Image image){
        type_ImageView.setImage(image);
    }

    private void setName(String name){
        txt_name.setText(name);
    }

    public void setCalque(Calque calque){
        this.calque = calque;
//        setName(calque.getName());
    }

    public TextField getTxt_name(){
        return txt_name;
    }
}
