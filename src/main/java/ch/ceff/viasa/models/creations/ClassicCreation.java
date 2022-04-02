package ch.ceff.viasa.models.creations;

import com.esri.arcgisruntime.mapping.Basemap;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette objet représente un course d'orientation.
 * Cette objet pourra être serializé dans un fichier
 */
public class ClassicCreation implements Serializable{

    private final List<Point> points;
    private final List<Chemin> chemins;

    private String name;

    private transient Basemap currentBaseMap;
    private CourseOrder order;

    public enum CourseOrder implements Serializable{
        ORDERED,
        NO_ORDERED
    }

    public ClassicCreation(String name, Basemap currentBaseMap, CourseOrder order){
        this(name);
        this.currentBaseMap = currentBaseMap;
        this.order = order;
    }


//    public ClassicCreation(){
//        name = "NOM PAR DEFAUT";
//        points = new ArrayList<>();
//        chemins = new ArrayList<>();
//    }

    public ClassicCreation(String name){
        this.name = name;
        points = new ArrayList<>();
        chemins = new ArrayList<>();
    }

//    public ClassicCreation(String name, Basemap currentBaseMap){
//        this(name);
//        this.currentBaseMap = currentBaseMap;
//    }

    public static class Point implements Serializable{
        private Longitude longitude;
        private Latitude latitude;
        private String name;

        private final transient SimpleStringProperty nameProperty;

        public Point(Longitude longitude, Latitude latitude, String name){
            this.longitude = longitude;
            this.latitude = latitude;
            this.name = name;
            nameProperty = new SimpleStringProperty(name);
            nameProperty.addListener((observable, oldValue, newValue) -> this.name = newValue);
        }


        public static abstract class GeographyDMS implements Serializable{
            private int degree, minute;
            private float second;

            public GeographyDMS(int degree, int minute, float second){
                this.degree = degree;
                this.minute = minute;
                this.second = second;
            }

            public int getDegree(){
                return degree;
            }

            public void setDegree(int degree){
                this.degree = degree;
            }

            public int getMinute(){
                return minute;
            }

            public void setMinute(int minute){
                this.minute = minute;
            }

            public float getSecond(){
                return second;
            }

            public void setSecond(float second){
                this.second = second;
            }
        }

        public static class Latitude extends GeographyDMS{
            public Latitude(int degree, int minute, float second){
                super(degree, minute, second);
            }
        }

        public static class Longitude extends GeographyDMS{
            public Longitude(int degree, int minute, float second){
                super(degree, minute, second);
            }
        }

        public Longitude getLongitude(){
            return longitude;
        }

        public void setLongitude(Longitude longitude){
            this.longitude = longitude;
        }

        public Latitude getLatitude(){
            return latitude;
        }

        public void setLatitude(Latitude latitude){
            this.latitude = latitude;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public SimpleStringProperty getNameProperty(){
            return nameProperty;
        }
    }

    public class Chemin implements Serializable{
        private String name;
        private List<Point> points;

        public Chemin(List<Point> points, String name){
            this.points = points;
            this.name = name;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public List<Point> getPoints(){
            return points;
        }

        public void setPoints(List<Point> points){
            this.points = points;
        }


    }


    // ---------------- GETTERS ---------------- \\


    public List<Point> getPoints(){
        return points;
    }

    public Basemap getCurrentBaseMap(){
        return currentBaseMap;
    }


    // ---------------- SETTERS ---------------- \\

    public void setCurrentBaseMap(Basemap currentBaseMap){
        this.currentBaseMap = currentBaseMap;
    }
}
