package ch.ceff.viasa.models.calques.infos;

import ch.ceff.viasa.models.calques.Calque;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class CalqueChemin extends Calque{

    private final List<Point2D> points;

    private float distanceTotal;

    public CalqueChemin(){
        super();
        points = new ArrayList<>();
    }

    public CalqueChemin (String name, List<Point2D> points){
        super(name);
        this.points = points;
        calculeDistanceTotal();
    }

    private void calculeDistanceTotal(){

        // TODO AVEC LA LISTE DES points, calculer la distance total

        // distanceTotal = ?
    }

    // ---------------- GETTERS ---------------- \\

    public List<Point2D> getPoints(){
        return points;
    }

    public float getDistanceTotal(){
        return distanceTotal;
    }
}
