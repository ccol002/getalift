<<<<<<< HEAD
package mt.edu.um.getalift;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

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

import java.util.List;

public class CreateRideActivity extends AppCompatActivity implements
        OnMapReadyCallback{

    private MyPoint startingPoint;
    private MyPoint endingPoint;

    private Button btn_create_ride;
    private Button btn_edit_ride;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_create_ride);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.create_ride_map);
        mapFragment.getMapAsync(this);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_create_map);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerStartingPointLat",0.0),getIntent().getDoubleExtra("passengerStartingPointLng",0.0),0,0);
        endingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerEndingPointLat",0.0),getIntent().getDoubleExtra("passengerEndingPointLng",0.0),0,0);

        btn_create_ride = findViewById(R.id.btn_create_ride);
        btn_create_ride.setText(R.string.btn_create_ride);

        btn_edit_ride = findViewById(R.id.btn_edit_ride);
        btn_edit_ride.setText(R.string.btn_edit_ride);

        setTitle("Create your route");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startingPoint.getLat(),startingPoint.getLng()), 12));

        //Setting the passenger markers (starting point and ending point)
        LatLng passenger_starting_point = new LatLng(startingPoint.getLat(),startingPoint.getLng());
        googleMap.addMarker(new MarkerOptions().position(passenger_starting_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Your origin point"));

        LatLng passenger_ending_point = new LatLng(endingPoint.getLat(),endingPoint.getLng());
        googleMap.addMarker(new MarkerOptions().position(passenger_ending_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Your destination point"));

    }
}
=======
package mt.edu.um.getalift;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

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

import java.util.List;

public class CreateRideActivity extends AppCompatActivity implements
        OnMapReadyCallback{

    private MyPoint startingPoint;
    private MyPoint endingPoint;

    private Button btn_create_ride;
    private Button btn_edit_ride;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_create_ride);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.create_ride_map);
        mapFragment.getMapAsync(this);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_create_map);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerStartingPointLat",0.0),getIntent().getDoubleExtra("passengerStartingPointLng",0.0),0,0);
        endingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerEndingPointLat",0.0),getIntent().getDoubleExtra("passengerEndingPointLng",0.0),0,0);

        btn_create_ride = findViewById(R.id.btn_create_ride);
        btn_create_ride.setText(R.string.btn_create_ride);

        btn_edit_ride = findViewById(R.id.btn_edit_ride);
        btn_edit_ride.setText(R.string.btn_edit_ride);

        setTitle("Create your route");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startingPoint.getLat(),startingPoint.getLng()), 12));

        //Setting the passenger markers (starting point and ending point)
        LatLng passenger_starting_point = new LatLng(startingPoint.getLat(),startingPoint.getLng());
        googleMap.addMarker(new MarkerOptions().position(passenger_starting_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Your origin point"));

        LatLng passenger_ending_point = new LatLng(endingPoint.getLat(),endingPoint.getLng());
        googleMap.addMarker(new MarkerOptions().position(passenger_ending_point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Your destination point"));

    }
}
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
