package ch.ceff.viasa.models.calques.infos;

import ch.ceff.viasa.models.calques.Calque;
import ch.ceff.viasa.models.creations.ClassicCreation;
import javafx.geometry.Point2D;

public class CalquePoint extends Calque{

//    private final Point2D point;

    private final ClassicCreation.Point point;

    public CalquePoint(ClassicCreation.Point point){
        super();
//        point = new Point2D(0 ,0);
        this.point = point;
        name = point.getName();
    }

//    public CalquePoint(String name, Point2D point){
//        super(name);
////        this.point = point;
//
//    }

    public ClassicCreation.Point getPoint(){
        return point;
    }


//    public Point2D getPoint(){
//        return point;
//    }
}
