package mt.edu.um.getalift;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private TextView txt_driver_id;
    private TextView txt_driver_phoneNumber;
    private TextView txt_driver_email;
    private TextView txt_driver_name;
    private TextView txt_view_starting_point;
    private TextView txt_view_ending_point;

    private Button btn_confirm_ride;
    private MyPoint startingPoint;
    private MyPoint endingPoint;
    private int routeId;
    private int userID;

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
        //txt_driver_id = (TextView) findViewById(R.id.txt_driver_name);
        txt_driver_phoneNumber = findViewById(R.id.txt_driver_phoneNumber);
        txt_driver_email = findViewById(R.id.txt_driver_email);
        txt_driver_name = findViewById(R.id.txt_driver_name);
        txt_view_starting_point = findViewById(R.id.txt_start_point);
        txt_view_ending_point= findViewById(R.id.txt_end_point);

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
            /*try {
                txt_view_starting_point.setText(onHandleIntent(intent_View_Ride_Info_activity, startingPoint));
                txt_view_ending_point.setText(onHandleIntent(intent_View_Ride_Info_activity, endingPoint));
            } catch (IOException e) {
                e.printStackTrace();
            }*/


        //Complete the fields with the info of the driver
        completeDriverInfo();

        btn_confirm_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPassenger();
                //RecoverRideId();
            }
        });

    }


    private void RecoverRideId(){
        final int route_id = routeId ;
        final String[] ride = new String[1];
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/rides/";
        final Activity activity = this;

        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        ride[0] =response;
                        Toast.makeText(getBaseContext(),"Ride ID Trouvée",Toast.LENGTH_SHORT).show();
                        NavUtils.navigateUpFromSameTask(activity);
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
                //headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            //Parameters for the SQL request, we need all the information
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //ride, passenger, inTheCar
                params.put("route",Integer.toString(route_id));

                return params;

            }
        };

        queue.add(putRequest);
        //return ride[0];

    }
    private void createPassenger() {

        final int route_id = routeId;
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
                        // response
                        Log.d("Response", response.toString());
                        Toast.makeText(getBaseContext(),"Vous avez été ajouté en tant que passager, veuillez attendre la confirmation du conducteur",Toast.LENGTH_LONG).show();
                        NavUtils.navigateUpTo(activity, getParentActivityIntent());
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
                params.put("ride","29");
                params.put("passenger",Integer.toString(user_id));
                params.put("inTheCar","0");

                return params;

            }
        };

        queue.add(putRequest);

    }

    // Return to the last page
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

    //Complete the fields with the info t=of teh driver
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
                            txt_driver_phoneNumber.setText(jo.getString("mobileNumber"));
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


  /*  public String onHandleIntent(Intent intent, MyPoint point) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;
        //geocoder = new Geocoder(this.getBaseContext(),Locale.getDefault());

        addresses = geocoder.getFromLocation(point.getLat(), point.getLng(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        return address;
    }*/


}
