package ch.ceff.viasa.models.calques;

public abstract class Calque{

    protected String name;

    protected Calque(){
        name = "";
    }

    protected Calque(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
