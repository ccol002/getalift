package mt.edu.um.getalift;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

//Google maps places API KEY AIzaSyD7ElaUB44FMGItFL3H6RbB7G-R8kJUAWI

public class HomeMapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "HomeMapActivity";

    private final static int MY_ACCESS_PERMISSION_CODE = 1;
    private final static String GoogleMapsAPIKey = "AIzaSyDTy7xW1utk3NLaG_HXk28KIBbVm4mgkp0";

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    private View mapsView;
    private View formView;
    private int mShortAnimationDuration;

    private Button BtnHomeSearch;
    private TextView showDateView;
    private AutoCompleteTextView mTextSearchOrigin;
    private AutoCompleteTextView mTextSearchDestination;

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapater;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(35.777609,14.15109), new LatLng(36.041529,14.619515));

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private FloatingActionButton myFab;

    private boolean isShowingMap;

    private double[] originPoint = {-360,-360};
    private double[] destinationPoint = {-360,-360};

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

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        mPlaceAutocompleteAdapater = new PlaceAutocompleteAdapter(this, googleApiClient, LAT_LNG_BOUNDS, null);

        mTextSearchOrigin.setAdapter(mPlaceAutocompleteAdapater);
        mTextSearchDestination.setAdapter(mPlaceAutocompleteAdapater);

        showDateView = findViewById(R.id.edt_home_date);

        Log.i(TAG, "Activity launched.....");

        context = getApplicationContext();
        CharSequence text = parseStringForURL("Universty of malta");
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void startResultSearchActivity() {
        getLatLongFromGivenAddress(mTextSearchDestination.getText().toString(),"destination");
        getLatLongFromGivenAddress(mTextSearchOrigin.getText().toString(),"origin");
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
            startActivity(new Intent(getBaseContext(), ProfileActivity.class));
        } else if (id == R.id.nav_lifts) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_settings) {

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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                setCameraPosition(location);
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
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONObject(response);
                            double[] arr = new double[2];

                            double lng = ((JSONArray)jo.get("results")).getJSONObject(0)
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lng");

                            double lat = ((JSONArray)jo.get("results")).getJSONObject(0)
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lat");

                            if(point.equals("origin")){
                                originPoint[0] = lat;
                                originPoint[1] = lng;
                            }else{
                                destinationPoint[0] = lat;
                                destinationPoint[1] = lng;
                            }

                            //If the origin field or the destination field is empty
                            if(destinationPoint[0] > -180 && destinationPoint[1] > -180 && originPoint[0] > -180 && originPoint[1] > -180) {
                                searchRide();
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

                }
        );
        queue.add(sr);
    }

    public void searchRide(){
        RequestQueue queue = Volley.newRequestQueue(this);

        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/findTarget";

        // We retrieve the search parameters
        final String startLat = Double.toString(originPoint[0]);
        final String startLng = Double.toString(originPoint[1]);
        final String endLat = Double.toString(destinationPoint[0]);
        final String endLng = Double.toString(destinationPoint[1]);
        final String startDate = ((EditText) findViewById(R.id.edt_home_date)).getText().toString().trim();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Intent intent = new Intent(getApplicationContext(), ResultSearchActivity.class);
                        intent.putExtra("JSON_RESULT", response);
                        startActivity(intent);
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
                params.put("startLng", startLng);
                params.put("startLat", startLat);
                params.put("endLng", endLng);
                params.put("endLat", endLat);
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
}