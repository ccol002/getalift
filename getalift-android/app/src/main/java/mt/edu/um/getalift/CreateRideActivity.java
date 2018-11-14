package mt.edu.um.getalift;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.Locale;

import static mt.edu.um.getalift.HomeMapActivity.GoogleMapsAPIKey;
import static mt.edu.um.getalift.R.*;

/**  Created by Thessalène JEAN-LOUIS **/

public class CreateRideActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MyPoint startingPoint;
    private MyPoint endingPoint;
    private int userID;

    private GoogleMap mMap;
    private LatLng origin;
    private LatLng destination;
    private String adressOrigin;
    private String adressDestination;
    private Polyline line;
    private FloatingActionButton btn_create_ride;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(layout.activity_create_ride);
        setTitle(getString(R.string.title_activity_create_ride));

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(id.create_ride_map);
        mapFragment.getMapAsync(this);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(id.tlb_create_map);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_create_ride = findViewById(id.btn_create_ride);

        userID = getIntent().getIntExtra("userID",0);
        //Create the starting and the ending points to use
        startingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerStartingPointLat",0.0),getIntent().getDoubleExtra("passengerStartingPointLng",0.0),0,0);
        endingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerEndingPointLat",0.0),getIntent().getDoubleExtra("passengerEndingPointLng",0.0),0,0);

        Log.i("TAG_START", startingPoint.getLng().toString());
        Log.i("TAG_END", endingPoint.getLng().toString());


        View view = findViewById(android.R.id.content);
        Snackbar.make(view, getString(R.string.txt_edit_create_route) + "   \n \n", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Recover the map
        mMap = googleMap;

        // Position the map's camera near the starting point,
        // and set the zoom factor so most of Australia shows on the screen.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startingPoint.getLat(),startingPoint.getLng()), 12));

        //Setting the route markers (starting point and ending point)

        LatLng passenger_starting_point = new LatLng(startingPoint.getLat(),startingPoint.getLng());
        origin = passenger_starting_point;
        adressOrigin = getAddressFromLocation(origin.latitude, origin.longitude, this);
        Marker marker1 = mMap.addMarker(new MarkerOptions()
                .position(origin)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .snippet(adressOrigin)
                .title(getString(string.txt_new_origin_point)));

        LatLng passenger_ending_point = new LatLng(endingPoint.getLat(),endingPoint.getLng());
        destination = passenger_ending_point;
        adressDestination= getAddressFromLocation(destination.latitude, destination.longitude, this);
        Marker marker2 = mMap.addMarker(new MarkerOptions()
                .position(destination)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .snippet(adressDestination)
                .title(getString(string.txt_new_destination_point)));

        //To draw the route between the two markers
        DownloadTask downloadTask = new DownloadTask();
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, destination);
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        //We make the markers drawable to make teh user able to edit the route
        marker1.setDraggable(true);
        marker2.setDraggable(true);
        //A tag to know which marker has been dragged
        marker1.setTag("marker1"); //origin
        marker2.setTag("marker2"); // Destination
        mMap.setOnMarkerDragListener(dragmarker);

        //Transfer of the info to add them to the database in the following page
        btn_create_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreatedRouteInfo = new Intent(getApplicationContext(),CreateRideInfo.class);
                intentCreatedRouteInfo.putExtra("newStartingPointLat", origin.latitude);
                intentCreatedRouteInfo.putExtra("newStartingPointLng", origin.longitude);
                intentCreatedRouteInfo.putExtra("newEndingPointLat",destination.latitude);
                intentCreatedRouteInfo.putExtra("newEndingPointLng",destination.longitude);
                intentCreatedRouteInfo.putExtra("originAddress",adressOrigin);
                intentCreatedRouteInfo.putExtra("destinationAddress",adressDestination);
                intentCreatedRouteInfo.putExtra("userID",userID);
                startActivity(intentCreatedRouteInfo);
            }
        });

    }

    public GoogleMap.OnMarkerDragListener dragmarker = new GoogleMap.OnMarkerDragListener() {

        @Override
        public void onMarkerDrag(Marker marker) {
            if(line != null){
                line.remove();
            }

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            //We want to know which marker has been dragged
            Object tag = marker.getTag();

            //The new position after dragging
            LatLng dragPosition = marker.getPosition();
            //arg0.setPosition(dragPosition);

            DownloadTask downloadTask = new DownloadTask();

            if(tag == "marker1") {
                origin = dragPosition;
                adressOrigin = getAddressFromLocation(origin.latitude, origin.longitude, getApplicationContext());
                marker.setSnippet(adressOrigin);
            }else if (tag == "marker2"){
                destination = dragPosition;
                adressDestination = getAddressFromLocation(destination.latitude, destination.longitude, getApplicationContext());
                marker.setSnippet(adressDestination);
            }

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, destination);
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
            marker.showInfoWindow();

        }

        @Override
        public void onMarkerDragStart(Marker marker) {
            marker.hideInfoWindow();
        }
    };

    public class DownloadTask extends AsyncTask<String, Void, String> {

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
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            line = mMap.addPolyline(lineOptions);
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

    //Convert the GPS coordinates into addresses to display it to the user
    public static String getAddressFromLocation(final double latitude, final double longitude, final Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result=null;
        String locality, zip, country, street, featureName;

        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)); //.append("\n");
                }
                //Make sure the information for the address are not null before displaying them
                if(address.getLocality() != null)
                    locality = address.getLocality() +", ";
                else
                    locality = "";
                if(address.getPostalCode() != null)
                    zip = address.getPostalCode() + ", ";
                else
                    zip ="";
                if(address.getCountryName() != null)
                    country = address.getCountryName();
                else
                    country = "";
                if(address.getThoroughfare() != null)
                    street = address.getThoroughfare() +", ";
                else
                    street ="";
                if(address.getFeatureName() != null && address.getFeatureName()!= street && address.getFeatureName()!= locality )
                    featureName = address.getFeatureName() +", ";
                else
                    featureName ="";

                //sb.append(featureName);  //Doublons
                sb.append(street);
                sb.append(locality);
                sb.append(zip);
                sb.append(country);

                result = sb.toString();
            }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        } finally {

            if (result == null) {
                result = " Unable to get address for this location.";
            }

        }
        return result;
    }

}


