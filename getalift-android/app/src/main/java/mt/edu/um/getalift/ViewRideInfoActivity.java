package mt.edu.um.getalift;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewRideInfoActivity extends AppCompatActivity {

    //Creation of the intent which recovers the id of the driver selected by the user
    Intent intent_View_Ride_Info_activity;
    private int driverId;

    //Tag for the LOG
    private static final String TAG = "ViewRideInfoActivity";
    
    private TextView txt_driver_phoneNumber;
    private TextView txt_driver_email;
    private TextView txt_driver_name;
    private TextView txt_view_starting_point;
    private TextView txt_view_ending_point;
    private ImageButton img_button_tel;
    private String phoneNumber;

    private String text_message;

    private Button btn_confirm_ride;
    private MyPoint startingPoint;
    private MyPoint endingPoint;
    private int routeId;
    private int userID;
    private int ride_id;
    private String driver_email;
    private ImageButton img_button_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ride_info);
        setTitle("Ride's Info");

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //We assimilate the variables created above with the Id of the layout
        txt_driver_phoneNumber = findViewById(R.id.txt_driver_phoneNumber);
        txt_driver_email = findViewById(R.id.txt_driver_email);
        txt_driver_name = findViewById(R.id.txt_driver_name);
        txt_view_starting_point = findViewById(R.id.txt_start_point);
        txt_view_ending_point= findViewById(R.id.txt_end_point);
        img_button_tel = findViewById(R.id.img_btn_tel);
        img_button_mail = findViewById(R.id.img_button_mail);

        //edt_message = findViewById(R.id.edt_passenger_message);

        btn_confirm_ride = findViewById(R.id.btn_confirm_ride);

        // Recovering the ride selected
        intent_View_Ride_Info_activity = getIntent();
        if (intent_View_Ride_Info_activity != null) {
            driverId = intent_View_Ride_Info_activity.getIntExtra("driver_id",0);
            startingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerStartingPointLat",0.0),getIntent().getDoubleExtra("passengerStartingPointLng",0.0),0,0);
            endingPoint = new MyPoint(0,getIntent().getDoubleExtra("passengerEndingPointLat",0.0),getIntent().getDoubleExtra("passengerEndingPointLng",0.0),0,0);
            routeId = intent_View_Ride_Info_activity.getIntExtra("route_id",0);
            userID = intent_View_Ride_Info_activity.getIntExtra("userID",0);
            txt_view_ending_point.setText("UserID :" + userID);
            txt_view_starting_point.setText("Route ID : "+ routeId);
            }


            //Recover address of the starting point

        String adresseOrigin = getAddressFromLocation(startingPoint.getLat(),startingPoint.getLng(),getApplicationContext());
        String adresseDestination = getAddressFromLocation(endingPoint.getLat(),endingPoint.getLng(),getApplicationContext());
                txt_view_starting_point.setText(adresseOrigin);
                txt_view_ending_point.setText(adresseDestination);

        //Complete the fields with the info of the driver
        completeDriverInfo();

        //Add ride and the passenger to the ride by clicking on this button
        btn_confirm_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*text_message = edt_message.getText().toString().trim();
                if(text_message.length() == 0){
                    addRide();
                }
                else {
                     if (text_message.length() > 100){
                        Toast.makeText(getApplicationContext(), getString(R.string.error_message_20_long), Toast.LENGTH_SHORT).show();
                    } else if (text_message.length() < 20){
                        Toast.makeText(getApplicationContext(), getString(R.string.error_message_20_short), Toast.LENGTH_SHORT).show();
                    }
                    else{*/
                         addRide();

                //We transform the TextView into


            }
        });

        img_button_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 0" + phoneNumber));
                Log.i("TAG_phone_call", "Passer appel avec : " + phoneNumber);
                startActivity(call);
            }
        });

        img_button_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }

/**When the user click on "Confirm", the app creates a ride corresponding to the route selected before
    To add the route to the ride table in the database :*/
    private void addRide(){
        final int route_id = routeId ;

        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/rides/";

        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("GetALift_1", response);
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONObject(response);
                            // If the server respond with a success...
                            if (jo.getBoolean("success")){
                                // We tell the user his ride is now created...
                                Toast.makeText(getApplicationContext(), "Route added to your rides", Toast.LENGTH_SHORT).show();
                                //Get the ride_Id created
                                ride_id = jo.getInt("insertId");

                                //Add the user as a passenger in this ride
                                createPassenger(ride_id);
                            } else {
                                if (jo.getInt("errorCode") == 1){
                                    Log.i("TAG_ErrorCode_1", "on rentre dans error code");
                                    // We tell the user his account is now created...
                                    Toast.makeText(getApplicationContext(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                                    addUserToExistingRide(route_id);
                               }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getBaseContext(),"Error !", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {

           @Override
            public Map<String, String> getHeaders()/* throws AuthFailureError*/ {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo");
                //headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            //Parameters for the SQL request, we need all the information
            protected Map<String, String> getParams()/* throws AuthFailureError*/ {
                Map<String, String> params = new HashMap<String, String>();
                //ride, passenger, inTheCar
                params.put("route",Integer.toString(route_id));

                return params;

            }
        };

        queue.add(putRequest);


    }


    /**When the user click on confirm, the route is added in the ride table
      If the ride already exist, the addRide() send an error with the errorCode==1 and say  "This route is already attributed to a ride"*/
    private void addUserToExistingRide(int route) {
        final int route_id = route;
        final int user_id = userID ;
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/passenger/existingRide";

        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("GetALift_2_add_existing", response);
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONObject(response);
                            // If the server respond with a success...
                            if (jo.has("insertId")) {
                                Log.i("GetALift_2_error", jo.getString("serverStatus"));
                                //Toast.makeText(getApplicationContext(),"You have been added as a passenger on the route selected, you can see it in your lifts",Toast.LENGTH_LONG).show();
                                AlertCall(getString(R.string.txt_passenger_added_notification));
                                //Come back to the home page
                                //NavUtils.navigateUpTo(activity, getParentActivityIntent());
                            } else if(!jo.getBoolean("success")) {
                                Log.i("GetALift_2_already", "Already passenger");
                                // We tell the user his ride is now created...
                               // Toast.makeText(getBaseContext(),"You are already passenger on this ride !", Toast.LENGTH_SHORT).show();
                                AlertCall(getString(R.string.txt_passenger_already_added_notification));
                                //Come back to the home page
                               // NavUtils.navigateUpTo(activity, getParentActivityIntent());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getBaseContext(),"Error to add the user in an existing ride !", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() /*throws AuthFailureError*/ {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                //or try with this:
                headers.put("x-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            //Parameters for the SQL request, we need all the information
            protected Map<String, String> getParams() /*throws AuthFailureError */{
                Map<String, String> params = new HashMap<String, String>();
                //ride, passenger, inTheCar
                params.put("routeId",Integer.toString(route_id));
                params.put("passId",Integer.toString(user_id));

                Log.i("TAG_Route_id", Integer.toString(route_id));
                Log.i("TAG_User_id", Integer.toString(user_id));

                return params;

            }
        };

        queue.add(putRequest);
    }

    /**To add the passenger to the database*/
    private void createPassenger(final int ride_id) {

        //final int route_id = routeId;
        final int user_id = userID ;
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/passenger/";
        final Activity activity = this;

        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("GetALift_3", response.toString());
                        //Toast.makeText(getBaseContext(),"Passenger added to this ride",Toast.LENGTH_LONG).show();
                        //NavUtils.navigateUpTo(activity, getParentActivityIntent());
                        AlertCall(getString(R.string.txt_passenger_added_notification));
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getBaseContext(),"Error !", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                //or try with this:
                headers.put("x-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            //Parameters for the SQL request, we need all the information
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                    //ride, passenger, inTheCar
                params.put("ride",Integer.toString(ride_id));
                params.put("passenger",Integer.toString(user_id));
                params.put("inTheCar","0");

                return params;

            }
        };

        queue.add(putRequest);

    }

    /** Return to the last page when clicking on the arrow*/
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

    /**Complete the fields with the info t=of teh driver*/
    public void completeDriverInfo(){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users/" + Integer.toString(driverId);

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONArray(response).getJSONObject(0);

                            // Display the info of the user
                            Log.i("Test",response);
                            txt_driver_email.setText(jo.getString("email"));
                            driver_email = jo.getString("email");
                            txt_driver_phoneNumber.setText(jo.getString("mobileNumber"));
                            phoneNumber = jo.getString("mobileNumber");
                            txt_driver_name.setText(jo.getString("name")+ " " + jo.getString("surname"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }

                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename), Context.MODE_PRIVATE);
                params.put("x-access-token", sh.getString("token", null));
                return params;
            }
        };
        queue.add(sr);
    }

/**Convert the coordinates into addresses ti display them to the driver*/
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
                    sb.append(address.getAddressLine(i));
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

                if(featureName != street) {
                    sb.append(featureName);
                    sb.append(street);
                    sb.append(locality);
                    sb.append(zip);
                    sb.append(country);
                }
                else{
                    sb.append(street);
                    sb.append(locality);
                    sb.append(zip);
                    sb.append(country);
                }
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

    public void AlertCall(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.txt_new_notification))
                .setIcon(R.drawable.ic_notifications)
                .setMessage(message)
                .setPositiveButton("Okay",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Intent intent = new Intent(getApplicationContext(), HomeMapActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.txt_cancel),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /** To send a mail to the driver by clicking on the icon */
    public void sendMail(){
        //send message to the email of the enterprise
        String emailList [] = {driver_email}; //TO change with the good address
        Intent intentMail = new Intent(Intent.ACTION_SEND);
        intentMail.setType("message/rfc822");
        intentMail.putExtra(Intent.EXTRA_EMAIL, emailList);
        startActivity(Intent.createChooser(intentMail, " "));

    }


}
