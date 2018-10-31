package mt.edu.um.getalift;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriveDetails extends AppCompatActivity {

    private static final String TAG = "DriveTAGDetails";
    Intent intentDetails;
    private int id;

    //Variable contenant l' id du driver obtenu par la première requete (elle est sous forme d'un string car la requete le donne dans ce format )
    private int driverIdQ;

    private TextView origins;
    private TextView destionation;
    private TextView driverTxt;
    private TextView druration;

    private int test;

    private List<User> passengerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intentDetails= getIntent();
        if (intentDetails != null) {
            id = intentDetails.getIntExtra("userId",0);
        }

        origins = findViewById(R.id.idOrigins);
        destionation = findViewById(R.id.idDestination);
        driverTxt = findViewById(R.id.idDriver);
        druration = findViewById(R.id.idDuration);


        driverTxt.setOnClickListener(driverBtn);


        //On met les requetes les unes apres les autres
        //Les details de la route + le driver
        driveDetailsList();

        //Liste des passagers
        passengerDetails();

    }

    public void driveDetailsList(){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/routes/" + Integer.toString(id);
        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            JSONObject jo = new JSONArray(response).getJSONObject(0);
                            origins.setText(jo.getString("originAdress"));
                            destionation.setText(jo.getString("destinationAdress"));
                            druration.setText(jo.getString("duration"));
                            driverIdQ = Integer.parseInt(jo.getString("driver"));
                            driverDetails(driverIdQ);

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

    // On parametre la requete pour qu'elle est besoin de la donnée que l'on recupère dans la première requete pour un problème de temporalité
    public void driverDetails(final int driverId){

    // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users/" + driverId;

        StringRequest srDriver = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        // We got a response from our server.
                        try {
                            JSONObject jo = new JSONArray(response).getJSONObject(0);
                            driverTxt.setText(jo.getString("username"));
                            test = jo.getInt("id");
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
        queue.add(srDriver);

    }

    public void passengerDetails(){


        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/passenger/information/" + Integer.toString(id);

        StringRequest srDriver = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {

                            int length = new JSONArray(response).length();
                            for (int i = 0;i<length;i++ ){
                                JSONObject jo = new JSONArray(response).getJSONObject(i);
                                User user = new User(jo.getInt("id"),jo.getString("surname"));
                                passengerList.add(user);
                            }
                            ArrayAdapter<User> adapter = new MyAdapter();
                            ListView passengers = findViewById(R.id.idList);
                            passengers.setAdapter(adapter);

                            // Set an item click listener for ListView
                            passengers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long idl) {
                                    // Get the selected item text from ListView
                                    User selectedItem = (User) parent.getItemAtPosition(position);
                                    int passengerId = selectedItem.getId();

                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    intent.putExtra("userId",passengerId);
                                    intent.putExtra("routeId",id);
                                    intent.putExtra("canRate",1);
                                    startActivity(intent);
                                }
                            });
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
        queue.add(srDriver);
    }

    private class MyAdapter extends ArrayAdapter<User>{
        public MyAdapter(){
            super(DriveDetails.this,R.layout.passenger_view,passengerList);
        }

        @Override
        public View getView(int position,  View convertView,  ViewGroup parent){
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.passenger_view, parent, false);
            }

            User currentUser = passengerList.get(position);

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.usr_icon);
            imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);

            // Name:
            TextView nameText = (TextView) itemView.findViewById(R.id.usr_name);
            nameText.setText(currentUser.getUsername());

            return itemView;
        }

    }

    private View.OnClickListener driverBtn =new View.OnClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(DriveDetails.this,ProfileActivity.class);
            intent.putExtra("userId",test);
            intent.putExtra("routeId",id);
            intent.putExtra("canRate",1);
            startActivity(intent);
        }
    };
}
