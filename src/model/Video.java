package model;

import model.Document;
import java.io.Serializable;

public class Video implements Serializable, Document{
    private static int count = 0;
    private String id;
    private String name;
    private int disk;
    private float price;

    //constructor
    public Video(String n, int d, float p){
        this.name = n;
        this.disk = d;
        this.price = p;
        count++;
        this.id = "V100" + Integer.toString(count);
    }

    
    //getter
    public String getID() {return this.id;}
    public String getName() {return this.name;}
    public int getDisk() {return this.disk;}
    public float getPrice() {return this.price;}

    //setter
    public void setName(String n) {this.name=n;}
    public void setDisk(int d) {this.disk=d;}
    public void setPrice(float p) {this.price=p;}
    @Override
    public String toString(){
       return name + " - " + disk + " - " + price;
    }

    @Override
    public String toHtmlDoc() {
        String VideoDoc;
        VideoDoc = "<html>";
        VideoDoc += "<head><title>Video:" + this.disk + " - " + this.name + "</title></head>";
        VideoDoc += "<body>";
        VideoDoc += this.id + " " + this.name + " " + this.disk + " " + this.price;
        VideoDoc += "</body></html>";
        return VideoDoc;
    }
}
