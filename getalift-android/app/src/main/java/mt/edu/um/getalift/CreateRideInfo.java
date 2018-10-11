package mt.edu.um.getalift;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CreateRideInfo extends AppCompatActivity {

    private TextView txt_origin_address;
    private TextView txt_destination_address;
    private TextView txt_distance_route;
    private TextView txt_duration;
    private Button btn_create_route_confirm;

    Intent intentCreateRideInfo;
    private double newStartingPointLat;
    private double newStartingPointLng;
    private double newEndingPointLat;
    private double newEndingPointLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride_info);
        setTitle("Your created route's info");

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TextViews
        txt_origin_address = (TextView) findViewById(R.id.txt_origin_address);
        txt_destination_address = (TextView) findViewById(R.id.txt_destination_address);
        txt_distance_route = (TextView) findViewById(R.id.txt_distance_route);
        txt_duration = (TextView) findViewById(R.id.txt_duration);
        btn_create_route_confirm = (Button)  findViewById(R.id.btn_create_route_confirm);


        //Recover the LatLat points from the last page
        intentCreateRideInfo =getIntent();
        if(intentCreateRideInfo != null){
            newStartingPointLat = intentCreateRideInfo.getDoubleExtra("newStartingPointLat",0.0);
            newStartingPointLng = intentCreateRideInfo.getDoubleExtra("newStartingPointLng",0.0);
            newEndingPointLat = intentCreateRideInfo.getDoubleExtra("newEndingPointLat",0.0);
            newEndingPointLng = intentCreateRideInfo.getDoubleExtra("newEndingPointLng",0.0);

           // txt_origin_address.setText(Double.toString(newStartingPointLat)+","+Double.toString(newStartingPointLng));
            //txt_destination_address.setText(Double.toString(newEndingPointLat)+","+Double.toString(newEndingPointLng));

            //Display the 2 addresses (origin and destination)
            txt_origin_address.setText(getAddressFromLocation(newStartingPointLat,newStartingPointLng,this));
            txt_destination_address.setText(getAddressFromLocation(newEndingPointLat,newEndingPointLng,this));
        }


        float[] distance_array = new float[1];
        double distance = calculateDistance(newStartingPointLat, newStartingPointLng,
                newEndingPointLat, newEndingPointLng, distance_array);

        //Calculate Time aproximatively between the 2 points
        int speedIs1KmMinute = 100;
        int estimatedDriveTimeInMinutes = (int) distance / speedIs1KmMinute;
        txt_duration.setText("Duration of the ride : " + estimatedDriveTimeInMinutes +" min");
        //To complete the database with the good unity : sec
        int estimatedDriveTimeInSecondes = estimatedDriveTimeInMinutes * 60;

    btn_create_route_confirm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createRoute();

        }
    });

    } //end of OnCreate

    private void createRoute() {


    }

    public static String getAddressFromLocation(final double latitude, final double longitude, final Context context) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                String locality, zip, country, street, featureName;

                try {
                    List< Address > addressList = geocoder.getFromLocation(latitude, longitude, 1);
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
                        if(address.getFeatureName() != null)
                            featureName = address.getFeatureName() +", ";
                        else
                            featureName ="";

                        sb.append(featureName);
                        sb.append(street);
                        sb.append(locality);
                        sb.append(zip);
                        sb.append(country);
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e("Location Address Loader", "Unable connect to Geocoder", e);
                } finally {

                    if (result != null) {
                        //addressFind = result;
                    } else {
                        result = " Unable to get address for this location.";
                    }

                }
                return result;
            }


            public double calculateDistance(double startLat, double startLng, double endLat, double endLng, float[] resultArray ){
                //To round the values
                NumberFormat nf = new DecimalFormat("0.#");
                NumberFormat nff = new DecimalFormat("0");
                //String s = nf.format(monNombre);

                //Calculate the distance between the two points

                Location.distanceBetween(startLat, startLng,
                        endLat, endLng, resultArray);
                float distance = resultArray[0];
                String distance_round = nf.format(distance);
                txt_distance_route.setText("Distance : " + distance_round +" m");

                return Double.parseDouble(distance_round);
            }

            //Return to the last activity
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // If we select the "Go back" button
                switch (item.getItemId()) {
                    case android.R.id.home:
                        NavUtils.navigateUpFromSameTask(this);
                        return true;
                }
                return super.onOptionsItemSelected(item);
            }
}

