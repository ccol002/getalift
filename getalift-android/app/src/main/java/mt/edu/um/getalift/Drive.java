package mt.edu.um.getalift;

class Drive {
    private String destination;
    private String origin;
    private String date;

    public Drive( String origin, String destination,String date){
        this.date = date;
        this.destination = destination;
        this.origin = origin;
    }

    public String getDestination() {return destination;}

    public String getDate() {return date;}

    public String getOrigin() {return origin;}

    public void setDate(String date) {this.date = date;}

    public void setDestination(String destination) {this.destination = destination;}

    public void setOrigin(String origin) {this.origin = origin;}


}
