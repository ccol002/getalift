package mt.edu.um.getalift;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static mt.edu.um.getalift.CreateRideActivity.getAddressFromLocation;
import static mt.edu.um.getalift.HomeMapActivity.GoogleMapsAPIKey;

/**  Improved by Thessalène JEAN-LOUIS **/

public class ViewRideActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Ride ride;

    private MyPoint startingPoint;
    private MyPoint endingPoint;
    private MyPoint meetingPoint;
    private MyPoint droppingPoint;
    private GoogleMap mMap;
    private LatLng origin;
    private LatLng originDriver;
    private LatLng destination;
    private LatLng destinationDriver;
    private Polyline line1;
    private Polyline line2;
    private int color;


    //Creation of the intent which recovers the id of the driver selected by the user
    Intent intent_View_Ride_activity;
    private int driverId;
    private  int userID;
    int routeId;
    private FloatingActionButton btn_go_ride;
    private String adressOrigin;
    private String adressDestination;
    private String adressOriginDriver;
    private String adressDestinationDriver;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //To change after the color of the polyline of the driver of the passenger
        color=0;
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_view_ride);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.view_ride_map);
        mapFragment.getMapAsync(this);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_view_map);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ride = (Ride) getIntent().getExtras().getParcelable("UserRide");
        setTitle(ride.getUser_name()+"'s route");

        startingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerStartingPointLat",0.0),getIntent().getDoubleExtra("passengerStartingPointLng",0.0),0,0);
        endingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerEndingPointLat",0.0),getIntent().getDoubleExtra("passengerEndingPointLng",0.0),0,0);
        meetingPoint = ride.getClosestPointStart();
        droppingPoint = ride.getClosestPointEnd();

        btn_go_ride = findViewById(R.id.btn_go_ride);

        // Recovering the ride selected
        intent_View_Ride_activity = getIntent();
        if (intent_View_Ride_activity != null) {
            driverId = intent_View_Ride_activity.getIntExtra("driver_id",0);
            routeId = intent_View_Ride_activity.getIntExtra("route_id",0);
            userID = intent_View_Ride_activity.getIntExtra("userID",0);
        }

        //Go to the Ride Info View
        btn_go_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We transfer to ViewRideInfo the id of the driver selected by teh user
                Intent intentViewRideInfo = new Intent(getApplicationContext(), ViewRideInfoActivity.class);
                intentViewRideInfo.putExtra("driver_id",driverId);
                intentViewRideInfo.putExtra("passengerStartingPointLat", getIntent().getDoubleExtra("passengerStartingPointLat",0.0));
                intentViewRideInfo.putExtra("passengerStartingPointLng", getIntent().getDoubleExtra("passengerStartingPointLng",0.0));
                intentViewRideInfo.putExtra("passengerEndingPointLat", getIntent().getDoubleExtra("passengerEndingPointLat",0.0));
                intentViewRideInfo.putExtra("passengerEndingPointLng", getIntent().getDoubleExtra("passengerEndingPointLng",0.0));
                intentViewRideInfo.putExtra("route_id",routeId);
                intentViewRideInfo.putExtra("userID",userID);
                startActivity(intentViewRideInfo);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Recover the map
        mMap = googleMap;
        createRouteDriver();

        /**Draw the route between the markers of the research in red**/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startingPoint.getLat(),startingPoint.getLng()), 12));

        //Setting the route markers (starting point and ending point)
        origin = new LatLng(startingPoint.getLat(),startingPoint.getLng());
        adressOrigin = getAddressFromLocation(origin.latitude, origin.longitude, this);
        Marker marker3 = mMap.addMarker(new MarkerOptions()
                .position(origin)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .snippet(adressOrigin)
                .alpha(0.7f)
                .title(getString(R.string.txt_new_origin_point)));

        destination = new LatLng(endingPoint.getLat(),endingPoint.getLng());
        adressDestination= getAddressFromLocation(destination.latitude, destination.longitude, this);
        Marker marker4 = mMap.addMarker(new MarkerOptions()
                .position(destination)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .snippet(adressDestination)
                .alpha(0.7f)
                .title(getString(R.string.txt_new_destination_point)));

        //To draw the route between the two markers
        ViewRideActivity.DownloadTask downloadTask = new ViewRideActivity.DownloadTask(Color.RED);
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, destination);
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    public void createRouteDriver() {

        /** Creating the polyline of the driver's route*/
        originDriver = new LatLng(ride.getStartLat(), ride.getStartLng());
        adressOriginDriver = getAddressFromLocation(originDriver.latitude, originDriver.longitude, this);
        Marker marker1 = mMap.addMarker(new MarkerOptions()
                .position(originDriver)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .snippet(adressOriginDriver)
                .title("Origin driver"));

        destinationDriver = new LatLng(ride.getEndLat(), ride.getEndLng());
        adressDestinationDriver = getAddressFromLocation(destinationDriver.latitude, destinationDriver.longitude, this);
        Marker marker2 = mMap.addMarker(new MarkerOptions()
                .position(destinationDriver)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .snippet(adressDestinationDriver)
                .title("Destination driver"));

        //To draw the route between the two markers
        ViewRideActivity.DownloadTask downloadTask = new ViewRideActivity.DownloadTask(Color.RED);
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(originDriver, destinationDriver);
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        //Setting the meeting points markers (starting point and ending point)
        int seconds_from_start = (int) ride.getClosestPointStart().getSeconds_from_start()/60;
        LatLng meeting_point = new LatLng(meetingPoint.getLat(),meetingPoint.getLng());
        mMap.addMarker(new MarkerOptions().position(meeting_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .alpha(1f)
                .title("Meeting point with the driver").snippet("The driver arrives at "+ getIntent().getStringExtra("meetingTime")));

        LatLng dropping_point = new LatLng(droppingPoint.getLat(),droppingPoint.getLng());
        mMap.addMarker(new MarkerOptions().position(dropping_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .alpha(1f)
                .title("Dropping point").snippet("The driver drops you at "+ getIntent().getStringExtra("droppingTime")));
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        private int color;
        public DownloadTask(int color) {
            this.color = color;
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ViewRideActivity.ParserTask parserTask = new ViewRideActivity.ParserTask();
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                if(color==0) {
                    lineOptions.color(Color.rgb(12,232,0));
                    //lineOptions.color(Color.YELLOW);
                }else if(color==1){
                    lineOptions.color(Color.RED);
                }
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if(color==0) {
                line1 = mMap.addPolyline(lineOptions);
                color++;
            }
            else if(color==1){
                line2 = mMap.addPolyline(lineOptions);
                color=0;
            }

        }
    }

    public String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + GoogleMapsAPIKey;

        return url;
    }

    /**
     * A method to download json data from url
     */
    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    /** Old code**/

   /* @Override
    public void onMapReady(GoogleMap googleMap) {

        //Creating the polyline of the driver's route
        PolylineOptions po = new PolylineOptions();
        List<MyPoint> rp = ride.getRoutePoints();

        for(int i=0; i<rp.size();i++){
            po.add(new LatLng(rp.get(i).lat,rp.get(i).lng));
        }

        po.color(R.color.polylineAzur).width(10);

        Polyline polyline = googleMap.addPolyline(po.clickable(true));

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(rp.get(0).lat,rp.get(0).lng), 12));

        //Setting the passenger markers (starting point and ending point)
        LatLng passenger_starting_point = new LatLng(startingPoint.getLat(),startingPoint.getLng());
        googleMap.addMarker(new MarkerOptions().position(passenger_starting_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Your origin point"));

        LatLng passenger_ending_point = new LatLng(endingPoint.getLat(),endingPoint.getLng());
        googleMap.addMarker(new MarkerOptions().position(passenger_ending_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Your destination point"));


        //Setting the meeting points markers (starting point and ending point)
        int seconds_from_start = (int) ride.getClosestPointStart().getSeconds_from_start()/60;
        LatLng meeting_point = new LatLng(meetingPoint.getLat(),meetingPoint.getLng());
        googleMap.addMarker(new MarkerOptions().position(meeting_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Meeting point with the driver").snippet("The driver arrives at "+ getIntent().getStringExtra("meetingTime")));

        LatLng dropping_point = new LatLng(droppingPoint.getLat(),droppingPoint.getLng());
        googleMap.addMarker(new MarkerOptions().position(dropping_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Dropping point").snippet("The driver drops you at "+ getIntent().getStringExtra("droppingTime")));

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { } */
}
