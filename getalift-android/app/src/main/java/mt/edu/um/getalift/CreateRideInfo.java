package mt.edu.um.getalift;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateRideInfo extends AppCompatActivity {

    private TextView txt_origin_address;
    private TextView txt_destination_address;

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

        //Recover the LatLat points from the last page
        intentCreateRideInfo =getIntent();
        if(intentCreateRideInfo != null){
            newStartingPointLat = intentCreateRideInfo.getDoubleExtra("newStartingPointLat",0.0);
            newStartingPointLng = intentCreateRideInfo.getDoubleExtra("newStartingPointLng",0.0);
            newEndingPointLat = intentCreateRideInfo.getDoubleExtra("newEndingPointLat",0.0);
            newEndingPointLng = intentCreateRideInfo.getDoubleExtra("newEndingPointLng",0.0);

           // txt_origin_address.setText(Double.toString(newStartingPointLat)+","+Double.toString(newStartingPointLng));
            //txt_destination_address.setText(Double.toString(newEndingPointLat)+","+Double.toString(newEndingPointLng));

            txt_origin_address.setText(getAddressFromLocation(newStartingPointLat,newStartingPointLng,this));
            txt_destination_address.setText(getAddressFromLocation(newEndingPointLat,newEndingPointLng,this));
        }

    } //end of OnCreate

    public static String getAddressFromLocation(final double latitude, final double longitude, final Context context) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                String addressFind ="";
                try {
                    List< Address > addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)); //.append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e("Location Address Loader", "Unable connect to Geocoder", e);
                } finally {

                    if (result != null) {
                        addressFind = result;
                    } else {
                        addressFind = " Unable to get address for this location.";
                    }

                }
                return addressFind;
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

