package mt.edu.um.getalift;

class MyPoint {
    int id;
    Double lat;
    Double lng;
    int seconds_from_start;
    int route_id;

    public MyPoint(int id, Double lat, Double lng, int seconds_from_start, int route_id){
        this.id = id;
        this.lat=lat;
        this.lng=lng;
        this.seconds_from_start = seconds_from_start;
        this.route_id = route_id;
    }

    public MyPoint(Double lat, Double lng, int route_id){
        this.lat=lat;
        this.lng=lng;

        this.route_id = route_id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getSeconds_from_start() {
        return seconds_from_start;
    }

    public void setSeconds_from_start(int seconds_from_start) {
        this.seconds_from_start = seconds_from_start;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
