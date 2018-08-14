package mt.edu.um.getalift;

import android.os.Parcel;
import android.os.Parcelable;

class MyPoint implements Parcelable{
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

    protected MyPoint(Parcel in) {
        id = in.readInt();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
        seconds_from_start = in.readInt();
        route_id = in.readInt();
    }

    public static final Creator<MyPoint> CREATOR = new Creator<MyPoint>() {
        @Override
        public MyPoint createFromParcel(Parcel in) {
            return new MyPoint(in);
        }

        @Override
        public MyPoint[] newArray(int size) {
            return new MyPoint[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        if (lat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(lat);
        }
        if (lng == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(lng);
        }
        parcel.writeInt(seconds_from_start);
        parcel.writeInt(route_id);
    }
}
