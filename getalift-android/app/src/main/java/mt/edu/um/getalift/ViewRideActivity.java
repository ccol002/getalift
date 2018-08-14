<<<<<<< HEAD
package mt.edu.um.getalift;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewRideActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener{

    private Ride ride;
    private MyPoint startingPoint;
    private MyPoint endingPoint;
    private MyPoint meetingPoint;
    private MyPoint droppingPoint;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

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

        startingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerStartingPointLat",0.0),getIntent().getDoubleExtra("passengerStartingPointLng",0.0),0,0);
        endingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerEndingPointLat",0.0),getIntent().getDoubleExtra("passengerEndingPointLng",0.0),0,0);
        meetingPoint = ride.getClosestPointStart();
        droppingPoint = ride.getClosestPointEnd();

        setTitle(ride.getUser_name()+"'s route");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add polylines and polygons to the map. This section shows just
        // a single polyline. Read the rest of the tutorial to learn more.
        /*Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));*/

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
}
=======
package mt.edu.um.getalift;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewRideActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener{

    private Ride ride;
    private MyPoint startingPoint;
    private MyPoint endingPoint;
    private MyPoint meetingPoint;
    private MyPoint droppingPoint;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

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

        startingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerStartingPointLat",0.0),getIntent().getDoubleExtra("passengerStartingPointLng",0.0),0,0);
        endingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerEndingPointLat",0.0),getIntent().getDoubleExtra("passengerEndingPointLng",0.0),0,0);
        meetingPoint = ride.getClosestPointStart();
        droppingPoint = ride.getClosestPointEnd();

        setTitle(ride.getUser_name()+"'s route");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add polylines and polygons to the map. This section shows just
        // a single polyline. Read the rest of the tutorial to learn more.
        /*Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));*/

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
}
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
