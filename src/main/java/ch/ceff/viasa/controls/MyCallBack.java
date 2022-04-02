package ch.ceff.viasa.controls;

import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class MyCallBack implements Callback<TreeView<AnchorPane>, MyTreeCell<AnchorPane>>{

    public MyCallBack(){ }

    @Override
    public MyTreeCell<AnchorPane> call(TreeView<AnchorPane> param){
        return new MyTreeCell<>();
    }
}
