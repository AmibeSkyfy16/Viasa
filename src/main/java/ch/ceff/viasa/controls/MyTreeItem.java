package ch.ceff.viasa.controls;

import ch.ceff.viasa.controllers.calques.CalqueController;
import ch.ceff.viasa.listeners.CalqueControllerListener;
import javafx.event.Event;
import javafx.scene.control.TreeItem;

public class MyTreeItem<T> extends TreeItem<T>{

    private final CalqueControllerListener calqueControllerListener;

    private final TreeModificationEvent<T> childrenModificaionEvent;

    public MyTreeItem(T value, CalqueControllerListener calqueControllerListener){
        super(value);
        this.calqueControllerListener = calqueControllerListener;
        childrenModificaionEvent = new TreeModificationEvent<>(TreeItem.childrenModificationEvent(), this);
        this.addEventHandler(TreeItem.childrenModificationEvent(), event -> {
            calqueControllerListener.updateCalques(this);
        });
    }

    public void update(){
        Event.fireEvent(this, childrenModificaionEvent);
    }


    // ---------------- GETTERS ---------------- \\

    public CalqueController getCalqueController(){
        return (CalqueController) calqueControllerListener;
    }

}
