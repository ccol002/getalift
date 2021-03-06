package mt.edu.um.getalift;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
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
    private FloatingActionButton btn_create_route_confirm;
    private FloatingActionButton btn_edit_ride;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private String date;
    private String time;
    private int userID;
    private String originAddress;
    private String destinationAddress;


    Intent intentCreateRideInfo;
    private double newStartingPointLat;
    private double newStartingPointLng;
    private double newEndingPointLat;
    private double newEndingPointLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride_info);
        setTitle(getString(R.string.title_activity_create_ride_info));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TextViews
        txt_origin_address = (TextView) findViewById(R.id.txt_origin_address);
        txt_destination_address = (TextView) findViewById(R.id.txt_destination_address);
        txt_distance_route = (TextView) findViewById(R.id.txt_distance_route);
        txt_duration = (TextView) findViewById(R.id.txt_duration);
        btn_create_route_confirm = (FloatingActionButton)  findViewById(R.id.btn_create_route_confirm);
        btn_edit_ride = (FloatingActionButton)  findViewById(R.id.btn_edit_ride);
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

        int currentHour = mTimePicker.getCurrentHour()+2;
        int currentMinute = mTimePicker.getCurrentMinute();
        Log.i("TAG_CURRENT_TIME",currentHour +":"+currentMinute);

        //Do not work with the Android phone (API level 22) but it seems
        //mTimePicker.setHour(currentHour);
        //mTimePicker.setMinute(currentMinute);

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
            originAddress = intentCreateRideInfo.getStringExtra("originAddress");
            destinationAddress = intentCreateRideInfo.getStringExtra("destinationAddress");

            //Display the 2 addresses (origin and destination)
            txt_origin_address.setText(originAddress);
            txt_destination_address.setText(destinationAddress);
        }
        Log.i("TAG_User", Integer.toString(userID));


        //Calculate Distance approximately between the 2 points : to display it's not to add it as parameters in the database (the request does it)
        //To display the info
        float[] distance_array = new float[1];
        double distance = calculateDistance(newStartingPointLat, newStartingPointLng,
                newEndingPointLat, newEndingPointLng, distance_array);

        //Calculate Time approximately between the 2 points: to display it's not to add it as parameters in the database (the request does it)
        int speedIs1KmMinute = 100;
        int estimatedDriveTimeInMinutes = (int) distance / speedIs1KmMinute;
        txt_duration.setText(getString(R.string.txt_duration)+" : "+ estimatedDriveTimeInMinutes +" min");


    btn_create_route_confirm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createRoute();
        }
    });
    btn_edit_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
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
                        AlertCall(getString(R.string.txt_route_created_notification));
                        //Toast.makeText(getApplicationContext(), "Your route has been created, you can see it in \"My routes\"",Toast.LENGTH_LONG).show();
                        //NavUtils.navigateUpFromSameTask(activity);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "Unable to add the route into the database",Toast.LENGTH_SHORT).show();
                        Log.d("GetALift", error.toString());
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                headers.put("x-access-token", sh.getString("token", null));
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


            public double calculateDistance(double startLat, double startLng, double endLat, double endLng, float[] resultArray ){
                //To round the values
                NumberFormat nf = new DecimalFormat("0.#");
                NumberFormat nff = new DecimalFormat("0");
                float distance_round_km;
                //String s = nf.format(monNombre);

                //Calculate the distance between the two points

                Location.distanceBetween(startLat, startLng,
                        endLat, endLng, resultArray);
                float distance = resultArray[0];
                String distance_round = nf.format(distance);
                String distance_round_km_string;
                if(distance_round.length() > 3){
                    distance_round_km = distance/1000;
                    distance_round_km_string = nf.format(distance_round_km);

                txt_distance_route.setText(getString(R.string.txt_distance)+"  :  " + distance_round_km_string+" km");}
                else{
                    txt_distance_route.setText(getString(R.string.txt_distance)+"  :  " + distance_round+" m");
                }

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

    public void AlertCall(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.txt_new_notification))
                .setIcon(R.drawable.ic_notifications)
                .setMessage(message)
                .setPositiveButton(getString(R.string.txt_okay),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Intent intent = new Intent(getApplicationContext(), HomeMapActivity.class);
                        startActivity(intent);
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}

