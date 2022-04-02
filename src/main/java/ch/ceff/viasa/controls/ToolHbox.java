package ch.ceff.viasa.controls;

import ch.ceff.viasa.constants.LeftToolBarTypes;
import ch.ceff.viasa.listeners.ToolHboxListener;
import javafx.scene.layout.HBox;

public class ToolHbox extends HBox{

    private boolean selected;

    private ToolHboxListener toolHboxListener;

    private LeftToolBarTypes leftToolBarTypes;

    public ToolHbox(){
        super();
        selected = false;
        toolHboxListener = null;

        this.setOnMouseClicked(event -> {
            toolHboxListener.updateLeftToolBar(this);
            System.out.println("TYPES : " + leftToolBarTypes.name());
        });

    }

    // ---------------- GETTERS ---------------- \\

    public boolean isSelected(){
        return selected;
    }

    public LeftToolBarTypes getLeftToolBarTypes(){
        return leftToolBarTypes;
    }

    // ---------------- SETTERS ---------------- \\

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public void setToolHboxListener(ToolHboxListener toolHboxListener){
        this.toolHboxListener = toolHboxListener;
    }

    public void setLeftToolBarTypes(LeftToolBarTypes leftToolBarTypes){
        this.leftToolBarTypes = leftToolBarTypes;
    }
}
