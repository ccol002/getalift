package mt.edu.um.getalift;

public class Ride {
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private int route_id;
    private int user_id;
    private String user_name;
    private String date;
    private int minWalking;

    public Ride(double startLat, double startLng, double endLat, double endLng, int route_id, int user_id, String user_name, String date, int minWalking) {
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.route_id = route_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.date = date;
        this.minWalking = minWalking;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMinWalking() {
        return minWalking;
    }

    public void setMinWalking(int minWalking) {
        this.minWalking = minWalking;
    }
}
