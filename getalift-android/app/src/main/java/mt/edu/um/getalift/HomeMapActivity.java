package mt.edu.um.getalift;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

//Google maps places API KEY AIzaSyD7ElaUB44FMGItFL3H6RbB7G-R8kJUAWI

public class HomeMapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "HomeMapActivity";

    private final static int MY_ACCESS_PERMISSION_CODE = 1;
    private final static String GoogleMapsAPIKey = "AIzaSyAVVmg3hP70Yj7j1ND3MQuD2_gdeFYrouY";
    //AIzaSyCEOfYboyKL1Wb8R04sIFPFPKtxzTQG7M0  AIzaSyAVVmg3hP70Yj7j1ND3MQuD2_gdeFYrouY

    public static String ORIGIN = "nul";
    public static String DESTINATION = "nul";

    private int user_id;

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    private View mapsView;
    private View formView;
    private int mShortAnimationDuration;

    private Button BtnHomeSearch;
    private TextView showDateView;
    private AutoCompleteTextView mTextSearchOrigin;
    private AutoCompleteTextView mTextSearchDestination;

    private EditText edt_time;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private int currentHour;
    private int currentMinute;

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapater;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(35.777609,14.15109), new LatLng(36.041529,14.619515));

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private FloatingActionButton myFab;

    private boolean isShowingMap;

    private double[] originPoint = {-360,-360};
    private double[] destinationPoint = {-360,-360};

    Intent intent_result_search_activity;

    public HomeMapActivity() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homemap);

        // Get the two views from the activity, and set the form "gone".
        mapsView = findViewById(R.id.frg_homemap);
        formView = findViewById(R.id.form_homemap);

        formView.setVisibility(View.GONE);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        myFab = (FloatingActionButton) findViewById(R.id.fab_switchpanel);
        myFab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                switchView();
            }
        });

        // Get the SharedPreference File
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.msc_shared_pref_filename),
                Context.MODE_PRIVATE
        );

        // Setup the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_homemap);
        setSupportActionBar(toolbar);
        setTitle(R.string.txt_title_homemap);

        //Setup the form text fields
        mTextSearchOrigin = (AutoCompleteTextView) findViewById(R.id.edt_home_origin);
        mTextSearchDestination = (AutoCompleteTextView) findViewById(R.id.edt_home_destination);

        // Setup the Drawer Navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_homemap);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        // Setting the user info in the navbar
        try {
            JSONObject user = new JSONObject(sharedPreferences.getString(getString(R.string.msc_saved_user), null));

            ((TextView) headerView.findViewById(R.id.txt_homenav_name)).setText(user.getString("name")+" "+user.getString("surname"));
            ((TextView) headerView.findViewById(R.id.txt_homenav_email)).setText(user.getString("email"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Setup the boolean isShowingMap
        isShowingMap = true;

        // Setup the Maps fragment.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.frg_homemap);
        mapFragment.getMapAsync(this);

        // Get the last known location of the user
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        BtnHomeSearch = (Button) findViewById(R.id.btn_home_search);

        BtnHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startResultSearchActivity();
            }
        });

        edt_time = findViewById(R.id.edt_home_time);
        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(HomeMapActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        edt_time.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });



        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        mPlaceAutocompleteAdapater = new PlaceAutocompleteAdapter(this, googleApiClient, LAT_LNG_BOUNDS, null);

        mTextSearchOrigin.setAdapter(mPlaceAutocompleteAdapater);
        mTextSearchDestination.setAdapter(mPlaceAutocompleteAdapater);

        mTextSearchOrigin.setText("University Of Malta, Msida");
        mTextSearchDestination.setText("Triq San Tumas, Luqa");

        showDateView = findViewById(R.id.edt_home_date);

        intent_result_search_activity =  new Intent(getApplicationContext(), ResultSearchActivity.class);

        SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
        JSONObject user = null;
        try {
            user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
            user_id = user.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Notification to the user if he is driver and he has passengers on his rides
        newPassengerOnMyRides();
    }

    private void newPassengerOnMyRides() {
        RecoverPassengers(user_id);
        Log.i("TAG_user_id", Integer.toString(user_id));
    }


    private void startResultSearchActivity() {
       // getLatLongFromGivenAddress(mTextSearchDestination.getText().toString(),"destination");
        //getLatLongFromGivenAddress(mTextSearchOrigin.getText().toString(),"origin");

        //Convert the address given by the user into Lat and Lng
        double [] lat_lng_origin = getLocationFromAddress(mTextSearchOrigin.getText().toString());
        double [] lat_lng_destination = getLocationFromAddress(mTextSearchDestination.getText().toString());

        originPoint[0] = lat_lng_origin[0];
        originPoint[1] = lat_lng_origin[1];

        destinationPoint[0] =lat_lng_destination[0];
        destinationPoint[1] = lat_lng_destination[1];
        searchRide();
    }

    public double[] getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getApplicationContext());
        List<Address> address;
        double[] tab_latlng ={0.0,0.0};

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            tab_latlng[0] = lat;
            tab_latlng[1] =lng;
            return tab_latlng;
        } catch (Exception e) {
            return null;
        }
    }

    public void switchView() {
        if (isShowingMap){

            mapsView.setAlpha(0f);
            mapsView.setVisibility(View.GONE);

            formView.setAlpha(1f);
            formView.setVisibility(View.VISIBLE);

            myFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_black_24dp));

        } else {

            formView.setAlpha(0f);
            formView.setVisibility(View.GONE);

            mapsView.setAlpha(1f);
            mapsView.setVisibility(View.VISIBLE);

            myFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_search_black_24dp));
        }
        isShowingMap = !isShowingMap;
    }

    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "date");
    }

    public void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        showDateView.setText(format.format(calendar.getTime()));
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static class DatePickerFragment extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

            if (id == R.id.nav_profile) {
          Intent intentProfile = new Intent(getBaseContext(), ProfileActivity.class);
          SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
          try {
              JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
              Log.i("Home",Integer.toString(user.getInt("id"),0));
              intentProfile.putExtra("userId", user.getInt("id"));
              intentProfile.putExtra("canRate",0);
              startActivity(intentProfile);
          } catch (JSONException e) {
              e.printStackTrace();
          }


        } else if (id == R.id.nav_lifts) {

                Intent intentDrive = new Intent(getBaseContext(), DriveActivity.class);
                startActivity(intentDrive);
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                try {
                    JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
                    Log.i("Home",Integer.toString(user.getInt("id"),0));
                    intentDrive.putExtra("userId", user.getInt("id"));
                    startActivity(intentDrive);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        } else if (id == R.id.nav_help) {
                Intent intentHelp = new Intent(getBaseContext(), HelpActivity.class);
                startActivity(intentHelp);
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                try {
                    JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
                    Log.i("Home",Integer.toString(user.getInt("id"),0));
                    intentHelp.putExtra("userId", user.getInt("id"));
                    startActivity(intentHelp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        } else if (id == R.id.nav_settings) {
                Intent intentSettings = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intentSettings);
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                try {
                    JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
                    Log.i("Home",Integer.toString(user.getInt("id"),0));
                    intentSettings.putExtra("userId", user.getInt("id"));
                    intentSettings.putExtra("name", user.getString("name"));
                    intentSettings.putExtra("username", user.getString("username"));
                    intentSettings.putExtra("email", user.getString("email"));
                    intentSettings.putExtra("surname", user.getString("surname"));
                    intentSettings.putExtra("password", user.getString("password"));
                    intentSettings.putExtra("mobileNumber", user.getInt("mobileNumber"));

                    startActivity(intentSettings);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        } else if (id == R.id.nav_logout) {
            // Retrieve the default SharedPreference File
            Context context = getApplicationContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    getString(R.string.msc_shared_pref_filename),
                    Context.MODE_PRIVATE
            );
            // Then, we remove every information about the user.
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(getString(R.string.msc_key_saved_token));
            edit.remove(getString(R.string.msc_saved_user));
            edit.apply();

            // And we open the Login activity, and we close this activity
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                setCameraPosition(location);
                            }
                            else {
                                // Add a marker in the University of Malta
                                float zoomLevel = 16.0f; //This goes up to 21
                                LatLng universityMsida = new LatLng(35.902163, 14.483748);
                                googleMap.addMarker(new MarkerOptions().position(universityMsida).title("Marker in the University of Malta (Msida)"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(universityMsida, zoomLevel));
                            }
                        }
                    });

            this.googleMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_ACCESS_PERMISSION_CODE);
        }
    }

    private void setCameraPosition(Location location){
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case MY_ACCESS_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        this.googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "You must allow position tracking in order to use this app.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        }
    }

    public String parseStringForURL(String str){
        String tmp = "";
        for (int i=0;i<str.length();i++){
            if(str.charAt(i) == ' '){
                tmp += "+";
            }else{
                tmp += str.charAt(i);
            }
        }

        return tmp;
    }

    public void getLatLongFromGivenAddress(String address, final String point) {
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+parseStringForURL(address)+"&key="+GoogleMapsAPIKey;
        /*Log.i(TAG, "STARTING QUERY FOR ADDRESS "+parseStringForURL(address)+"....");
        Log.i(TAG, url);*/
        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response){
                        // We got a response from our server.
                        try {
                            Log.i(TAG,response);
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONObject(response);

                            double lng = ((JSONArray)jo.get("results")).getJSONObject(0)
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lng");

                            double lat = ((JSONArray)jo.get("results")).getJSONObject(0)
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lat");

                            if(point.equals("origin")){
                                originPoint[0] = 35.9021631;
                                originPoint[1] = 14.483748300000002;
                                //originPoint[0] = lat;
                                //originPoint[1] = lng;
                                intent_result_search_activity.putExtra("passengerStartingPointLat", originPoint[0]);
                                intent_result_search_activity.putExtra("passengerStartingPointLng", originPoint[1]);
                            }else{
                                destinationPoint[0] = 35.85411349999999;
                                destinationPoint[1] = 14.48327949999998;
                                //destinationPoint[0] = lat;
                                //destinationPoint[1] = lng;
                                intent_result_search_activity.putExtra("passengerEndingPointLat", destinationPoint[0]);
                                intent_result_search_activity.putExtra("passengerEndingPointLng", destinationPoint[1]);
                            }

                            //If the origin field or the destination field is empty
                            if(destinationPoint[0] > -180 && destinationPoint[1] > -180 && originPoint[0] > -180 && originPoint[1] > -180) {
                                searchRide();
                            }
                        } catch (JSONException e) {
                            Log.i("Erreur",response);
                            Toast.makeText(getApplicationContext(), "Unable to connect to Google Maps API",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }

                }
        );
        queue.add(sr);
    }

    public void searchRide(){
        RequestQueue queue = Volley.newRequestQueue(this);

        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/findTarget";

        // We retrieve the search parameters
       /* final String startLat = Double.toString(originPoint[0]);
        final String startLng = Double.toString(originPoint[1]);
        final String endLat = Double.toString(destinationPoint[0]);
        final String endLng = Double.toString(destinationPoint[1]);*/

       final double startLat = originPoint[0];
       final double startLng = originPoint[1];
       final double endLat = destinationPoint[0];
       final double endLng =destinationPoint[1];
        final String startDate = ((EditText) findViewById(R.id.edt_home_time)).getText().toString().trim();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Intent intent_result_search_activity = new Intent(getBaseContext(), ResultSearchActivity.class);
                        SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                        intent_result_search_activity.putExtra("JSON_RESULT", response);

                        intent_result_search_activity.putExtra("passengerStartingPointLat", startLat);
                        intent_result_search_activity.putExtra("passengerStartingPointLng", startLng);
                        intent_result_search_activity.putExtra("passengerEndingPointLat",endLat);
                        intent_result_search_activity.putExtra("passengerEndingPointLng", endLng);
                        Log.i("TAG_startLat",Double.toString(startLat));
                        Log.i("TAG_startLng",Double.toString(startLng));
                        Log.i("TAG_endLat",Double.toString(endLat));
                        Log.i("TAG_endLng",Double.toString(endLng));

                        try {
                            JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
                            Log.i("Home",Integer.toString(user.getInt("id"),0));

                            intent_result_search_activity.putExtra("userId", user.getInt("id"));
                            //int userID = intent_result_search_activity.getIntExtra("userId", 0);
                            //Log.i("TAG_User",Integer.toString(userID));
                            if(intent_result_search_activity != null)
                            startActivity(intent_result_search_activity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.i(TAG, error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("startLng", Double.toString(startLng));
                params.put("startLat", Double.toString(startLat));
                params.put("endLng", Double.toString(endLng));
                params.put("endLat", Double.toString(endLat));
                params.put("startDate", startDate);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                params.put("x-access-token", sh.getString("token", null));
                return params;
            }
        };
        queue.add(postRequest);
        originPoint[0] = -360;
        originPoint[1] = -360;
        destinationPoint[0] = -360;
        destinationPoint[1] = -360;
    }

    //Find all new Passenger of the current user
    // parameter : id of the current user
    private void RecoverPassengers(int user_id) {

        // We first setup the queue for the API Request
        RequestQueue queue =  Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/passenger/alert/withoutDateVerification/" + Integer.toString(user_id);

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        //Display the response of the server
                        Log.i("TAG_response", response);
                        try {
                            // We create a JSONArray from the server response to have each route where
                            // there is a new passenger of our current user
                            JSONArray joArray =  new JSONArray(response);
                            int size = joArray.length();
                            Log.i("TAG_size_response", "taille : "+size);

                           if(size != 0) {
                               Log.i("TAG_array_null", "array PAS null!!!");
                               for (int i = 0; i < 1; i++) {
                                   JSONObject jo_route = new JSONArray(response).getJSONObject(i);
                                   String passengerName = jo_route.getString("username");
                                   //The id of the line in the database
                                   int passId = jo_route.getInt("id");
                                   Log.i("TAG_passengers", "passenger" + i + " : " + passengerName);

                                   //Recover ride_id where was added the passenger
                                   int ride_id = jo_route.getInt("ride");

                                   //To have the id of the route and after the info chose by the passenger
                                   //Passenger Name : His name to display it in the Alert Call
                                   //passId : the line in the passenger table if the driver do not wants to accept the passenger
                                   //ride_id : to find the route_id then the info of teh route chose by the passenger

                                   //I pass the info in the methods
                                   RouteId_Passenger(passengerName, passId,ride_id);

                               }
                           }
                            else {
                                   Log.i("TAG_array_null", "array null!!!");
                                   Toast.makeText(getApplicationContext(), "You don't have any passenger yet", Toast.LENGTH_SHORT).show();
                                }



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
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        queue.add(sr);
    }

    public void AlertCall(String username, final int passId, String origin, String destination, String date){
        final String name = username;
        final int nb_passId = passId;
        String new_pass, want_to_go, accept, cancel, cannot;

        //To display a date format like : yyyy-mm-dd  hh:mm:ss and not yyyy-mm-ddThh:mm:ss.00Z
        String[] date_converted = date.split("T");
        String time_converted = date_converted[1].substring(0,8);
        Log.i("TAG_time_converted", time_converted);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.txt_new_passenger))
                .setIcon(R.drawable.ic_notifications)
                .setMessage(username + " "+ getString(R.string.txt_want_to_go)+ "!"+ "\n \n "+ getString(R.string.txt_from) +origin +"\n" + getString(R.string.txt_to) + destination +"\n \n Date : "+date_converted[0] + " at : " +time_converted)
                //If the driver is okay to have this user as passenger, he clicks and the app change "inTheCar" in the database into 1
                .setPositiveButton(getString(R.string.txt_accept),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        changeInTheCar(name, nb_passId);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.txt_cannot),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        suppressPassenger(name,passId);
                        dialog.cancel();
                    }
                })
                .setNeutralButton(getString(R.string.txt_cancel),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                })
       ;
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void suppressPassenger(String username, int passId) {
        final int pass_id = passId;
        final String username_supp = username;
        Log.i("TAG_PassId", Integer.toString(passId));
        // We first setup the queue for the API Request
        RequestQueue queue =  Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/passenger/" + Integer.toString(pass_id);

        StringRequest sr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Display the response of the server
                        Log.i("TAG_suppress_pass", response);
                        Toast.makeText(getApplicationContext(), username_supp + " a été retiré de ce tajet !", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG_suppress_error", error.toString());
                    }

                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("x-access-token","eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo");
                return headers;
            }
        };

        queue.add(sr);
    }

    /**When the driver receive the alert whoch says : "user... wants to go with you" and he click on okay
    // it means he wants to take this user as passenger
    //the app change the "inTheCar" into 1 in the database
    // = Confirm that the passenger is accepted by the driver*/
    public void changeInTheCar(String username, int passId){
        final String username_display = username;
        final int passengerId = passId;
        Log.i("TAG_PassId", Integer.toString(passId));
        // We first setup the queue for the API Request
        RequestQueue queue =  Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/passenger/alert/" + Integer.toString(passengerId);

        StringRequest sr = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        //Display the response of the server
                        Log.i("TAG_ChangeInTheCar", response);
                        Toast.makeText(getApplicationContext(), username_display + " a été ajouté à votre tajet !", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }

                }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                //or try with this:
                headers.put("x-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                    params.put("inTheCar", Integer.toString(1));
                    return params;
                }
        };

        queue.add(sr);
    }

    //To have the id of the route the passenger wants to go with the user if he is driver
    //After we make this id in parameter for the fi=unction :     to recover all the information about the route (origin and destination adresses, date)
    public void RouteId_Passenger(String passengerName, int passId, int rideId){
        final int passID = passId;
        final String usernamePassenger = passengerName;
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/rides/" + Integer.toString(rideId);
        Log.i("TAG_rideId_chose",Integer.toString(rideId));

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            Log.i("TAG_Route_NewPassenger",response);
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONArray(response).getJSONObject(0);
                            // Retrieve the route_id of the route the passenger of the driver wants to go
                            int route_id_chose = jo.getInt("route");
                            Log.i("TAG_RouteId_chose",Integer.toString(route_id_chose));
                            RecoverRouteInfo(usernamePassenger, passID, route_id_chose);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG_error_routeIdPass", error.toString());
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

    /**To have all the route where our user is driver
       Then find the one corresponding to the route chose by the new passenger with the routeId_chose in parameter
       So, after we will have the origin and destination Adress and also the time
       It avoids to do 2 requests*/
    private void RecoverRouteInfo(String usernamePassenger, int passId,  int routeId_chose) {
        final int passID = passId;
    final int routeID_chose = routeId_chose;
     final String PassengerUsername = usernamePassenger;

        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/driverroutesdate/" + Integer.toString(user_id);

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            Log.i("TAG_RecoverInfoRoute",response);

                            // We create a JSONArray from the server response to have each route where
                            // there is a new passenger of our driver who is our current user there
                            JSONArray joArray =  new JSONArray(response);
                            int size = joArray.length();

                            if(size != 0) {
                                Log.i("TAG_array_null", "array not null !");
                                for (int i = 0; i < size; i++) {
                                    JSONObject jo_route = new JSONArray(response).getJSONObject(i);
                                    Log.i("TAG_boucle", "Boucle For");
                                    //Find in the JSON response the route chose by the passenger and stock the info into the String array
                                    if(jo_route.getInt("route") == routeID_chose){
                                        //Send an alert to the driver whith all the information
                                        AlertCall(PassengerUsername, passID, jo_route.getString("originAdress"),
                                                jo_route.getString("destinationAdress"),jo_route.getString("route_date"));
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG_error", error.toString());
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

}
