package mt.edu.um.getalift;

public class Ride {
    private String origin;
    private String destination;
    private User driver;
    private String date;
    private int minWalking;

    public Ride(String origin, String destination, User driver, String date, int minWalking) {
        this.origin = origin;
        this.destination = destination;
        this.driver = driver;
        this.date = date;
        this.minWalking = minWalking;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
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
