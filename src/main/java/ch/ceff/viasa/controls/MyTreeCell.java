package ch.ceff.viasa.controls;

import javafx.scene.Node;
import javafx.scene.control.TreeCell;

public class MyTreeCell<T> extends TreeCell<T>{

    public MyTreeCell(){

    }


    @Override
    protected void updateItem(T item, boolean empty){
        super.updateItem(item, empty);
        this.setDisclosureNode(null);
        this.setText(null);

        if(empty || item == null || !(item instanceof Node)){
            this.setGraphic(null);
        }else{
            this.setGraphic((Node) item);
        }

    }

}
