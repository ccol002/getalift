package mt.edu.um.getalift;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateRideInfo extends AppCompatActivity {

    private TextView txt_origin_address;
    private TextView txt_destination_address;
    private TextView txt_distance_route;
    private TextView txt_duration;
    private Button btn_create_route_confirm;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private String date;
    private String time;
    private int userID;
    private String originAddress;
    private String destinationAddress;
    private String adresse = "SAlut addresse";

    Intent intentCreateRideInfo;
    private double newStartingPointLat;
    private double newStartingPointLng;
    private double newEndingPointLat;
    private double newEndingPointLng;

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);


        //Recover the ID of the user (who becomes the driverId in this activity
        userID = getIntent().getIntExtra("userID",0);

        //Get the date of the day
        Calendar rightNow = Calendar.getInstance();

        //Initialise the DatePicker to display the date of the day
        int currentYear = rightNow.get(Calendar.YEAR);
        int currentMonth = rightNow.get(Calendar.MONTH)+1;
        int currentDayOfTheMonth = rightNow.get(Calendar.DAY_OF_MONTH);

        //To not have null if teh user doesn't change the date
        date = currentYear+"-"+currentMonth+"-"+currentDayOfTheMonth;
        Log.i("TAG_DATE_CURRENT", date);
        mDatePicker.init( currentYear, currentMonth-1,currentDayOfTheMonth , new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                Log.i("TAG_DATE_NEW", date);
            }
        });
       /* int hours = rightNow.get(Calendar.HOUR_OF_DAY) +2;
        int minutes = rightNow.get(Calendar.MINUTE);
        Log.i("TAG_TIME", Integer.toString(hours)+":"+ Integer.toString(minutes));*/


        int currentHour = mTimePicker.getCurrentHour()+2;
        int currentMinute = mTimePicker.getCurrentMinute();
        Log.i("TAG_CURRENT_TIME",currentHour +":"+currentMinute);

        mTimePicker.setHour(currentHour);
        mTimePicker.setMinute(currentMinute);

        //To not have null if the user doesn't change the hour
        time=currentHour+":"+currentMinute+":00";
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String minuteDouble, hoursDouble;
                //to have the good format for the request
                if(minute <10){
                    minuteDouble = "0"+minute;
                }
                else
                    minuteDouble = Integer.toString(minute);

                if(hourOfDay <10){
                    hoursDouble = "0"+hourOfDay;
                }
                else
                    hoursDouble = Integer.toString(hourOfDay);
                time = hoursDouble +":"+minuteDouble+":00";
                Log.i("TAG_TIME_new_listener",time);
            }
        });

        //Recover the LatLat points from the last page
        intentCreateRideInfo =getIntent();
        if(intentCreateRideInfo != null){
            newStartingPointLat = intentCreateRideInfo.getDoubleExtra("newStartingPointLat",0.0);
            newStartingPointLng = intentCreateRideInfo.getDoubleExtra("newStartingPointLng",0.0);
            newEndingPointLat = intentCreateRideInfo.getDoubleExtra("newEndingPointLat",0.0);
            newEndingPointLng = intentCreateRideInfo.getDoubleExtra("newEndingPointLng",0.0);

            //Display the 2 addresses (origin and destination)
            originAddress = getAddressFromLocation(newStartingPointLat,newStartingPointLng,this);
            destinationAddress = getAddressFromLocation(newEndingPointLat,newEndingPointLng,this);
            txt_origin_address.setText(originAddress);
            txt_destination_address.setText(destinationAddress);
        }
        Log.i("TAG_User", Integer.toString(userID));


        //Calculate Distance aproximatively between the 2 points : to display it's not to add it as parameters in the database (the request does it)
        float[] distance_array = new float[1];
        double distance = calculateDistance(newStartingPointLat, newStartingPointLng,
                newEndingPointLat, newEndingPointLng, distance_array);

        //Calculate Time aproximatively between the 2 points: to display it's not to add it as parameters in the database (the request does it)
        int speedIs1KmMinute = 100;
        int estimatedDriveTimeInMinutes = (int) distance / speedIs1KmMinute;
        txt_duration.setText("Duration of the ride : " + estimatedDriveTimeInMinutes +" min");


    btn_create_route_confirm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createRoute();
            //To create the ride corresponding to the route to be able to add a passenger after (parameters ride_id to ad a passenger and now, we have just the route_id)
            createRide();
        }
    });

    } //end of OnCreate


    //To add the route to the database
    private void createRoute() {
            // We first setup the queue for the API Request
            RequestQueue queue = Volley.newRequestQueue(this);
            // We get the URL of the server.
            String url = ConnectionManager.SERVER_URL+"/api/routes";

            final Activity activity = this;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("TAG_Response", response);
                        Toast.makeText(getApplicationContext(), "Your route has been created, you can see it in \"My routes\"",Toast.LENGTH_SHORT).show();
                        NavUtils.navigateUpFromSameTask(activity);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "Unable to add the route to the database",Toast.LENGTH_SHORT).show();
                        Log.d("GetALift", error.toString());
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                //or try with this:
                headers.put("x-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }


            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String> ();
                params.put("startLat",Double.toString(newStartingPointLat));
                params.put("startLng",Double.toString(newStartingPointLng));
                params.put("endLat",Double.toString(newEndingPointLat));
                params.put("endLng",Double.toString(newEndingPointLng));
                params.put("driverId",Integer.toString(userID));
                params.put("origin",originAddress);
                params.put("destination",destinationAddress);
                params.put("dates",date+";"+time);

                Log.i("TAG_create_startLat",Double.toString(newStartingPointLat));
                Log.i("TAG_create,start_lng",Double.toString(newStartingPointLng));
                Log.i("TAG_create_end_lat",Double.toString(newEndingPointLat));
                Log.i("TAG_create_end_lng",Double.toString(newEndingPointLng));
                Log.i("TAG_create_user",Integer.toString(userID));
                Log.i("TAG_create_origin",originAddress);
                Log.i("TAG_create_destination",destinationAddress);
                Log.i("TAG_create_date_time",date+";"+time);

                return params;
            }

        };

        queue.add(putRequest);
            }

            //to add the route in the Ride table in teh database
    private void createRide() {

    }

//Convert the GPS coordinates into addresses to display it to the user
    public static String getAddressFromLocation(final double latitude, final double longitude, final Context context) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result=null;
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
                       /* if(address.getFeatureName() != null)
                            featureName = address.getFeatureName() +", ";
                        else
                            featureName ="";*/

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

