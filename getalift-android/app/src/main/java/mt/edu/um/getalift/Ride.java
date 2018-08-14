package mt.edu.um.getalift;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Ride implements Parcelable {
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private int route_id;
    private int user_id;
    private String user_name;
    private int minWalking;
    private MyDate date;
    private List<MyPoint> routePoints;
    private MyPoint closestPointStart;
    private MyPoint closestPointEnd;
    private int distancePointStart;
    private int distancePointEnd;

    public Ride(double startLat, double startLng, double endLat, double endLng, int route_id, int user_id, String user_name, int minWalking, MyDate date, List<MyPoint> routePoints, MyPoint closestPointStart, MyPoint closestPointEnd, int distancePointStart, int distancePointEnd) {
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.route_id = route_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.minWalking = minWalking;
        this.date = date;
        this.routePoints = routePoints;
        this.closestPointStart = closestPointStart;
        this.closestPointEnd = closestPointEnd;
        this.distancePointStart = distancePointStart;
        this.distancePointEnd = distancePointEnd;
    }

    protected Ride(Parcel in) {
        startLat = in.readDouble();
        startLng = in.readDouble();
        endLat = in.readDouble();
        endLng = in.readDouble();
        route_id = in.readInt();
        user_id = in.readInt();
        user_name = in.readString();
        minWalking = in.readInt();
        routePoints = in.createTypedArrayList(MyPoint.CREATOR);
        closestPointStart = in.readParcelable(MyPoint.class.getClassLoader());
        closestPointEnd = in.readParcelable(MyPoint.class.getClassLoader());
        distancePointStart = in.readInt();
        distancePointEnd = in.readInt();
    }

    public static final Creator<Ride> CREATOR = new Creator<Ride>() {
        @Override
        public Ride createFromParcel(Parcel in) {
            return new Ride(in);
        }

        @Override
        public Ride[] newArray(int size) {
            return new Ride[size];
        }
    };

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

    public int getMinWalking() {
        return minWalking;
    }

    public void setMinWalking(int minWalking) {
        this.minWalking = minWalking;
    }

    public void setDate(MyDate date) {
        this.date = date;
    }

    public MyDate getDate() {
        return date;
    }

    public List<MyPoint> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<MyPoint> routePoints) {
        this.routePoints = routePoints;
    }

    public MyPoint getClosestPointStart() {
        return closestPointStart;
    }

    public void setClosestPointStart(MyPoint closestPointStart) {
        this.closestPointStart = closestPointStart;
    }

    public MyPoint getClosestPointEnd() {
        return closestPointEnd;
    }

    public void setClosestPointEnd(MyPoint closestPointEnd) {
        this.closestPointEnd = closestPointEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getDistancePointStart() {
        return distancePointStart;
    }

    public void setDistancePointStart(int distancePointStart) {
        this.distancePointStart = distancePointStart;
    }

    public int getDistancePointEnd() {
        return distancePointEnd;
    }

    public void setDistancePointEnd(int distancePointEnd) {
        this.distancePointEnd = distancePointEnd;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(startLat);
        parcel.writeDouble(startLng);
        parcel.writeDouble(endLat);
        parcel.writeDouble(endLng);
        parcel.writeInt(route_id);
        parcel.writeInt(user_id);
        parcel.writeString(user_name);
        parcel.writeInt(minWalking);
        parcel.writeTypedList(routePoints);
        parcel.writeParcelable(closestPointStart, i);
        parcel.writeParcelable(closestPointEnd, i);
        parcel.writeInt(distancePointStart);
        parcel.writeInt(distancePointEnd);

    }
}
