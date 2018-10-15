package mt.edu.um.getalift;

import java.util.ArrayList;
import java.util.List;

class Drive {
    private String destination;
    private String origin;
    private String date;
    private int id;

    public Drive( int id, String origin, String destination,String date){
        this.date = date;
        this.destination = destination;
        this.origin = origin;
        this.id = id;
    }

    public String getDestination() {return destination;}

    public String getDate() {return date;}

    public String getOrigin() {return origin;}

    public int getId() {return id;}


    public void setDate(String date) {this.date = date;}

    public void setDestination(String destination) {this.destination = destination;}

    public void setOrigin(String origin) {this.origin = origin;}


    public List<String> getInfo (){
        List<String > info = new ArrayList<>();
        info.add(String.valueOf(id));
        info.add(date);
        info.add(destination);
        info.add(origin);
        return info;}


}
