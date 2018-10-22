package mt.edu.um.getalift;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

import static mt.edu.um.getalift.R.*;

public class CreateRideActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String GAME_STATE_KEY = "CreateRideActivity";
    private String mGameState;

    private MyPoint startingPoint;
    private MyPoint endingPoint;
    private int userID;


    private LatLng newStartingPoint;
    private LatLng newEndingPoint;

    private Intent intentCreateRide;

    private Button btn_create_ride;
    private Button btn_edit_ride;

    private GoogleMap mMap;
    ArrayList markerPoints= new ArrayList();
    private String GoogleMapsAPIKey = "AIzaSyAVVmg3hP70Yj7j1ND3MQuD2_gdeFYrouY";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(layout.activity_create_ride);
        setTitle("Create your route");

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(id.create_ride_map);
        mapFragment.getMapAsync(this);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(id.tlb_create_map);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // recovering the instance state
        if (savedInstanceState != null) {
            mGameState = savedInstanceState.getString(GAME_STATE_KEY);
        }

        userID = getIntent().getIntExtra("userID",0);
        //Create the starting and the ending points to use
        startingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerStartingPointLat",0.0),getIntent().getDoubleExtra("passengerStartingPointLng",0.0),0,0);
        endingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerEndingPointLat",0.0),getIntent().getDoubleExtra("passengerEndingPointLng",0.0),0,0);

        newStartingPoint = new LatLng(startingPoint.getLat(),startingPoint.getLng());
        newEndingPoint = new LatLng(endingPoint.getLat(),endingPoint.getLng());


        Log.i("TAG_START", startingPoint.getLng().toString());
        Log.i("TAG_END", endingPoint.getLng().toString());

        btn_create_ride = findViewById(id.btn_create_ride);
        btn_create_ride.setText(string.btn_create_ride);

        btn_edit_ride = findViewById(id.btn_edit_ride);
        btn_edit_ride.setText(string.btn_edit_ride);




    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(GAME_STATE_KEY, mGameState);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Recover the map
        mMap = googleMap;

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startingPoint.getLat(),startingPoint.getLng()), 12));

        //Setting the passenger markers (starting point and ending point)
        LatLng passenger_starting_point = new LatLng(startingPoint.getLat(),startingPoint.getLng());
        mMap.addMarker(new MarkerOptions().position(passenger_starting_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                .title("Your origin point"));

        LatLng passenger_ending_point = new LatLng(endingPoint.getLat(),endingPoint.getLng());
        mMap.addMarker(new MarkerOptions().position(passenger_ending_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                .title("Your destination point"));

        //When the user click on "edit" it shows a message and he can create a route
        btn_edit_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),getString(string.txt_edit_create_route), Toast.LENGTH_LONG).show();
                onMapClick(view);
            }
        });

        btn_create_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreatedRouteInfo = new Intent(getApplicationContext(),CreateRideInfo.class);
                intentCreatedRouteInfo.putExtra("newStartingPointLat", newStartingPoint.latitude);
                intentCreatedRouteInfo.putExtra("newStartingPointLng", newStartingPoint.longitude);
                intentCreatedRouteInfo.putExtra("newEndingPointLat",newEndingPoint.latitude);
                intentCreatedRouteInfo.putExtra("newEndingPointLng",newEndingPoint.longitude);
                intentCreatedRouteInfo.putExtra("userID",userID);
                startActivity(intentCreatedRouteInfo);
            }
        });

    }


    public void onMapClick(View view) {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                markerPoints.add(latLng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(latLng);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    options.title("New origin Point");
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    options.title("New Destination Point");
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (markerPoints.size() >= 2) {
                    LatLng origin = (LatLng) markerPoints.get(0);
                    LatLng dest = (LatLng) markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    //Stock the new starting and ending points
                    newStartingPoint = origin;
                    newEndingPoint = dest;

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }

            }
        });
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

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

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

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
    private String downloadUrl(String strUrl) throws IOException {
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
}


